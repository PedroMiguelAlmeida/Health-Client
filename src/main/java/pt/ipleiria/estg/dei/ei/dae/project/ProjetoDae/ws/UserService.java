package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.User;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("Users")
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
                user.getEmail()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<UserDTO> toDTOs(List<User> users) {
        return (List)users.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
