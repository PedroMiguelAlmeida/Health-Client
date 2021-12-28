package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.MeasureTypeType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({@NamedQuery(
        name = "getAllQuantitativeMeasureTypes",
        query = "SELECT a FROM QuantitativeMeasureType a"
)})
@Entity
public class QuantitativeMeasureType extends MeasureType implements Serializable {

    @NotNull
    private double min;
    @NotNull
    private double max;
    @NotNull
    private boolean decimal;

    public QuantitativeMeasureType() {

    }

    public QuantitativeMeasureType(String name, boolean multiple, MeasureTypeType type, double min, double max, boolean decimal) {
        super(name, multiple, type);
        this.min = min;
        this.max = max;
        this.decimal = decimal;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public boolean isDecimal() {
        return decimal;
    }

    public void setDecimal(boolean decimal) {
        this.decimal = decimal;
    }
}
