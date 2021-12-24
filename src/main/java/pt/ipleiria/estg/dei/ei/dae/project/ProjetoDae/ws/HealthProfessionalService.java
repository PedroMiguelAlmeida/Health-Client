package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.HealthProfessionalDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.HealthProfessionalBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("HealthProfessionalService")
@Produces({"application/json"})
@Consumes({"application/json"})
public class HealthProfessionalService {
    @EJB
    private HealthProfessionalBean healthProfessionalBean;

    @GET
    @Path("/")
    public HealthProfessionalDTO toDTO(HealthProfessional healthProfessional) {
        return new HealthProfessionalDTO(healthProfessional.getUsername(), healthProfessional.getPassword(), healthProfessional.getName(), healthProfessional.getEmail(), healthProfessional.getVersion(), healthProfessional.getProfession(), healthProfessional.isChefe());
    }

    private List<HealthProfessionalDTO> toDTOs(List<HealthProfessional> healthProfessionals){
        return (List)healthProfessionals.stream().map(this::toDTO).collect(Collectors.toList());
    }
    @GET
    @Path("{username}")
    public Response getAdministratorDetails(@PathParam("username")String username){
        HealthProfessional healthProfessional = this.healthProfessionalBean.findHealthProfessional(username);
        return healthProfessional != null ? javax.ws.rs.core.Response.ok(this.toDTO(healthProfessional)).build() : javax.ws.rs.core.Response.status(Response.Status.NOT_FOUND).entity("ERROR_FINDING_ADMINISTRATOR").build();
    }
}
