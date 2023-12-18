package fr.unilasalle.flight.api.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "flights")
public class Vol extends PanacheEntityBase {
    
    @Id
    @SequenceGenerator(name = "flights_sequence_in_java_code", sequenceName = "flights_sequence_in_database", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flights_sequence_in_java_code")
    private Long id;

    @NotBlank(message = "Number cannot be null")
    @Column(nullable = false, unique = true)
    private String number;

    @NotBlank(message = "Origin cannot be null")
    @Column(nullable = false)
    private String origin;

    @NotBlank(message = "Destination cannot be null")
    @Column(nullable = false)
    private String destination;

    @NotNull
    @Column(nullable = false)
    private LocalDate departure_date;

    @NotNull
    @Column(nullable = false)
    private LocalTime departure_time;

    @NotNull
    @Column(nullable = false)
    private LocalDate arrival_date;

    @NotNull
    @Column(nullable = false)
    private LocalTime arrival_time;

    @NotNull
    @Column(nullable = false)
    private Long plane_id;
}
