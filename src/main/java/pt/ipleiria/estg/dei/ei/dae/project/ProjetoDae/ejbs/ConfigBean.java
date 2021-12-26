package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Level;
import java.util.logging.Logger;

@Startup
@Singleton
public class ConfigBean {
    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @EJB
    PatientBean patientBean;
    @EJB
    HealthProfessionalBean healthProfessionalBean;

    @PostConstruct
    private void populateDB() {
        try {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Creating patients>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            this.patientBean.create("patient1", "1234", "patient1", "patient1@test.com");
            this.patientBean.create("patient2", "1234", "patient2", "patient2@test.com");
            this.patientBean.create("patient3", "1234", "patient3", "patient3@test.com");
            this.patientBean.create("patient4", "1234", "patient4", "patient4@test.com");
            this.healthProfessionalBean.create("professional1","1234","professional1","professional1@gmail.com",0,"Doctor",true);
            this.healthProfessionalBean.create("professional2","1234","professional2","professional2@gmail.com",0,"Nurse",true);
            this.healthProfessionalBean.create("professional3","1234","professional3","professional3@gmail.com",0,"Nurse",false);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
