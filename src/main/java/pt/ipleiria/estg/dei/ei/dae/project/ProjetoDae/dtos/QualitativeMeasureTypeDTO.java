package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos;

import java.util.ArrayList;
import java.util.List;

public class QualitativeMeasureTypeDTO {

    private int id;
    private String name;
    private boolean multiple;
    private List<String> values;

    public QualitativeMeasureTypeDTO() {

    }

    public QualitativeMeasureTypeDTO(int id, String name, boolean multiple, List<String> values) {
        this.id = id;
        this.name = name;
        this.multiple = multiple;
        this.values = values;
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

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void addValues(String value) {
        this.values.add(value);
    }

    public void removeValues(String value) {
        this.values.remove(value);
    }
}
