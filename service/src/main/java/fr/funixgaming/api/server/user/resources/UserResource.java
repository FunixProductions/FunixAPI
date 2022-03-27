package fr.funixgaming.api.server.user.resources;

import fr.funixgaming.api.client.user.clients.UserClient;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.resources.ApiResource;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.mappers.UserAuthMapper;
import fr.funixgaming.api.server.user.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserResource extends ApiResource<UserDTO, UserService> implements UserClient {
    private final UserAuthMapper authMapper;
    private final AuthenticationManager authenticationManager;

    public UserResource(UserService userService,
                        UserAuthMapper authMapper,
                        AuthenticationManager authenticationManager) {
        super(userService);
        this.authMapper = authMapper;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDTO findById(String id) {
        if (id.equals("self")) {
            final UserDTO userDTO = super.getService().getCurrentUser();

            if (userDTO == null) {
                throw new ApiException("Vous n'êtes pas connecté à l'application.");
            } else {
                return userDTO;
            }
        } else {
            return super.findById(id);
        }
    }

    @Override
    public UserDTO register(UserCreationDTO request) {
        if (request.getPassword().equals(request.getPasswordConfirmation())) {
            final UserDTO userDTO = this.authMapper.toDto(request);

            userDTO.setRole(UserRole.USER);
            return super.create(userDTO);
        } else {
            throw new ApiBadRequestException("Les mots de passe ne correspondent pas.");
        }
    }

    @Override
    public UserTokenDTO login(UserLoginDTO request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(), request.getPassword()
                            )
                    );

            User user = (User) authenticate.getPrincipal();

            return super.getService().generateAccessToken(user);
        } catch (BadCredentialsException ex) {
            throw new ApiBadRequestException("Vos identifiants sont incorrects.", ex);
        }
    }
}
