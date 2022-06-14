package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class TradedVolumeBodyDTO {
    @JsonProperty
    private LocalDateTime start;
    @JsonProperty
    private LocalDateTime end;

    public TradedVolumeBodyDTO(LocalDateTime start, LocalDateTime end){
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
