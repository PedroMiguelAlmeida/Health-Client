package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos;

import io.smallrye.common.constraint.NotNull;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.MeasureType;
import pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.entities.User;

import javax.persistence.ManyToOne;

public class MeasurementDTO {

    private int measureTypeId;
    private String value;
    private String inputSource;
    private String username;

    public MeasurementDTO() {
    }

    public MeasurementDTO(int measureTypeId, String value, String inputSource, String username) {
        this.measureTypeId = measureTypeId;
        this.value = value;
        this.inputSource = inputSource;
        this.username = username;
    }

    public int getMeasureTypeId() {
        return measureTypeId;
    }

    public void setMeasureTypeId(int measureTypeId) {
        this.measureTypeId = measureTypeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInputSource() {
        return inputSource;
    }

    public void setInputSource(String inputSource) {
        this.inputSource = inputSource;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
