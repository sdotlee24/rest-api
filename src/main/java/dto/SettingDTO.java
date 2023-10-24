package dto;

import java.util.List;

public class SettingDTO {



    private List<SettingElementDTO> settings;
    public void setSettings(List<SettingElementDTO> settings) {
        this.settings = settings;
    }

    public List<SettingElementDTO> getSettings() {
        return settings;
    }
}
