package pt.ipleiria.estg.dei.ei.dae.project.ProjetoDae.dtos;

public class MeasurementDTO {

    private int id;
    private int measureTypeId;
    private String measureTypeName;
    private String value;
    private String inputSource;
    private String username;

    public MeasurementDTO() {
    }

    public MeasurementDTO(int id, int measureTypeId, String measureTypeName, String value, String inputSource, String username) {
        this.id = id;
        this.measureTypeId = measureTypeId;
        this.value = value;
        this.inputSource = inputSource;
        this.username = username;
        this.measureTypeName = measureTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeasureTypeId() {
        return measureTypeId;
    }

    public void setMeasureTypeId(int measureTypeId) {
        this.measureTypeId = measureTypeId;
    }

    public String getMeasureTypeName() {
        return measureTypeName;
    }

    public void setMeasureTypeName(String measureTypeName) {
        this.measureTypeName = measureTypeName;
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

    public String toString() {
        return "ID - "+getId();
    }
}
