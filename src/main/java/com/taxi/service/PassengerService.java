package com.taxi.service;

import com.taxi.entity.Passenger;
import com.taxi.entity.Role;
import com.taxi.entity.User;
import com.taxi.repository.PassengerRepository;
import com.taxi.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final RoleRepository roleRepository;
    private final PassengerRepository passengerRepository;
    private final PasswordEncoder passwordEncoder;

    public User save(User user, String roleId) {
        Role role = roleRepository.findById(roleId).get();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        Passenger p = new Passenger(
                user.getUserName(),
                passwordEncoder.encode(user.getUserPassword()),
                user.getUserFirstName(),
                user.getUserLastName()
        );

        p.setRole(userRoles);
        System.out.println(223);

        return passengerRepository.save(p);
    }

    public Passenger findById(String id) {
        return passengerRepository.findById(id).get();
    }
}
