package fr.unilasalle.flight.api.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "planes")
public class Avion extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "planes_sequence_in_java_code", sequenceName = "planes_sequence_in_database", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planes_sequence_in_java_code")
    private Long id;

    @NotBlank(message = "Operator cannot be null")
    @Column(nullable = false)
    private String operator;

    @NotBlank(message = "Model cannot be null")
    @Column(nullable = false)
    private String model;

    @NotBlank(message = "Immatriculation cannot be null")
    @Size(max = 6, message = "Immatriculation is maximum 6 characters long")
    @Column(nullable = false, unique = true)
    private String immatriculation;

    @NotNull(message = "Capacity cannot be null")
    @Column(nullable = false)
    @Min(value = 2, message = "Capacity must be at least 2")
    private Integer capacity;
}