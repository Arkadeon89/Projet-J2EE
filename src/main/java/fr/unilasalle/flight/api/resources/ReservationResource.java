package fr.unilasalle.flight.api.resources;

import java.util.List;

import fr.unilasalle.flight.api.beans.Passager;
import fr.unilasalle.flight.api.beans.Reservation;
import fr.unilasalle.flight.api.beans.Vol;
import fr.unilasalle.flight.api.repositories.AvionRepository;
import fr.unilasalle.flight.api.repositories.PassagerRepository;
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
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource extends GenericResource{

    @Inject
    private ReservationRepository repository;

    @Inject
    private VolRepository volRepository;

    @Inject
    private AvionRepository avionRepository;

    @Inject
    private PassagerRepository passagerRepository;

    @Inject
    Validator validator;

    @GET
    public Response getBookings(@QueryParam("flight_id") Long flight_id){
        List<Reservation> list;
        if (flight_id == null) {
            list = repository.listAll();
        } else {
            list = repository.findByFlightID(flight_id);
        }
        return getOr404(list);
    }

    @POST
    @Transactional
    public Response createBooking(Reservation booking) {
        var violations = validator.validate(booking);
        if (!violations.isEmpty()) {
            return Response.status(400)
                .entity(new ErrorWrapper(violations))
                .build();
        }

        Vol vol = volRepository.findByIdOptional(booking.getFlight_id()).orElse(null);
        Passager passager = passagerRepository.findByIdOptional(booking.getPassenger_id()).orElse(null);
        Boolean isFull = false;

        if (vol != null) {
            List<Reservation> bookingsInFlight = repository.findByFlightID(vol.getId());
            Integer planeCapacity = avionRepository.findById(vol.getPlane_id()).getCapacity();
            if (bookingsInFlight.size() >= planeCapacity) {
                isFull = true;
            }
        }
        

        if (vol != null && passager != null && !isFull) {
            try {
                repository.persistAndFlush(booking);
                return Response.status(201).build();
            } catch (PersistenceException e) {
                return Response.serverError()
                    .entity(new ErrorWrapper(e.getMessage()))
                    .build();
            }   
        } else {
            return Response.status(400)
                .entity(new ErrorWrapper("Le vol ou le passager n'existe pas, ou le vol est complet."))
                .build();
        }
    }

    @POST
    @Path("/passenger")
    @Transactional
    public Response createBookingAndPassenger(@QueryParam("flight_id") Long flight_id, Passager passenger){
        var violations = validator.validate(passenger);
        if (!violations.isEmpty()) {
            return Response.status(400)
                .entity(new ErrorWrapper(violations))
                .build();
        }
        
        Passager tmpPassager;

        try {
            passagerRepository.persistAndFlush(passenger);
            tmpPassager = passagerRepository.find("id", passenger.getId()).firstResult();

            Vol vol = volRepository.findByIdOptional(flight_id).orElse(null);
            Boolean isFull = false;

            if (vol != null) {
                List<Reservation> bookingsInFlight = repository.findByFlightID(vol.getId());
                Integer planeCapacity = avionRepository.findById(vol.getPlane_id()).getCapacity();
                if (bookingsInFlight.size() >= planeCapacity) {
                    isFull = true;
                }
            }

            if (vol != null && !isFull) {
                try {
                    Reservation booking = new Reservation();
                    booking.setFlight_id(flight_id);
                    booking.setPassenger_id(tmpPassager.getId());
                    repository.persistAndFlush(booking);
                    return Response.status(201).build();
                } catch (PersistenceException e) {
                    return Response.serverError()
                        .entity(new ErrorWrapper(e.getMessage()))
                        .build();
                }   
            } else {
                return Response.status(400)
                    .entity(new ErrorWrapper("Le vol est complet ou n'existe pas."))
                    .build();
            }
        } catch (PersistenceException e) {
            return Response.serverError()
                .entity(new ErrorWrapper(e.getMessage()))
                .build();
        }

        
    }

    @DELETE
    @Transactional
    public Response deleteFlight(Reservation booking) {
        var violations = validator.validate(booking);
        if (!violations.isEmpty()) {
            return Response.status(400)
                .entity(new ErrorWrapper(violations))
                .build();
        }

        try {
            repository.delete(booking);
            return Response.status(200).build();
        } catch (PersistenceException e) {
            return Response.serverError()
                .entity(new ErrorWrapper(e.getMessage()))
                .build();
        }
    }
}