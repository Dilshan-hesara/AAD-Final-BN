package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AuthRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AuthResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.RegistrationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.security.JwtTokenProvider;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    private final UserService userService; // Inject UserService

//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequestDto loginRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = tokenProvider.generateToken(authentication);
//        String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
//        return ResponseEntity.ok(new AuthResponseDto(true, "Login Successful!", jwt, role));
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authenticateUser(@RequestBody AuthRequestDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst().orElse(null);

            // If login is successful, return a 200 OK response with the token and role
            return ResponseEntity.ok(new AuthResponseDto(true, "Login Successful!", jwt, role));

        } catch (BadCredentialsException e) {
            // If login fails (wrong username/password), return a 401 Unauthorized response
            return ResponseEntity.status(401).body(new AuthResponseDto(false, "Invalid username or password.", null, null));
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequestDto registrationDto) {
        try {
            userService.registerOnlineUser(registrationDto);
            return ResponseEntity.ok("User registered successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}

