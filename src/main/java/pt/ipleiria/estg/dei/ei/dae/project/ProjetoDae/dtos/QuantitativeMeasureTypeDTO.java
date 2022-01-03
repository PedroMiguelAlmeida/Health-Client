package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos;

public class QuantitativeMeasureTypeDTO {

    private int id;
    private String name;
    private boolean multiple;
    private double min;
    private double max;
    private boolean decimal;

    public QuantitativeMeasureTypeDTO() {
    }

    public QuantitativeMeasureTypeDTO(int id, String name, boolean multiple, double min, double max, boolean decimal) {
        this.id = id;
        this.name = name;
        this.multiple = multiple;
        this.min = min;
        this.max = max;
        this.decimal = decimal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
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
