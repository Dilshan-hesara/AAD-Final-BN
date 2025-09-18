package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AuthRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AuthResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.OnlineUserRegistrationDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.RegistrationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.security.JwtTokenProvider;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.OnlineUserService;
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

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String redirectUrl = tokenProvider.generateToken(authentication);
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);

        return ResponseEntity.ok(new AuthResponseDto(true, "Login Successful!", redirectUrl, role));
    }

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


        private final OnlineUserService onlineUserService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody OnlineUserRegistrationDto registrationDto) {
        try {
            onlineUserService.registerNewOnlineUser(registrationDto);
            return ResponseEntity.ok("User registered successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}

