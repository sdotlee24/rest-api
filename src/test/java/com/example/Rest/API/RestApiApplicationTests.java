package com.example.Rest.API;

import dto.ComponentDTO;
import dto.JukeBoxDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class RestApiApplicationTests {
	@InjectMocks
	private DataService dataService;

	@Mock
	private RestTemplate restTemplate;

	/**
	 * Tests if a given JukeBox object fits the requirements of a "setting". expected output: false -> false -> pass
	 */
	@Test
	void testFitsRequirements() {
		//given
		List<String> requirements = new ArrayList<>();
		requirements.add("speaker");
		requirements.add("touchscreen");

		ComponentDTO comp1 = new ComponentDTO("speaker");
		ComponentDTO comp2 = new ComponentDTO("touchscreen");
		List<ComponentDTO> case1 = new ArrayList<>();
		List<ComponentDTO> case2 = new ArrayList<>();
		case1.add(comp1);
		case2.add(comp1);
		case2.add(comp2);
		JukeBoxDTO box = new JukeBoxDTO();

		//when
		boolean result = dataService.fitsRequirement(requirements, box);

		//then

		assertFalse(result);

		//set 1 compoenent
		box.setComponents(case1);
		boolean result2 = dataService.fitsRequirement(requirements, box);

		//then
		assertFalse(result2);


		box.setComponents(case2);
		System.out.println(box.getComponents());
		boolean result3 = dataService.fitsRequirement(requirements, box);
		assertTrue(result3);
	}
	@Test
	void testJukeBox() {
		//given

		//when

		//then

	}

}
