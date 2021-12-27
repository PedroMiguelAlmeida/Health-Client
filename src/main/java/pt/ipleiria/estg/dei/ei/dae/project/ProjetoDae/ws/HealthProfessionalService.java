package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.HealthProfessionalDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.HealthProfessionalBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("HealthProfessionals")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class HealthProfessionalService {
    @EJB
    private HealthProfessionalBean healthProfessionalBean;

    @GET
    @Path("/")
    public List<HealthProfessionalDTO> getAllStudentsWS() {
        return toDTOs(healthProfessionalBean.getAllHealthProfessionals());
    }
    // Converts an entity Student to a DTO Student class
    private HealthProfessionalDTO toDTO(HealthProfessional professional) {
        return new HealthProfessionalDTO(
                professional.getUsername(),
                professional.getPassword(),
                professional.getName(),
                professional.getEmail(),
                professional.getVersion(),
                professional.getProfession(),
                professional.isChefe(),
                professional.getRole(),
                professional.isActive()
        );
    }
    // converts an entire list of entities into a list of DTOs
    private List<HealthProfessionalDTO> toDTOs(List<HealthProfessional> professionals) {
        return professionals.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{username}")
    public Response getAdministratorDetails(@PathParam("username")String username){
        HealthProfessional healthProfessional = this.healthProfessionalBean.findHealthProfessional(username);
        return healthProfessional != null ? javax.ws.rs.core.Response.ok(this.toDTO(healthProfessional)).build() : javax.ws.rs.core.Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_ADMINISTRATOR").build();
    }

    @PUT
    @Path("{username}")
    public Response update(@PathParam("username")String username, HealthProfessional healthProfessional) throws MyEntityNotFoundException {
        HealthProfessional updateHealthProfessional = this.healthProfessionalBean.findHealthProfessional(username);

        updateHealthProfessional.setChefe(healthProfessional.isChefe());
        updateHealthProfessional.setProfession(healthProfessional.getProfession());
        updateHealthProfessional.setName(healthProfessional.getName());
        updateHealthProfessional.setEmail(healthProfessional.getEmail());
        updateHealthProfessional.setRole(healthProfessional.getRole());
        updateHealthProfessional.setVersion(healthProfessional.getVersion()+1);
        updateHealthProfessional.setActive(healthProfessional.isActive());

        healthProfessionalBean.update(updateHealthProfessional);
        return Response.ok().build();
    }

    @POST
    @Path("/")
    public Response createNewPatient (HealthProfessionalDTO professionalDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        healthProfessionalBean.create(
                professionalDTO.getUsername(),
                professionalDTO.getPassword(),
                professionalDTO.getName(),
                professionalDTO.getEmail(),
                professionalDTO.getVersion(),
                professionalDTO.getProfession(),
                professionalDTO.isChefe(),
                professionalDTO.getRole(),
                professionalDTO.isActive());
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{username}")
    public Response delete(@PathParam("username")String username){
        HealthProfessional deleteHealthProfessional = this.healthProfessionalBean.findHealthProfessional(username);

        healthProfessionalBean.delete(deleteHealthProfessional);

        return Response.ok().build();
    }

}
