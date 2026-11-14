package shopping.product.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PurgoMalumClientConfig {
    private static final String BASE_URL = "https://www.purgomalum.com";

    @Bean
    public RestClient purgoMalumRestClient() {
        return RestClient.builder().baseUrl(BASE_URL).build();
    }
}
