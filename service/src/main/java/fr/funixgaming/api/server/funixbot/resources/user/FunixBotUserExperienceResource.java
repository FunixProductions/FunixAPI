package fr.funixgaming.api.server.funixbot.resources.user;

import fr.funixgaming.api.client.funixbot.clients.FunixBotUserExperienceClient;
import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserExperienceDTO;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import fr.funixgaming.api.server.funixbot.services.user.FunixBotUserExperienceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funixbot/user/exp")
public class FunixBotUserExperienceResource extends ApiResource<FunixBotUserExperienceDTO, FunixBotUserExperienceService> implements FunixBotUserExperienceClient {
    public FunixBotUserExperienceResource(FunixBotUserExperienceService funixBotUserExperienceService) {
        super(funixBotUserExperienceService);
    }
}
