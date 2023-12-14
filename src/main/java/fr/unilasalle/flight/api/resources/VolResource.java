package fr.unilasalle.flight.api.resources;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.unilasalle.flight.api.beans.Reservation;
import fr.unilasalle.flight.api.beans.Vol;
import fr.unilasalle.flight.api.repositories.ReservationRepository;
import fr.unilasalle.flight.api.repositories.VolRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VolResource extends GenericResource{
    
    @Inject
    private VolRepository repository;

    @Inject
    private ReservationRepository reservationRepository;
    
    @Inject
    Validator validator;

    @GET
    public Response getFlights(@QueryParam("destination") String destination) {
        List<Vol> list;
        if (StringUtils.isBlank(destination)) {
            list = repository.listAll();
        } else {
            list = repository.findByDestination(destination);
        }
        return getOr404(list);
    }

    @GET
    @Path("/{id}")
    public Response getFlight(@PathParam("id") Long id) {
        var vol = repository
                .findByIdOptional(id).orElse(null);
        return getOr404(vol);
    }

    @POST
    @Transactional
    public Response createFlight(Vol flight) {
        var violations = validator.validate(flight);
        if (!violations.isEmpty()) {
            return Response.status(400)
                .entity(new ErrorWrapper(violations))
                .build();
        }

        Vol tmpVol = repository.find("number", flight.getNumber()).firstResult();

        if (tmpVol == null) {
            try {
                repository.persistAndFlush(flight);
                return Response.status(201).build();
            } catch (PersistenceException e) {
                return Response.serverError()
                    .entity(new ErrorWrapper(e.getMessage()))
                    .build();
            } 
        } else {
            return Response.status(400)
            .entity(new ErrorWrapper("Un vol possède déjà ce numéro."))
            .build();
        }
    }

    @DELETE
    @Transactional
    public Response deleteFlight(Vol flight) {
        var violations = validator.validate(flight);
        if (!violations.isEmpty()) {
            return Response.status(400)
                .entity(new ErrorWrapper(violations))
                .build();
        }

        List<Reservation> bookings = reservationRepository.findByFlightID(flight.getId());

        try {
            repository.delete(flight);
            for (Reservation booking : bookings) {
                reservationRepository.delete(booking);
            }
            return Response.status(200).build();
        } catch (PersistenceException e) {
            return Response.serverError()
                .entity(new ErrorWrapper(e.getMessage()))
                .build();
        }
    }
}
