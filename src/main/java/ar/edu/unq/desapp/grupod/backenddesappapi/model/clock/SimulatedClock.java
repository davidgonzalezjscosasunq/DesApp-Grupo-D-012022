package ar.edu.unq.desapp.grupod.backenddesappapi.model.clock;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SimulatedClock implements Clock {

    private LocalDateTime now = LocalDateTime.now();

    public LocalDateTime now() {
        return now;
    }

    public void advanceMinutes(Integer numberOfMinutesToAdvanceTime) {
        now = now.plusMinutes(numberOfMinutesToAdvanceTime);
    }
}
