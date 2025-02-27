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
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "acknowledged", nullable = false, columnDefinition = "boolean default false")
    private Boolean acknowledged = false; //default value

    @Column(name = "status", nullable = false)
    private Integer status; // bitmap: FIRST_HEARTBEAT - MEMORY_FAILED - YARD_FAILED - ANALYZER_FAILED - RFCAT_FAILED - FAILED - ACTIVE

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
            if (signal.getAcknowledged() == null) {
                signal.setAcknowledged(false);
            }
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

        public Signal.Builder acknowledged(Boolean acknowledged) {
            signal.setAcknowledged(acknowledged != null ? acknowledged : false);
            return this;
        }

        public Signal.Builder status(Integer status) {
            signal.setStatus(status);
            return this;
        }
    }
}
