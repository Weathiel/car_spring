package app.services;

import app.configuration.jwt.JwtUtils;
import app.dao.UserRepository;
import app.entity.Role;
import app.entity.User;
import app.entity.VerifyToken;
import app.entity.payloads.JwtRequest;
import app.entity.payloads.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserDetailsService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private RoleService roleService;
    private EmailService emailService;
    private VerifyTokenService verifyTokenService;
    private ReCaptchaService reCaptchaService;

    @Autowired
    public UserServiceImpl(ReCaptchaService reCaptchaService, BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, JwtUtils jwtUtils, AuthenticationManager authenticationManager, RoleService roleService, EmailService emailService, VerifyTokenService verifyTokenService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
        this.emailService = emailService;
        this.verifyTokenService = verifyTokenService;
        this.reCaptchaService = reCaptchaService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        if (!username.contains("@"))
            user = userRepository.findByUsername(username);
        else
            user = userRepository.findByEmail(username);
        Set<GrantedAuthority> authorities;
        if (user != null) {
            authorities = new HashSet<>();
            for (Role role :
                    user.getRole()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
            }
            user.setAuthorities(authorities);
        } else
            throw new UsernameNotFoundException("Username or email not found. Invalid user");

        return user;
    }

    public User getUserByToken(HttpServletRequest httpServletRequest) {
        User user = null;
        String jwt = parseJwt(httpServletRequest);
        String username = jwtUtils.getUsernameByToken(jwt);
        user = userRepository.findByUsername(username);
        return user;
    }

    public ResponseEntity<?> createAuthenticationToken(JwtRequest jwtRequest) throws Exception {
        Authentication authentication;
        try {
            authentication = authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        final String token = jwtUtils.generateJwtToken(authentication);
        JwtRequest payload = new JwtRequest();
        payload.setToken(token);
        if (jwtRequest.getUsername().contains("@")) {
            payload.setEmail(jwtRequest.getUsername());
            payload.setRole(userRepository.findByEmail(jwtRequest.getEmail()).getRole());
        } else {
            payload.setRole(userRepository.findByUsername(jwtRequest.getUsername()).getRole());
            payload.setUsername(jwtRequest.getUsername());
        }

        return ResponseEntity.ok(payload);
    }

    private Authentication authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("BAD_CREDENTIALS", e);
        }
    }

    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {

        User user = new User();
        VerifyToken verifyToken = new VerifyToken();
        String token;
        user.setUsername(registerRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleService.getRole("USER"));
        user.setRole(roleSet);
        user.setEnabled(false);
        userRepository.save(user);

        token = bCryptPasswordEncoder.encode(user.getUsername());
        token = token.replaceAll("[^a-zA-Z0-9]+", "");

        verifyToken.setUser(user);
        verifyToken.setToken(token);
        verifyTokenService.saveToken(verifyToken);

        emailService.sendSimpleMessage(registerRequest.getEmail(), "To complete registry: " + "http://localhost:8080/token/" + verifyToken.getToken());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<?> getUsersBySpecificRole() {
        List<User> userList;
        userList = userRepository.findAll();

        return ResponseEntity.ok(userList);
    }

    public ResponseEntity<?> changeRole(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);

        Role worker = new Role();
        worker.setRoleName("WORKER");
        if (!user.getRole().equals(worker)) {
            user.getRole().add(roleService.getRoleByRoleName("WORKER"));
            System.out.println(user.getRole().size());
            userRepository.save(user);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            Role admin = new Role();
            admin.setRoleName("ADMIN");
            if (user.getRole().contains(admin)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            userRepository.delete(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }

    private String parseJwt(HttpServletRequest httpServletRequest) {
        String headerAuthentication = httpServletRequest.getHeader("Authorization");

        if (StringUtils.hasText(headerAuthentication) && headerAuthentication.startsWith("Bearer ")) {
            return headerAuthentication.substring(7);
        }

        return null;
    }
}
