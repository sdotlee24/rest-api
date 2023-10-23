package dto;

import java.util.List;

public class JukeBoxDTO {
    private String id;
    private String model;
    private List<ComponentDTO> components;

    public String getId() {
        return id;
    }


    public List<ComponentDTO> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentDTO> components) {
        this.components = components;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "JukeBoxDTO{" +
                "id='" + id + '\'' +
                ", model='" + model + '\'' +
                ", components=" + components +
                '}';
    }

}
