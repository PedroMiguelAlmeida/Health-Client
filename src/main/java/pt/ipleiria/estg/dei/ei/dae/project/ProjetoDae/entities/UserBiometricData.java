package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import javax.persistence.ManyToOne;

public class UserBiometricData {

    @ManyToOne
    private User user;

    private String inputOrigin;

    private BiometricData biometricData;

    private String value;
}
