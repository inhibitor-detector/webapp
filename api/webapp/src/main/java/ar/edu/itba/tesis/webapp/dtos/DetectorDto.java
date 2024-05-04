package ar.edu.itba.tesis.webapp.dtos;

import ar.edu.itba.tesis.models.Detector;
import ar.edu.itba.tesis.models.Role;
import ar.edu.itba.tesis.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public record DetectorDto(
        Long id,

        @NotNull
        Long ownerId,

        @NotNull
        Long userId,

        @NotNull
        Boolean isOnline,

        @NotNull
        String version,

        @NotNull
        String name,

        @NotNull
        String description

        ) {

    public static DetectorDto fromDetector(Detector detector) {
        return new DetectorDto(
                detector.getId(),
                detector.getOwner().getId(),
                detector.getUser().getId(),
                detector.getIsOnline(),
                detector.getVersion(),
                detector.getName(),
                detector.getDescription()
        );
    }

    public static List<DetectorDto> fromDetectors(List<Detector> detectors) {
        return detectors.stream().map(DetectorDto::fromDetector).toList();
    }
}
