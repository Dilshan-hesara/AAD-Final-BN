package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // We will use BCrypt now
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public APIs
                        .requestMatchers("/api/auth/**").permitAll()

                        // APIs for ANY logged-in user (OnlineUser, Admin, etc.)
                        .requestMatchers("/api/staff/my-profile").authenticated()
                        .requestMatchers("/api/branches").authenticated()
                        .requestMatchers("/api/doctors/by-branch/**").authenticated()



//                        // APIs for specific roles
//                        .requestMatchers("/api/doctors/**").hasAuthority("ROLE_BRANCH_ADMIN")
//                        .requestMatchers("/api/patients/**").hasAuthority("ROLE_BRANCH_ADMIN")
//                        .requestMatchers("/api/receptionists/**").hasAuthority("ROLE_BRANCH_ADMIN")
//                        .requestMatchers("/api/appointments/**").hasAuthority("ROLE_BRANCH_ADMIN")
//                        .requestMatchers("/api/user/**").hasAuthority("ROLE_ONLINEUSER")

                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}