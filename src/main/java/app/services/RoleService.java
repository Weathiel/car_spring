package app.services;

import app.dao.RoleRepository;
import app.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    public RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRole(String name) {
        return roleRepository.findFirstByRoleName(name);
    }

    public Role getRoleById(Long id) {
        return roleRepository.getOne(id);
    }

    public Role getRoleByRoleName(String rolename) {
        return roleRepository.findFirstByRoleName(rolename);
    }
}
