package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("administrators")
@Produces({"application/json"})
@Consumes({"application/json"})
public class AdministratorService {
    @EJB
    private AdministratorBean administratorBean;

    @GET
    @Path("/")
    public List<AdministratorDTO> getAllAdministratorWS(){return this.toDTOs(this.administratorBean.getAllAdministrators());}

    private AdministratorDTO toDTO(Administrator administrator){
        return new AdministratorDTO(administrator.getUsername(),administrator.getPassword(), administrator.getName(), administrator.getEmail(),administrator.getVersion());
    }

    private List<AdministratorDTO> toDTOs(List<Administrator> administrators){
        return (List)administrators.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{username}")
    public Response getAdministratorDetails(@PathParam("username")String username){
        Administrator administrator = this.administratorBean.findAdministrator(username);
        return administrator != null ? javax.ws.rs.core.Response.ok(this.toDTO(administrator)).build() : javax.ws.rs.core.Response.status(Status.NOT_FOUND).entity("ERROR_FINDING_ADMINISTRATOR").build();    }
}
