package com.taxi.service;

import com.taxi.entity.Driver;
import com.taxi.entity.Role;
import com.taxi.entity.User;
import com.taxi.repository.DriverRepository;
import com.taxi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final RoleRepository roleRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    public User save(User user, String roleId) {
        Role role = roleRepository.findById(roleId).get();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        System.out.println(user.getUserPassword());
        Driver p = new Driver(
                user.getUserName(),
                passwordEncoder.encode(user.getUserPassword()),
                user.getUserFirstName(),
                user.getUserLastName()
        );
        p.setRole(userRoles);
        return driverRepository.save(p);
    }

    public Driver findDriverById(String id) {
        return driverRepository.findById(id).get();
    }
}
