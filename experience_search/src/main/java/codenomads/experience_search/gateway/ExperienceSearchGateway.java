package codenomads.experience_search.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import codenomads.experience_search.dto.ExperienceSearchCriteria;
import reactor.core.publisher.Mono;

@Component
public class ExperienceSearchGateway {

    private final WebClient webClient;

    public ExperienceSearchGateway(WebClient.Builder webClientBuilder, @Value("${external.experience-search.provider-url}") String providerUrl) {
        this.webClient = webClientBuilder.baseUrl(providerUrl).build();
    }

    public Mono<JsonNode> searchExperiences(ExperienceSearchCriteria criteria) {
        return webClient.method(HttpMethod.GET)
            .uri("")
            .bodyValue(criteria)
            .retrieve()
            .bodyToMono(JsonNode.class);
    }
}