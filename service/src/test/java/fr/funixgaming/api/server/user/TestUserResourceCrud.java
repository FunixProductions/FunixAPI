package fr.funixgaming.api.server.user;

import com.google.common.reflect.TypeToken;
import fr.funixgaming.api.client.user.dtos.requests.UserAdminDTO;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.server.user.components.UserTestComponent;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.entities.UserToken;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import fr.funixgaming.api.server.user.repositories.UserTokenRepository;
import fr.funixgaming.api.server.utils.JsonHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestUserResourceCrud {

    private final MockMvc mockMvc;
    private final JsonHelper jsonHelper;
    private final UserTestComponent userTestComponent;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;

    private final String route;
    private final String bearerToken;

    @Autowired
    public TestUserResourceCrud(MockMvc mockMvc,
                                JsonHelper jsonHelper,
                                UserTestComponent userTestComponent,
                                UserRepository userRepository,
                                UserTokenRepository userTokenRepository) throws Exception {
        final UserTokenDTO tokenDTO = userTestComponent.loginUser(userTestComponent.createAdminAccount());

        this.mockMvc = mockMvc;
        this.jsonHelper = jsonHelper;
        this.route = "/user/";
        this.bearerToken = tokenDTO.getToken();
        this.userTestComponent = userTestComponent;
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
    }

    @Test
    public void testAccessUser() throws Exception {
        final User user = userTestComponent.createBasicUser();
        final UserTokenDTO token = userTestComponent.loginUser(user);

        mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAccessModo() throws Exception {
        final User user = userTestComponent.createModoAccount();
        final UserTokenDTO token = userTestComponent.loginUser(user);

        mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAll() throws Exception {
        final MvcResult result = mockMvc.perform(get(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();

        final Type type = new TypeToken<Set<UserDTO>>(){}.getType();
        final Set<UserDTO> users = jsonHelper.fromJson(result.getResponse().getContentAsString(), type);
    }

    @Test
    public void testCreate() throws Exception {
        final UserAdminDTO userDTO = createUser();

        final MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();

        final UserDTO user = getResponse(result);
    }

    @Test
    public void testPatch() throws Exception {
        final UserAdminDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        user.setUsername("sdkjfhsdkjh");
        result = mockMvc.perform(patch(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(user)))
                .andExpect(status().isOk())
                .andReturn();

        user = getResponse(result);
    }

    @Test
    public void testFindById() throws Exception {
        final UserAdminDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        result = mockMvc.perform(get(route + "/" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk())
                .andReturn();
        user = getResponse(result);
    }

    @Test
    public void testRemoveId() throws Exception {
        final UserAdminDTO userDTO = createUser();

        MvcResult result = mockMvc.perform(post(route)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonHelper.toJson(userDTO)))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO user = getResponse(result);

        UserToken userToken = new UserToken();
        userToken.setUser(userRepository.findByUuid(user.getId().toString()).get());
        userToken.setToken("jshdlqkfjhslkh");
        userToken.setExpirationDate(Date.from(Instant.now().plusSeconds(10000)));
        userToken = this.userTokenRepository.save(userToken);

        mockMvc.perform(delete(route + "?id=" + user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken))
                .andExpect(status().isOk());

        assertFalse(this.userTokenRepository.findByUuid(userToken.getUuid().toString()).isPresent());
    }

    private UserDTO getResponse(final MvcResult result) throws Exception {
        return jsonHelper.fromJson(result.getResponse().getContentAsString(), UserDTO.class);
    }

    private UserAdminDTO createUser() {
        final UserAdminDTO userDTO = new UserAdminDTO();

        userDTO.setUsername("OUI");
        userDTO.setRole(UserRole.USER);
        userDTO.setEmail("oui@ggmail.com");
        userDTO.setPassword("password");
        return userDTO;
    }
}
