package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.HealthProfessionalDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("Administrators")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AdministratorService {
    @EJB
    private AdministratorBean administratorBean;

    @GET
    @Path("/")
    public List<AdministratorDTO> getAllAdministratorWS(){return this.toDTOs(this.administratorBean.getAllAdministrators());}

    private AdministratorDTO toDTO(Administrator administrator){
        return new AdministratorDTO(administrator.getUsername(),administrator.getPassword(), administrator.getName(), administrator.getEmail(),administrator.getVersion(),administrator.getRole(),administrator.isActive());
    }

    private List<AdministratorDTO> toDTOs(List<Administrator> administrators){
        return (List)administrators.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{username}")
    public Response getAdministratorDetails(@PathParam("username")String username){
        Administrator administrator = this.administratorBean.findAdministrator(username);
        return administrator != null ? javax.ws.rs.core.Response.ok(this.toDTO(administrator)).build() : javax.ws.rs.core.Response.status(Status.NOT_FOUND).entity("ERROR_FINDING_ADMINISTRATOR").build();
    }

    @PUT
    @Path("{username}")
    public Response update(@PathParam("username")String username, Administrator administrator) throws MyEntityNotFoundException {
        Administrator updateAdministrator = this.administratorBean.findAdministrator(username);

        updateAdministrator.setEmail(administrator.getEmail());
        updateAdministrator.setActive(administrator.isActive());
        updateAdministrator.setRole(administrator.getRole());
        updateAdministrator.setName(administrator.getName());
        updateAdministrator.setVersion(administrator.getVersion() + 1);



        administratorBean.update(updateAdministrator);
        return Response.ok().build();
    }

    @POST
    @Path("/")
    public Response createNewPatient (AdministratorDTO administratorDTO) throws MyEntityExistsException, MyEntityNotFoundException {
        administratorBean.create(
                administratorDTO.getUsername(),
                administratorDTO.getPassword(),
                administratorDTO.getName(),
                administratorDTO.getEmail(),
                administratorDTO.getVersion(),
                administratorDTO.getRole(),
                administratorDTO.isActive()
        );
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{username}")
    public Response delete(@PathParam("username")String username){
        Administrator deleteAdministrator = this.administratorBean.findAdministrator(username);

        administratorBean.delete(deleteAdministrator);

       return Response.ok().build();
    }
}
