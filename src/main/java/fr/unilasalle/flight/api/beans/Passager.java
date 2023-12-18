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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "passengers")
public class Passager extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "passengers_sequence_in_java_code", sequenceName = "passengers_sequence_in_database", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passengers_sequence_in_java_code")
    private Long id;

    @NotBlank(message = "Surname cannot be null")
    @Column(nullable = false)
    private String surname;

    @NotBlank(message = "Firstname cannot be null")
    @Column(nullable = false)
    private String firstname;

    @NotBlank(message = "Email address cannot be null")
    @Column(nullable = false, unique = true)
    private String email_address;
}