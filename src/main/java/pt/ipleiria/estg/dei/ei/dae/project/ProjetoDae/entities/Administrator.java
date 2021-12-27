package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAdministrators",
                query = "SELECT s FROM Administrator s ORDER BY s.name" // JPQL
        )
})
public class Administrator extends HospitalStaff implements Serializable {

    //region constructor
    public Administrator() {
    }

    public Administrator(String username, String password, String name, String email, int version, Roles role,boolean active) {
        super(username, password, name, email, version,role,active);
    }
//endregion


}