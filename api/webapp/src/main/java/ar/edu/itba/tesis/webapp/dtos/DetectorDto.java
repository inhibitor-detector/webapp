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
        System.out.println("hola time");
        System.out.println("lastHeartbeat:" + lastHeartbeat);
        if (lastHeartbeat != null) {
            System.out.println("isAfter:" + lastHeartbeat.isAfter(LocalDateTime.now().minusSeconds(150)));
        }
        System.out.println("LocalDateTime.now().minusSeconds(150):" + LocalDateTime.now().minusSeconds(150));
        System.out.println("isOnline:" + isOnline);
        System.out.println("chau time");

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
        System.out.println("hola fromDetectors");
        System.out.println(detectors);
        System.out.println("chau fromDetectors");

        return detectors.stream().map(DetectorDto::fromDetector).toList();
    }
}
