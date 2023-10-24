package com.example.Rest.API;

import dto.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

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
     * @return List of jukeboxes that fit the given query parameters.
     */
    public List<JukeBoxDTO> getJukeBox(String id, String model, int offset, int limit) {

        int endIndex = offset + limit;

        //Endpoint
        String url = dotenv.get("MOCK_ENDPOINT") + "jukes";
        List<String> requirements = getRequirements(id);
        if (requirements == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Could not resolve settingId");
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
                //Optional parameter parsing
                if (model != null && model.length() != 0) {
                    res2 = result.stream().filter(box -> box.getModel().equals(model.toLowerCase())).toList();
                } else {
                    res2 = result;
                }
                //input validation for "limit"
                if (endIndex > res2.size() || limit == 0) {
                    endIndex = res2.size();
                }
                return res2.subList(offset, endIndex);
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


    /**
     * For testing purposes, created a helper method for getRequirements function, that iterates through the list
     * of settings to find the item that matches "id".
     * @param settingDTO the Setting object which contains all the information about jukebox settings and their requirements.
     * @param id The settingId that is being queried.
     * @return A List of strings which represent the requirements of the particular setting. Null if not found.
     */
    public List<String> getSettings(SettingDTO settingDTO, String id) {
        List<SettingElementDTO> allSettings = settingDTO.getSettings();
        for (SettingElementDTO specificSetting : allSettings) {
            System.out.println("THIS is the settingID" + specificSetting.getId());
            if (specificSetting.getId().equals(id)) {
                return specificSetting.getRequires();
            }
        }
        return null;
    }

    /**
     * Returns all required components of the specific settingId.
     * @param id settingId.
     * @return An array of strings containing all the required components for a JukeBox to possess this feature.
     */
    public List<String> getRequirements(String id) {
        try {
            String url = dotenv.get("MOCK_ENDPOINT") + "settings";
            SettingDTO settingDTO = restTemplate.getForObject(url, SettingDTO.class);
            System.out.println(settingDTO);
            if (settingDTO != null) {
                List<String> setting = getSettings(settingDTO, id);
                if (setting == null) {
                    return null;
                }
                return setting;
            }
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Invalid settingId.");
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * <p>Indicates weather or not an instance of a Jukebox fits the list of requirements to possess the feature.
     * Method: Set of requirements MUST be a subset of the set containing the component of a given JukeBox.</p>
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
