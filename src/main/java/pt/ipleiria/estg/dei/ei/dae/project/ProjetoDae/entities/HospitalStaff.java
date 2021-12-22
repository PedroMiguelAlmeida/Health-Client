package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import java.io.Serializable;

public class HospitalStaff extends User implements Serializable {

    public HospitalStaff() {
    }

    public HospitalStaff(String username, String password, String name, String email, int version) {
        super(username, password, name, email, version);
    }

    public Patient createPatient(String username,String password,String name,String email,int version){
       return new Patient(username,password,name,email,version);
    }

    public void updatePatient(String username,String password,String name,String email){

    }
}
