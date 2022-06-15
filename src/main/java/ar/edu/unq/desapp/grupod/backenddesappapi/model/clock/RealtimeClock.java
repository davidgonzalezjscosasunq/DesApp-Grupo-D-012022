package ar.edu.unq.desapp.grupod.backenddesappapi.model.clock;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Profile("prod")
public class RealtimeClock implements Clock {

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

}
