package hotelsearch.gateway;

import hotelsearch.dto.HotelsApiSearchResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/** A gateway encapsulates all the logic for communicating with another microservice/external API.
 *  Spring Boot annotations:
 *  {@code }@Value}: is primarily used for assigning default values to variables and method parameters. It also enables injecting values from property files into fields and method parameters.
 */

@Component
public class HotelsApiGateway {
    @Value("${external.hotel-search.url}")
    private String hotelSearchServiceUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(hotelSearchServiceUrl)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    public Mono<HotelsApiSearchResponse> getFromHotelSearchService(String endpoint, String jsonBody) {
        return this.webClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path(endpoint).build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonBody))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<HotelsApiSearchResponse>() {});
    }

}