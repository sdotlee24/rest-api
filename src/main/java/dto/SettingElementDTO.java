package dto;

import java.util.List;

public class SettingElementDTO {
    private String id;
    private List<String> requires;


    public void setRequires(List<String> requires) {
        this.requires = requires;
    }

    public void setId(String id) {
        this.id = id;
    }
    public List<String> getRequires() {
        return requires;
    }

    public String getId() {
        return id;
    }
}
