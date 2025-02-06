package ar.edu.itba.tesis.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "detectors")
@Getter
@Setter
public class Detector {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detectors_id_seq")
    @SequenceGenerator(name = "detectors_id_seq", sequenceName = "detectors_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;
    
    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    /*
        Builder for Detector
     */
    public static Detector.Builder builder() {
        return new Detector.Builder();
    }

    public static class Builder {
        private final Detector detector;

        public Builder() {
            detector = new Detector();
        }

        public Detector build() {
            return detector;
        }

        public Detector.Builder owner(User owner) {
            detector.setOwner(owner);
            return this;
        }

        public Detector.Builder user(User user) {
            detector.setUser(user);
            return this;
        }

        public Detector.Builder lastHeartbeat(LocalDateTime lastHeartbeat) {
            detector.setLastHeartbeat(lastHeartbeat);
            return this;
        }
        
        public Detector.Builder version(String version) {
            detector.setVersion(version);
            return this;
        }

        public Detector.Builder name(String name) {
            detector.setName(name);
            return this;
        }

        public Detector.Builder description(String description) {
            detector.setDescription(description);
            return this;
        }
    }
}
