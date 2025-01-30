package ar.edu.itba.tesis.webapp.dtos;

import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.Role;
import ar.edu.itba.tesis.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Set;

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
        boolean isOnlined = false;
        LocalDateTime lastHeartbeat = detector.getLastHeartbeat();
        if (lastHeartbeat != null && lastHeartbeat.isAfter(LocalDateTime.now().minusSeconds(150))) {
            isOnlined = true;
        }
        return new DetectorDto(
                detector.getId(),
                detector.getOwner().getId(),
                detector.getUser().getId(),
                // detector.getIsOnline(),
                isOnlined,
                detector.getVersion(),
                detector.getName(),
                detector.getDescription()
        );
    }

    public static List<DetectorDto> fromDetectors(List<Detector> detectors) {
        System.out.println("hola fromDetectors");
        System.out.println(detectors);
        System.out.println("chau fromDetectors");

        return detectors.stream().map(DetectorDto::fromDetector).toList();
    }
}
