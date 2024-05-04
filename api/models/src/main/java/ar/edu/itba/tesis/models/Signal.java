package ar.edu.itba.tesis.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name = "signals")
@Getter
@Setter
public class Signal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "signals_id_seq")
    @SequenceGenerator(name = "signals_id_seq", sequenceName = "signals_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "detector_id")
    private Detector detector;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "is_heartbeat", nullable = false)
    private Boolean isHeartbeat;

    /*
        Builder for Signal
     */
    public static Signal.Builder builder() {
        return new Signal.Builder();
    }

    public static class Builder {
        private final Signal signal;

        public Builder() {
            signal = new Signal();
        }

        public Signal build() {
            return signal;
        }

        public Signal.Builder detector(Detector detector) {
            signal.setDetector(detector);
            return this;
        }

        public Signal.Builder timestamp(LocalDateTime timestamp) {
            signal.setTimestamp(timestamp);
            return this;
        }

        public Signal.Builder isHeartbeat(Boolean isHeartbeat) {
            signal.setIsHeartbeat(isHeartbeat);
            return this;
        }

    }
}
