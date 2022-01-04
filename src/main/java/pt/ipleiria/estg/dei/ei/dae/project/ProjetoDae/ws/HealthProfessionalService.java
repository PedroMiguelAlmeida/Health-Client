package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.HealthProfessionalDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.PatientDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.UpdatePasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.HealthProfessionalBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("healthProfessionals")
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
    public Response update(@PathParam("username")String username, HealthProfessionalDTO healthProfessionalDTO) throws MyEntityNotFoundException {
        healthProfessionalBean.update(username, healthProfessionalDTO.getName(),
                healthProfessionalDTO.getEmail(), healthProfessionalDTO.getProfession(), healthProfessionalDTO.isActive());

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/")
    public Response createNewPatient (HealthProfessionalDTO professionalDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException, MessagingException {
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

    @PUT
    @Path("{username}/updatePassword")
    public Response updatePassword(@PathParam("username")String username, UpdatePasswordDTO updatePasswordDTO) throws MyEntityNotFoundException {

        System.out.println(updatePasswordDTO.getPassword());
        System.out.println(updatePasswordDTO.getToken());
        healthProfessionalBean.updatePassword(username,updatePasswordDTO.getPassword(), updatePasswordDTO.getToken());
        return  Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{username}")
    public Response delete(@PathParam("username")String username) throws MyEntityNotFoundException {
        HealthProfessional healthProfessional = healthProfessionalBean.findHealthProfessional(username);
        if(healthProfessional != null && healthProfessional.isActive() == true){
            healthProfessionalBean.delete(username);
        }else if( healthProfessional.isActive() == false){
            return  Response.status(Response.Status.FORBIDDEN).build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

}
