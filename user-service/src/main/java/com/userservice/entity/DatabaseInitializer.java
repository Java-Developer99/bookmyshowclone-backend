package com.userservice.entity;

import com.userservice.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        initRoles();
    }

    private void initRoles() {


            // Check if roles already exist to avoid duplicates
            if (roleRepository.count() == 0) {
                // Create ROLE_USER
                Role userRole = new Role();
                userRole.setName(ERole.ROLE_USER);
                roleRepository.save(userRole);

                // Create ROLE_ADMIN
                Role adminRole = new Role();
                adminRole.setName(ERole.ROLE_ADMIN);
                roleRepository.save(adminRole);

                System.out.println("Roles have been initialized in the database");
            }

        }
    }
