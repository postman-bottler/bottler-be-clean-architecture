package online.bottler.auth;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuditorAware implements AuditorAware<Long> {

    @NotNull
    @Override
    public Optional<Long> getCurrentAuditor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails customUserDetails) {
            return Optional.ofNullable(customUserDetails.getUserId());
        }

        return Optional.empty();
    }
}
