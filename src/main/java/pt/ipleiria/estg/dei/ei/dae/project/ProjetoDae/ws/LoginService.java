
package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ws;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import java.text.ParseException;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos.AuthDTO;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.JwtBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs.UserBean;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.jwt.Jwt;

@Path("/auth")
public class LoginService {

    private static final Logger log = Logger.getLogger(LoginService.class.getName());

    @EJB
    JwtBean jwtBean;
    @EJB
    UserBean userBean;

    public LoginService() {
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(AuthDTO authDTO) {
        try {
            User user = this.userBean.authenticate(authDTO.getUsername(), authDTO.getPassword());
            if(user.isActive() == false){
                return Response.status(Status.FORBIDDEN).build();
            }
            if (user != null) {
                if (user.getUsername() != null) {
                    log.info("Generating JWT for user " + user.getUsername());
                }

                String token = this.jwtBean.createJwt(user.getUsername(), new String[]{user.getClass().getSimpleName()});
                return Response.ok(new Jwt(token)).build();
            }
        } catch (Exception var5) {
            log.info(var5.getMessage());
        }

        return Response.status(Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/user")
    public Response demonstrateClaims(@HeaderParam("Authorization") String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                JWT j = JWTParser.parse(auth.substring(7));
                return Response.ok(j.getJWTClaimsSet().getClaims()).build();
            } catch (ParseException var3) {
                log.warning(var3.toString());
                return Response.status(400).build();
            }
        } else {
            return Response.status(204).build();
        }
    }
}
