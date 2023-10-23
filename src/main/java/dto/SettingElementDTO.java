package dto;

import java.util.List;

public class SettingElementDTO {
    private String id;
    private List<String> requires;
    public List<String> getRequires() {
        return requires;
    }
    public String getId() {
        return id;
    }
}
