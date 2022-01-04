package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.ejbs;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.Roles;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;
import java.util.List;
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
    @EJB
    AdministratorBean administratorBean;

    @EJB
    QuantitativeMeasureTypeBean quantitativeMeasureTypeBean;

    @EJB
    QualitativeMeasureTypeBean qualitativeMeasureTypeBean;

    @EJB
    MeasurementBean measurementBean;


    @PostConstruct
    private void populateDB() {
        try {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Creating patients>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            this.patientBean.create("patient1", "1234","patient1", "patient1@test.com", Roles.Patient,true);
            this.patientBean.create("patient2", "1234" , "patient2", "patient2@test.com", Roles.Patient,true);
            this.patientBean.create("patient3", "1234" , "patient3", "patient3@test.com", Roles.Patient,true);
            this.patientBean.create("patient4", "1234" , "patient4", "patient4@test.com", Roles.Patient,true);
            this.healthProfessionalBean.create("professional1","1234","professional1","professional1@gmail.com",0,"Doctor",true, Roles.HealthProfessional,true);
            this.healthProfessionalBean.create("professional2","1234","professional2","professional2@gmail.com",0,"Nurse",true,Roles.HealthProfessional,true);
            this.healthProfessionalBean.create("professional3","1234","professional3","professional3@gmail.com",0,"Nurse",false,Roles.HealthProfessional,true);
            this.administratorBean.create("admin1","1234","admin1","admin1@mail.pt",0,Roles.Admin,true);
            this.administratorBean.create("admin2","1234","admin2","admin2@mail.pt",0,Roles.Admin,true);

            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Creating QuantitativeMeasureTypes>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            this.quantitativeMeasureTypeBean.create("Temperature", false, 1, 100, true);
            this.quantitativeMeasureTypeBean.create("Altura", false, 1, 999, true);

            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Creating QualitativeMeasureTypes>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            List<String> values = new ArrayList<>();
            values.add("High");
            values.add("Medium");
            values.add("Low");
            this.qualitativeMeasureTypeBean.create("Temperature", false, values);
            this.qualitativeMeasureTypeBean.create("Altura", false, values);

            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Creating Measurements>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            this.measurementBean.create(1, "test", "test", "patient1");
            this.measurementBean.create(3, "Medium", "test", "patient2");


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
