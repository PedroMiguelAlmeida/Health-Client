package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.common.constraint.Nullable;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Prescription {

    @Id
    private int id;
    @NotNull
    private List<String> treatment;
    @Nullable
    private List<Measurement> measurements;
    @NotNull
    private HealthProfessional healthProfessional;
    @NotNull
    private Patient patient;
    @NotNull
    private String description;

}
