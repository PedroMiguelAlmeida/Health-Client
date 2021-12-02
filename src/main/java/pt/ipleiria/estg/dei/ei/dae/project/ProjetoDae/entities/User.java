package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllUsers",
                query = "SELECT s FROM User s ORDER BY s.name" // JPQL
        )
})
public class User {
    @Id
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @Email
    @NotNull
    private String email;
    @Version
    private int version;

}
