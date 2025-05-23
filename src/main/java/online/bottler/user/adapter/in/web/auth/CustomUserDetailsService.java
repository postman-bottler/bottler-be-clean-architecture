package online.bottler.user.adapter.in.web.auth;

import lombok.RequiredArgsConstructor;
import online.bottler.user.application.port.out.UserPersistencePort;
import online.bottler.user.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserPersistencePort userPersistencePort;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userPersistencePort.findByEmail(email);
        return new CustomUserDetails(user);
    }
}
