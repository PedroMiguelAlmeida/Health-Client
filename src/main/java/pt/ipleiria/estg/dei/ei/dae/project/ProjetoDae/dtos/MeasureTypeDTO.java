package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos;

import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.MeasureTypeType;

public class MeasureTypeDTO {

    private int id;
    private String name;
    private boolean multiple;
    private MeasureTypeType type;

    public MeasureTypeDTO() {
    }

    public MeasureTypeDTO(int id, String name, boolean multiple, MeasureTypeType type) {
        this.id = id;
        this.name = name;
        this.multiple = multiple;
        this.type = type;
    }

    public int getId() {
        return id;
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

    public MeasureTypeType getType() {
        return type;
    }

    public void setType(MeasureTypeType type) {
        this.type = type;
    }
}

