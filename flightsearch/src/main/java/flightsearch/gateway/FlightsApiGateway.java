package flightsearch.gateway;

import flightsearch.dto.FlightSearchResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;

/** A gateway encapsulates all the logic for communicating with another microservice/external API.
 *  Spring Boot annotations:
 *  {@code }@Value}: is primarily used for assigning default values to variables and method parameters. It also enables injecting values from property files into fields and method parameters.
 */

@Component
public class FlightsApiGateway {
    @Value("${external.flight-search.url}")
    private String flightSearchServiceUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(flightSearchServiceUrl)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    public Mono<FlightSearchResponse> getFromFlightsApi(String endpoint, Map<String, String> queryStringParams) {
        return this.webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder b = uriBuilder.path(endpoint);
                    for (Map.Entry<String, String> param : queryStringParams.entrySet()) {
                        b.queryParam(param.getKey(), param.getValue());
                    }
                    return b.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<FlightSearchResponse>() {});
    }
}
