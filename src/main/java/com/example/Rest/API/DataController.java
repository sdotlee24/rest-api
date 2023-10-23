package com.example.Rest.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Serves as the controller for the REST API.
 * @author lsy
 */
@RestController
@RequestMapping("/api")
public class DataController {
    private final DataService dataService;

    /**
     * Initializes a DataController with a DataService instance
     * @param dataService The DataService instance that controller will use for third-party api querying
     */
    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    /**
     * This method is called when user fetches the endpoint /test
     * @param settingId The query parameter used to filter through Jukeboxes.
     * @param model (Optional) filter by jukebox model.
     * @param offset (Optional) Starting index of the page.
     * @param limit (Optional) Size of the page.
     * @return String containing JukeBoxes that fit the given requirements.
     */
    @GetMapping("/jukes")
    public String getJukes(@RequestParam String settingId,
                           @RequestParam(required = false) String model,
                           @RequestParam(defaultValue = "0") int offset,
                           @RequestParam(defaultValue = "0") int limit) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must be greater than or equal to  0");
        }
        if (limit < 0) {
            throw new IllegalArgumentException("Limit must be greater than or equal to  0");
        }

        return dataService.getJukeBox(settingId, model, offset, limit);
    }
}
