package com.company;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.company.employees.entity.User;
import com.company.employees.repository.UserRepository;

@SpringBootApplication
public class EmployeeManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApiApplication.class, args);
    }
     @Bean
    CommandLineRunner createAdminUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123")) // 🔐 hashed
                        .role("ADMIN")
                        .build();

                userRepository.save(admin);
                System.out.println("✅ Admin user created");
            }
        };
    }
}
