package app.services;

import app.dao.UserRepository;
import app.dao.VerifyTokenRepository;
import app.entity.User;
import app.entity.VerifyToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VerifyTokenService {

    private VerifyTokenRepository verifyTokenRepository;
    private UserRepository userRepository;

    @Autowired
    public VerifyTokenService(VerifyTokenRepository verifyTokenRepository, UserRepository userRepository) {
        this.verifyTokenRepository = verifyTokenRepository;
        this.userRepository = userRepository;
    }

    public void saveToken(VerifyToken verifyToken) {
        verifyTokenRepository.save(verifyToken);
    }

    public ResponseEntity<Void> enableUser(String token) {
        VerifyToken verifyToken = verifyTokenRepository.findByToken(token);
        User user = verifyToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verifyTokenRepository.deleteById(verifyToken.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
