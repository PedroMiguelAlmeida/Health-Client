


package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.util.List;


@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllHealthProfessionals",
                query = "SELECT s FROM User s ORDER BY s.name" // JPQL
        )
})
public class HealthProfessional extends HospitalStaff implements Serializable {
    //region attributes
    private String profession;
    private boolean chefe;


//    private List<Patient> patients;
//endregion attributes

    //region constructors
    public HealthProfessional() {
    }

    public HealthProfessional(String username, String password, String name, String email, int version, String profession,  boolean chefe) {
        super(username, password, name, email, version);
        this.profession = profession;
        this.chefe = chefe;
    }
//endregion constructors

    //region getters&setters
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }


    public boolean isChefe() {
        return chefe;
    }

    public void setChefe(boolean chefe) {
        this.chefe = chefe;
    }

//endregion
}

