package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class Clock {

    private LocalDateTime now = LocalDateTime.now();

    public LocalDateTime now() {
        return now;
    }

    public void advanceMinutes(int numberOfMinutesToAdvanceTime) {

    }
}
