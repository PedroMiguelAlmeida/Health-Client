package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import io.smallrye.common.constraint.Nullable;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.MeasureTypeType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({@NamedQuery(
        name = "getAllQualitativeMeasureTypes",
        query = "SELECT a FROM QualitativeMeasureType a"
)})
@Entity
public class QualitativeMeasureType extends MeasureType implements Serializable {

    @Nullable
    @ElementCollection
    @CollectionTable(name="QUALITATIVE_VALUES")
    private List<String> values;

    public QualitativeMeasureType() {
    }

    public QualitativeMeasureType(String name, boolean multiple, MeasureTypeType type, int version, boolean active, List<String> values) {
        super(name, multiple, type, version, active);
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
