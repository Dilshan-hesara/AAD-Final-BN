package lk.dilshanhesara.dilshan.hospitalmanagementsystembn;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class HospitalManagementSystemBnApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementSystemBnApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


//
//    @Bean
//    public CommandLineRunner initAdminUser(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
//        return args -> {
//            String plainPassword = "password123";
//
//            // --- Reset Super Admin Password ---
//            String superAdminUsername = "superadmin";
//            userAccountRepository.findByUsername(superAdminUsername).ifPresentOrElse(
//                    admin -> {
//                        String hashedPassword = passwordEncoder.encode(plainPassword);
//                        admin.setPassword(hashedPassword);
//                        userAccountRepository.save(admin);
//                        System.out.println("✅✅✅ Password for '" + superAdminUsername + "' has been reset successfully. ✅✅✅");
//                    },
//                    () -> System.out.println("❌ Superadmin user not found. Please ensure the user is in the database.")
//            );
//
//            // --- Reset Branch Admin Password ---
//            String branchAdminUsername = "colomboadmin";
//            userAccountRepository.findByUsername(branchAdminUsername).ifPresentOrElse(
//                    branchAdmin -> {
//                        String hashedPassword = passwordEncoder.encode(plainPassword);
//                        branchAdmin.setPassword(hashedPassword);
//                        userAccountRepository.save(branchAdmin);
//                        System.out.println("✅✅✅ Password for '" + branchAdminUsername + "' has been reset successfully. ✅✅✅");
//                    },
//                    () -> System.out.println("❌ Branch admin user ('colomboadmin') not found. Please ensure the user is in the database.")
//            );
//        };
//    }
}
