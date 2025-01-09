package ar.edu.itba.tesis.webapp.dtos;

import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.Signal;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record SignalDto(
        Long id,

        @NotNull
        Long detectorId,

        @DateTimeFormat
        String timestamp,

        Boolean isHeartbeat,

        Boolean status) {

    public static SignalDto fromSignal(Signal signal) {
        return new SignalDto(
                signal.getId(),
                signal.getDetector().getId(),
                signal.getTimestamp().toString(),
                signal.getIsHeartbeat(),
                signal.getStatus()
        );
    }

    public static List<SignalDto> fromSignals(List<Signal> signals) {
        return signals.stream().map(SignalDto::fromSignal).toList();
    }
}
