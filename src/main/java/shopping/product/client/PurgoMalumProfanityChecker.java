package shopping.product.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import shopping.product.domain.ProfanityChecker;

@Component
public class PurgoMalumProfanityChecker implements ProfanityChecker {
    private final RestClient restClient;

    public PurgoMalumProfanityChecker(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public boolean containsProfanity(String text) {
        String body = restClient.get()
                .uri("/service/containsprofanity?text={text}", text)
                .retrieve()
                .body(String.class);
        return Boolean.parseBoolean(body);
    }
}