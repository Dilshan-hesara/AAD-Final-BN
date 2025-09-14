package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;


    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        userService.processOAuthPostLogin(email, name);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        String jwt = tokenProvider.generateToken(newAuthentication);

        response.sendRedirect("http://localhost:63342/Hospital-Management-System-FN/oauth2-redirect.html?token=" + jwt);
    }
}