package codenomads.carhire_search.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import codenomads.carhire_search.dto.CarhireSearchCriteria;
import reactor.core.publisher.Mono;
import java.net.URI;

@Component
public class CarhireSearchGateway {

    private final WebClient webClient;

    public CarhireSearchGateway(WebClient.Builder webClientBuilder, @Value("${external.carhire-search.provider-url}") String providerUrl) {
        this.webClient = webClientBuilder.baseUrl(providerUrl).build();
    }

    public Mono<JsonNode> searchCarhires(CarhireSearchCriteria criteria) {
        return webClient.method(HttpMethod.GET)
            .uri("")
            .bodyValue(criteria)
            .retrieve()
            .bodyToMono(JsonNode.class);
    }
}