package com.william.puzzle.config;

import com.william.puzzle.enums.Role;
import com.william.puzzle.service.store.UserStoreServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {
    private final UserStoreServiceImpl userStoreService;
    private final AdminProperties adminProperties;

    @PostConstruct
    public void createAdminUserIfMissing() {
        try {
            userStoreService.addUser(
                    adminProperties.getUsername(),
                    adminProperties.getPassword(),
                    Role.ADMIN
            );
            log.info("Admin user created: " + adminProperties.getUsername());
        } catch (Exception e) {
            log.warn("Failed to create admin user: " + e.getMessage());
        }
    }
}
