package com.taxi.service;

import com.taxi.entity.Role;
import com.taxi.entity.User;
import com.taxi.repository.RoleRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RoleRepository roleRepository;
    private final PassengerService passengerService;
    private final DriverService driverService;

    public void initRoleAndUser() {
        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        adminRole.setRoleDescription("Admin role");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("DRIVER");
        userRole.setRoleDescription("Default role for newly created record");
        roleRepository.save(userRole);

        Role pas = new Role();
        pas.setRoleName("PASSENGER");
        pas.setRoleDescription("Default role for newly created record");
        roleRepository.save(pas);
    }

    public User registerNewUser(User user, String roleId) throws NotFoundException {
        switch (roleId) {
            case "DRIVER":
                return driverService.save(user, roleId);
            case "PASSENGER":
                return passengerService.save(user, roleId);
            default:
                throw new NotFoundException("Not found that role!");
        }
    }
}
