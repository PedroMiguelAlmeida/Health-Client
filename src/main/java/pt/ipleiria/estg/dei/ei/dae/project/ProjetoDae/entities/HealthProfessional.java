


package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllHealthProfessionals",
                query = "SELECT s FROM HealthProfessional s ORDER BY s.name" // JPQL
        )
})

//@Table(
//        name = "HEALTHPROFESSIONALS",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"USERNAME"})
//)
public class HealthProfessional extends HospitalStaff implements Serializable {
    //region attributes
    private String profession;
    private boolean chefe;

    @ManyToMany
    @JoinTable(name = "HEALTHPROFESSIONALS_PATIENTS",
    joinColumns = @JoinColumn(name = "HEALTHPROFESSIONALS_USERNAME",referencedColumnName = "USERNAME"),
    inverseJoinColumns = @JoinColumn(name = "PATIENT_USERNAME",referencedColumnName = "USERNAME"))
    List<Patient> patients;

//endregion attributes

    //region constructors
    public HealthProfessional() {
    }

    public HealthProfessional(String username, String password, String name, String email, int version, String profession,  boolean chefe, Roles role,boolean active) {
        super(username, password, name, email, version, role,active);
        this.profession = profession;
        this.chefe = chefe;
        this.patients = getPatients();
    }
//endregion constructors

    //region getters&setters


    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

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



    public void addPatients( Patient patient) {
        for (Patient patienti:patients) {
            if (patient.equals(patienti)){
                System.out.println("This patient is already on the list");
                return;
            }
        }
       if (!patients.add(patient)){
           System.out.println("ERRO HealthProfessional add ");
       }else{
           System.out.println("Paciente add");
       }
    }

    public void removePatients(Patient patient){patients.removeIf(patient::equals);}


}

