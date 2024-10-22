package codenomads.expensemanagement.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
@Component
public class TripGateway {

    private final WebClient webClient;

    @Autowired
    public TripGateway(WebClient.Builder webClientBuilder, @Value("${external.trip.url}") String tripServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(tripServiceUrl).build();
    }

    public boolean isUserInTrip(Long tripId, Long userId) {
        String token = (String) RequestContextHolder.getRequestAttributes()
            .getAttribute("jwt", RequestAttributes.SCOPE_REQUEST);

        return webClient.get()
            .uri("/trips/{tripId}/members/{userId}", tripId, userId)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .retrieve()
            .bodyToMono(Boolean.class)
            .block();
    }
}