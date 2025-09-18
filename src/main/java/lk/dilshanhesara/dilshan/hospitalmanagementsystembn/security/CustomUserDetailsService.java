package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.security;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));


        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userAccount.getRole().name());

        System.out.println("🔹 Logging in user: " + username + " | Assigning Authority: " + authority.getAuthority());

        return new User(userAccount.getUsername(), userAccount.getPassword(), Collections.singletonList(authority));
    }
}
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserAccountRepository userAccountRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserAccount userAccount = userAccountRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//
//        return new User(
//                userAccount.getUsername(),
//                userAccount.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority(userAccount.getRole().name()))
//        );
//    }
//}