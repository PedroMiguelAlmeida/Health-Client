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

    public ConfigBean() {
    }

    @PostConstruct
    private void populateDB() {
        try {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Creating patients>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            this.patientBean.create("patient1", "1234", "patient1", "patient1@test.com");
            this.patientBean.create("patient2", "1234", "patient2", "patient2@test.com");
            this.patientBean.create("patient3", "1234", "patient3", "patient3@test.com");
            this.patientBean.create("patient4", "1234", "patient4", "patient4@test.com");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
