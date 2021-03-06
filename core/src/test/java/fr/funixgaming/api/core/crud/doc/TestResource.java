package fr.funixgaming.api.core.crud.doc;

import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestResource extends ApiResource<TestDTO, TestService> implements TestClient {
    public TestResource(TestService service) {
        super(service);
    }
}
