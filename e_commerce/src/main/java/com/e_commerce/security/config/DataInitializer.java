package com.e_commerce.security.config;

import com.e_commerce.model.Permission;
import com.e_commerce.model.Role;
import com.e_commerce.model.UserApp;
import com.e_commerce.repository.IPermissionRepository;
import com.e_commerce.repository.IRoleRepository;
import com.e_commerce.repository.IUserAppRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(IUserAppRepository userRepo,
                                   IRoleRepository roleRepo,
                                   PasswordEncoder passwordEncoder,
                                   IPermissionRepository permissionRepository) {
        return args -> {
            // Solo crear el admin si no hay usuarios
            if (userRepo.count() == 0) {

                Permission createPermission = permissionRepository
                        .findByPermissionName("CREATE")
                        .orElseGet(() -> permissionRepository.save(new Permission(null, "CREATE")));

                createPermission = permissionRepository.findById(createPermission.getId())
                        .orElseThrow(() -> new RuntimeException("Permission 'CREATE' not found."));

                Role adminRole = roleRepo.findByRole("ADMIN").orElse(null);
                if (adminRole == null) {
                    Set<Permission> permissionSet = new HashSet<>();
                    permissionSet.add(createPermission);
                    Role newRole = new Role();
                    newRole.setRole("ADMIN");
                    newRole.setPermissionSet(permissionSet);
                    adminRole = roleRepo.save(newRole);
                }

                UserApp admin = new UserApp();
                admin.setName("Administrador");
                admin.setLastName("Del Sistema");
                admin.setEmail("admin@tudominio.com");
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEnable(true);
                admin.setAccountNotExpired(true);
                admin.setAccountNotLocked(true);
                admin.setCredentialNotExpired(true);
                admin.getRoleSet().add(adminRole);
                userRepo.save(admin);
            }
        };
    }
}
