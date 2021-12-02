package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import java.io.Serializable;

public class QuantitativeBiometricData extends BiometricData implements Serializable {
    @ElementCollection
    @CollectionTable
}
