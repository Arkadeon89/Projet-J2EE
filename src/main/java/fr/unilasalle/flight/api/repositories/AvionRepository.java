package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Avion;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class AvionRepository implements
        PanacheRepositoryBase<Avion, Long> {

    public List<Avion> findByOperator(String operatorParameter) {
        return find("operator", operatorParameter).list();
    }
}
