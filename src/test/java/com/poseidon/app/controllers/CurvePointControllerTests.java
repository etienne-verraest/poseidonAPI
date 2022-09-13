package com.poseidon.app.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.poseidon.app.domain.CurvePoint;
import com.poseidon.app.domain.dto.CurvePointDto;
import com.poseidon.app.exceptions.CurvePointServiceException;
import com.poseidon.app.services.CurvePointService;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class CurvePointControllerTests {

	// **** Setting up IT fields ****
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webContext;

	@MockBean
	CurvePointService curvePointServiceMock;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}

	// **** Setting up IT values used for testing ****
	// I'll reuse values used in services tests
	static CurvePoint mockFirstCurvePoint;
	static List<CurvePoint> curvePointListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		curvePointListMock = new ArrayList<>();
		mockFirstCurvePoint = new CurvePoint(123, 15d, 15d);
		curvePointListMock.add(mockFirstCurvePoint);
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_CurvePoints_Successful() throws Exception {

		// ARRANGE
		when(curvePointServiceMock.findAllCurvePoint()).thenReturn(curvePointListMock);

		// ACT AND ASSERT
		mockMvc.perform(get("/curvePoint/list")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("curvePoints")); //
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_CreateCurvePoint_Successful() throws Exception {

		// ACT AND ASSERT
		mockMvc.perform(get("/curvePoint/add")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("curvePointDto")) //
				.andExpect(view().name("curvePoint/add"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void post_CreateCurvePoint_Successful() throws Exception {

		// ARRANGE
		CurvePoint curvePointEntity = new CurvePoint(1, 12d, 12d);
		when(curvePointServiceMock.convertDtoToEntity(any(CurvePointDto.class))).thenReturn(curvePointEntity);
		when(curvePointServiceMock.createCurvePoint(any(CurvePoint.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/curvePoint/validate") //
				.param("curveId", "1") //
				.param("term", "12.0") //
				.param("value", "12.0")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/curvePoint/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void post_UpdateCurvePoint_Successful() throws Exception {

		// ARRANGE
		CurvePoint curvePointEntity = new CurvePoint(1, 12d, 12d);
		when(curvePointServiceMock.convertDtoToEntity(any(CurvePointDto.class))).thenReturn(curvePointEntity);
		when(curvePointServiceMock.updateCurvePoint(anyInt(), any(CurvePoint.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/curvePoint/update/{id}", "1") //
				.param("curveId", "1") //
				.param("term", "12.0") //
				.param("value", "12.0")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/curvePoint/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_UpdateCurvePoint_Successful() throws Exception {

		// ARRANGE
		CurvePoint curvePointEntity = new CurvePoint(1, 12d, 12d);
		CurvePointDto curvePointDto = new CurvePointDto();
		curvePointDto.setCurveId("1");
		curvePointDto.setTerm("12.0");
		curvePointDto.setValue("12.0");

		when(curvePointServiceMock.findCurvePointById(1)).thenReturn(curvePointEntity);
		when(curvePointServiceMock.convertEntityToDto(any(CurvePoint.class))).thenReturn(curvePointDto);

		// ACT AND ASSERT
		mockMvc.perform(get("/curvePoint/update/{id}", "1")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("curvePointDto")) //
				.andExpect(view().name("curvePoint/update"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_UpdateCurvePoint_Error() throws Exception {

		// ARRANGE
		when(curvePointServiceMock.findCurvePointById(23)).thenThrow(CurvePointServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/curvePoint/update/{id}", "23")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/curvePoint/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_DeleteCurvePoint_Successful() throws Exception {

		// ARRANGE
		when(curvePointServiceMock.deleteCurvePoint(1)).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(get("/curvePoint/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/curvePoint/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_DeleteCurvePoint_Error() throws Exception {

		// ARRANGE
		when(curvePointServiceMock.deleteCurvePoint(1)).thenThrow(CurvePointServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/curvePoint/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(view().name("redirect:/curvePoint/list"));
	}

}
