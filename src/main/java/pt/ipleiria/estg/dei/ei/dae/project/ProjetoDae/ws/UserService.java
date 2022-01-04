package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.UpdatePasswordDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.User;
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

@Path("users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserService {
    @EJB
    private UserBean userBean;

    @GET
    @Path("/")
    public List<UserDTO> getAllUsersWS() {
        return toDTOs(userBean.getAllUsers());
    }
    // Converts an entity Student to a DTO Student class
    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<UserDTO> toDTOs(List<User> users) {
        return (List)users.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PUT
    @Path("{username}/updatePassword")
    public Response updatePassword(@PathParam("username")String username, UpdatePasswordDTO updatePasswordDTO) throws MyEntityNotFoundException {

        System.out.println(updatePasswordDTO.getPassword());
        System.out.println(updatePasswordDTO.getToken());
        userBean.updatePassword(username,updatePasswordDTO.getPassword(), updatePasswordDTO.getToken());
        userBean.deleteToken(username, updatePasswordDTO.getToken());
        return  Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("{username}/changePassword")
    public Response changePassword(@PathParam("username")String username) throws MyConstraintViolationException, MessagingException, MyEntityNotFoundException, MyEntityExistsException {

        userBean.sendEmailToChangePassword(username);
        return Response.status(Response.Status.OK).build();
    }

}
