package shopping.product.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class PurgoMalumProfanityCheckerTest {
    private static final String BASE_URL = "https://www.purgomalum.com";
    private static final String CHECK_URL = BASE_URL + "/service/containsprofanity";

    private MockRestServiceServer server;
    private PurgoMalumProfanityChecker checker;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(builder).build();
        checker = new PurgoMalumProfanityChecker(builder.build());
    }

    @AfterEach
    void tearDown() {
        server.verify();
    }

    @Test
    void 서버가_true라고_답하면_비속어가_있다고_판단한다() {
        server.expect(requestTo(startsWith(CHECK_URL)))
                .andRespond(withSuccess("true", MediaType.TEXT_PLAIN));

        boolean result = checker.containsProfanity("비속어컵");

        assertThat(result).isTrue();
    }

    @Test
    void 서버가_false라고_답하면_비속어가_없다고_판단한다() {
        server.expect(requestTo(startsWith(CHECK_URL)))
                .andRespond(withSuccess("false", MediaType.TEXT_PLAIN));

        boolean result = checker.containsProfanity("컵");

        assertThat(result).isFalse();
    }

    @Test
    void 특수문자가_든_이름도_안전하게_변환해서_전달한다() {
        server.expect(requestTo(startsWith(CHECK_URL)))
                .andExpect(queryParam("text", "a%26b%2Bc"))
                .andRespond(withSuccess("false", MediaType.TEXT_PLAIN));

        checker.containsProfanity("a&b+c");
    }
}