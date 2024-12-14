package at.cgsit.demo.health;

import at.cgsit.demo.repository.TestEntityRepository;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import jakarta.enterprise.context.ApplicationScoped;

// check if we are ok
@Liveness
@ApplicationScoped
public class TestHealthCheck implements HealthCheck {

    @Inject
    TestEntityRepository testEntityRepository;

    @Override
    public HealthCheckResponse call() {
        boolean isHealthy = checkHealth();
        return HealthCheckResponse.named("yes there are test messages. OK ")
                .status(isHealthy)
                .build();
    }

    private boolean checkHealth() {
        // Add custom health logic
        Long l = testEntityRepository.countChatMessags();
        if (l > 0) {
            return true;
        }
        return false;
    }
}


