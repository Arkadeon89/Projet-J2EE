package fr.unilasalle.flight.api.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bookings")
public class Reservation extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "bookings_sequence_in_java_code", sequenceName = "bookings_sequence_in_database", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookings_sequence_in_java_code")
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long flight_id;

    @NotNull
    @Column(nullable = false)
    private Long passenger_id;
}