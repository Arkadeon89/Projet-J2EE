package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Reservation;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class ReservationRepository implements
        PanacheRepositoryBase<Reservation, Long>{

    public List<Reservation> findByFlightID(Long flight_idParameter) {
        return find("flight_id", flight_idParameter).list();
    }
}