package com.example.Rest.API;

import dto.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Service layer for data filtering for REST API.
 * @author lsy
 */
@Service
public class DataService {
    private final RestTemplate restTemplate;
    private final Dotenv dotenv = Dotenv.configure().load();

    /**
     * Dependency injection of an instance of RestTemplate.
     * @param restTemplate used to faciliate HTTP requests, provided by Spring Boot.
     */
    @Autowired
    public DataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Gets all JukeBox objects which match the filtering parameters.
     * @param id The settingId which is linked to its requirements.
     * @param model Model name of Jukebox object.
     * @param offset The starting index of the returning list of Jukeboxes.
     * @param limit The size of the jukebox list.
     * @return
     */
    public String getJukeBox(String id, String model, int offset, int limit) {

        int endIndex = offset + limit;

        //Endpoint
        String url = dotenv.get("MOCK_ENDPOINT") + "jukes";
        List<String> requirements = getRequirements(id);
        if (requirements == null) {
            return "Was not able to find setting with id: " + id;
        }
        List<JukeBoxDTO> result = new ArrayList<>();
        List<JukeBoxDTO> res2;
        try {
//            Fetch api
            System.out.println(url);
            ResponseEntity<List<JukeBoxDTO>> responseEntity = restTemplate.exchange(url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<JukeBoxDTO>>() {});
            System.out.println("passed 1");
            List<JukeBoxDTO> response = responseEntity.getBody();

            System.out.println("1 passed");
            if (response != null) {
                System.out.println("2 passed");
                for (JukeBoxDTO boxDTO : response) {

                    if (fitsRequirement(requirements, boxDTO)) {

                        result.add(boxDTO);
                    }
                }
                System.out.println("4 passed");
                System.out.println(result);
                //Optional parameter parsing
                if (model != null && model.length() != 0) {
                    res2 = result.stream().filter(box -> box.getModel().equals(model.toLowerCase())).toList();
                } else {
                    res2 = result;
                }
                System.out.println(res2);

                //input validation for "limit"
                if (endIndex > res2.size() || limit == 0) {
                    endIndex = res2.size();
                }
                return res2.subList(offset, endIndex).toString();
            } else {
                return "Could not connect to third party api";
            }

        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

    /**
     * Returns all required components of the specific settingId.
     * @param id settingId.
     * @return An string of array containing all the required components for a JukeBox to possess this feature.
     */
    public List<String> getRequirements(String id) {
        try {
            String url = dotenv.get("MOCK_ENDPOINT") + "settings";
            SettingDTO settingDTO = restTemplate.getForObject(url, SettingDTO.class);
            if (settingDTO != null) {
                List<SettingElementDTO> allSettings = settingDTO.getSettings();
                for (SettingElementDTO specificSetting : allSettings) {
                    if (specificSetting.getId().equals(id)) {
                        return specificSetting.getRequires();
                    }
                }
            }
            return null;
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Indicates weather or not an instance of a Jukebox fits the list of requirements to posess the feature.
     * @param requirements The list of requirements that a Jukebox must have.
     * @param box An instance of Jukebox.
     * @return A boolean indicating if the Jukebox fits the requirement or not.
     */
    public boolean fitsRequirement(List<String> requirements, JukeBoxDTO box) {
        HashSet<String> componentSet = new HashSet<>();
        List<ComponentDTO> components = box.getComponents();
        if (components != null) {
            for (ComponentDTO componentDTO : components) {
                componentSet.add(componentDTO.getName());
            }
            for (String requirement : requirements) {
                if (!componentSet.contains(requirement)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }
}