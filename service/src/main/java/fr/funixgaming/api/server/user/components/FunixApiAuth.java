package fr.funixgaming.api.server.user.components;

import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class FunixApiAuth implements AuthenticationManager {
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            final String username = authentication.getPrincipal().toString();
            final String password = authentication.getCredentials().toString();
            final User user = userService.loadUserByUsername(username);

            if (user.getPassword().equals(password)) {
                return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            } else {
                throw new BadCredentialsException("Bad credentials");
            }
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
