package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class ConfigBean {
    @EJB
    PatientBean patientBean;


    @PostConstruct
    public void populateDB() {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Creating patients>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        this.patientBean.create("patient1", "patient1", "patient1", "patient1@test.com");
        this.patientBean.create("patient2", "patient2", "patient2", "patient2@test.com");
        this.patientBean.create("patient3", "patient3", "patient3", "patient3@test.com");
        this.patientBean.create("patient4", "patient4", "patient4", "patient4@test.com");
    }
}