package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;


import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.AdministratorDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.HealthProfessionalDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.UpdatePasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.AdministratorBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.TokenBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Administrator;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.Patient;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.HealthProfessional;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("administrators")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class AdministratorService {
    @EJB
    private AdministratorBean administratorBean;
    @EJB
    private TokenBean tokenBean;

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
    public Response update(@PathParam("username")String username, AdministratorDTO administratorDTO) throws MyEntityNotFoundException {

        System.err.println("HELP ServiceAdmin Username "+ administratorDTO.getUsername()+" name: "+administratorDTO.getName()+"email: "+administratorDTO.getEmail());
        administratorBean.update(username,administratorDTO.getName(),administratorDTO.getEmail(),administratorDTO.isActive());

        return Response.status(Response.Status.OK).build();
    }

//    @PUT
//    @Path("{username}/updatePassword")
//    public Response updatePassword(@PathParam("username")String username, UpdatePasswordDTO updatePasswordDTO) throws MyEntityNotFoundException {
//
//        System.out.println(updatePasswordDTO.getPassword());
//        System.out.println(updatePasswordDTO.getToken());
//        administratorBean.updatePassword(username,updatePasswordDTO.getPassword(), updatePasswordDTO.getToken());
//        administratorBean.deleteToken(username,updatePasswordDTO.getToken());
//        return  Response.status(Response.Status.OK).build();
//    }
//
//    @POST
//    @Path("{username}/changePassword")
//    public Response changePassword(@PathParam("username")String username) throws MyConstraintViolationException, MessagingException, MyEntityNotFoundException, MyEntityExistsException {
//
//        administratorBean.sendEmailToChangePassword(username);
//        return Response.status(Response.Status.OK).build();
//    }

    @POST
    @Path("/")
    public Response createNewAdmin (AdministratorDTO administratorDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException, MessagingException {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        administratorBean.create(
                administratorDTO.getUsername(),
                generatedString,
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
    public Response delete(@PathParam("username")String username) throws MyEntityNotFoundException {
        Administrator administrator = administratorBean.findAdministrator(username);
        if(administrator != null && administrator.isActive() == true){
            administratorBean.delete(username);
        }else if( administrator.isActive() == false){
            return  Response.status(Response.Status.FORBIDDEN).build();
        }else{
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}
