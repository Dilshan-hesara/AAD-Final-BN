package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity

public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> {})
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("/api/auth/**", "/uploads/**", "/api/password-reset/**").permitAll()
//
//                        // ADD THIS RULE: Allow public access to the uploads folder
//                        .requestMatchers("/uploads/**").permitAll()
//                        // =========================================================
//                        .requestMatchers("/api/messages/**").hasAnyAuthority("ROLE_BRANCH_ADMIN", "ROLE_RECEPTIONIST")
//                        .requestMatchers("/api/staff/my-profile").hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_BRANCH_ADMIN", "ROLE_RECEPTIONIST")
//                        // All other requests must be authenticated
//                        .anyRequest().authenticated()
//                )
//                // ... your existing cors, csrf, sessionManagement ...
//                .authorizeHttpRequests(auth -> auth
//                                .requestMatchers("/api/auth/**", "/oauth2/**").permitAll() // <-- Allow OAuth2 endpoints
//                        // ... your other rules
//                )

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/oauth2/**", "/uploads/**").permitAll()
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }





    // ... your other beans



//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> {})
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**").permitAll() // Login/Register
//                        .anyRequest().authenticated() // All
//                        // other requests require a valid token
//                        .requestMatchers("/api/staff/my-profile").authenticated()
//                        .requestMatchers("/api/branches").authenticated()
//                        .requestMatchers("/api/doctors/by-branch/**").authenticated()
//
//                        // =============================================================
//                        // ADD THIS RULE: Allow Branch Admins to access the receptionists API
//                        .requestMatchers("/api/receptionists/**").hasAuthority("ROLE_BRANCH_ADMIN")
//                        // =============================================================
//
//                        .requestMatchers("/api/doctors/**").hasAuthority("ROLE_BRANCH_ADMIN")
//                        .requestMatchers("/api/patients/**").hasAuthority("ROLE_BRANCH_ADMIN")
//                        .requestMatchers("/api/appointments/**").hasAuthority("ROLE_BRANCH_ADMIN")
//                        .requestMatchers("/api/user/**").hasAuthority("ROLE_ONLINEUSER")
//
//
//                )
//
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

}