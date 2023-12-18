package fr.unilasalle.flight.api.resources;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.unilasalle.flight.api.beans.Passager;
import fr.unilasalle.flight.api.repositories.PassagerRepository;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/passengers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PassagerResource extends GenericResource {
    
    @Inject
    private PassagerRepository repository;
    
    @Inject
    Validator validator;

    @GET
    public Response getPassengers() {
        List<Passager> list;
        list = repository.listAll();
        return getOr404(list);
    }

    @GET
    @Path("/{id}")
    public Response getPassenger(@PathParam("id") Long id) {
        var passager = repository
                .findByIdOptional(id).orElse(null);
        return getOr404(passager);
    }

    @POST
    @Transactional
    public Response createPassenger(Passager passenger) {
        var violations = validator.validate(passenger);
        if (!violations.isEmpty()) {
            return Response.status(400)
                .entity(new ErrorWrapper(violations))
                .build();
        }

        Passager tmpPassager = repository.find("email_address", passenger.getEmail_address()).firstResult();

        if (tmpPassager == null) {
            try {
                repository.persistAndFlush(passenger);
                return Response.status(201).build();
            } catch (PersistenceException e) {
                return Response.serverError()
                    .entity(new ErrorWrapper(e.getMessage()))
                    .build();
            }
        } else {
            return Response.status(400)
                .entity(new ErrorWrapper("Un client possède déjà cette adresse email."))
                .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response editPassenger(@PathParam("id") Long id, @QueryParam("surname") String surname, @QueryParam("firstname") String firstname, @QueryParam("email_address") String email_address) {
        var passager = repository.findByIdOptional(id).orElse(null);
        if (passager == null) {
            return Response.status(404).build();
        }

        if (StringUtils.isNotBlank(surname)) {
            passager.setSurname(surname);
        }
        if (StringUtils.isNotBlank(firstname)) {
            passager.setFirstname(firstname);
        }
        if (StringUtils.isNotBlank(email_address)) {
            Passager tmpPassager = repository.find("email_address", email_address).firstResult();
            if (tmpPassager != null) {
                return Response.status(400)
                .entity(new ErrorWrapper("Un client possède déjà cette adresse email."))
                .build();
            }
            passager.setEmail_address(email_address);
            
        }

        var violations = validator.validate(passager);
        if (!violations.isEmpty()) {
            return Response.status(400)
                .entity(new ErrorWrapper(violations))
                .build();
        }

        try {
            repository.persistAndFlush(passager);
            return Response.status(200).build();
        } catch (PersistenceException e) {
            return Response.serverError()
            .entity(new ErrorWrapper(e.getMessage()))
            .build();
        }
        
    }
}