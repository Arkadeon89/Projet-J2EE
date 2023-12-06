package fr.unilasalle.flight.api.repositories;

import java.util.List;

import fr.unilasalle.flight.api.beans.Vol;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public class VolRepository implements PanacheRepositoryBase<Vol, Long>{
    
    public List<Vol> findByNumber(String numberParameter) {
        return find("number", numberParameter).list();
    }
}
