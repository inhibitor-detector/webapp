package ar.edu.itba.tesis.webapp.dtos;

import java.time.LocalDateTime;
import java.util.List;

import ar.edu.itba.tesis.models.Detector;
import jakarta.validation.constraints.NotNull;

public record DetectorDto(
        Long id,

        @NotNull
        Long ownerId,

        @NotNull
        Long userId,

        // @NotNull
        Boolean isOnline,

        @NotNull
        String version,

        @NotNull
        String name,

        @NotNull
        String description

        ) {

    public static DetectorDto fromDetector(Detector detector) {
        boolean isOnline = false;
        LocalDateTime lastHeartbeat = detector.getLastHeartbeat();
        if (lastHeartbeat != null && lastHeartbeat.isAfter(LocalDateTime.now().minusSeconds(150))) {
            isOnline = true;
        }

        return new DetectorDto(
                detector.getId(),
                detector.getOwner().getId(),
                detector.getUser().getId(),
                // detector.getIsOnline(),
                isOnline,
                detector.getVersion(),
                detector.getName(),
                detector.getDescription()
        );
    }

    public static List<DetectorDto> fromDetectors(List<Detector> detectors) {
        return detectors.stream().map(DetectorDto::fromDetector).toList();
    }
}
