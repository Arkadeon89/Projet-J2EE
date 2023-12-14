package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Passager;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

@Model
public class PassagerRepository implements
        PanacheRepositoryBase<Passager, Long>{
            
        }