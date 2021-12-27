package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;

import java.io.Serializable;

public class HospitalStaff extends User implements Serializable {

    public HospitalStaff() {
    }

    public HospitalStaff(String username, String password, String name, String email, int version, Roles role,boolean active) {
        super(username, password, name, email,version,role,active);
    }

}
