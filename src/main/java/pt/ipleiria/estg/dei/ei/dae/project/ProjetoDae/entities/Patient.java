package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

public class Patient extends User implements Serializable {

    @OneToMany(mappedBy = "pacient", cascade = CascadeType.REMOVE)
    private List<Measurement> UserBiometricDataList;

    private List<Prescription> prescription;


}

