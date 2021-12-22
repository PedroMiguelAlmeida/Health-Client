package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAdministrators",
                query = "SELECT s FROM User s ORDER BY s.name" // JPQL
        )
})
public class Administrator extends User implements Serializable {

    //region constructor
    public Administrator() {
    }

    public Administrator(String username, String password, String name, String email, int version) {
        super(username, password, name, email, version);
    }
//endregion

}