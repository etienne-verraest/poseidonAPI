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

import com.poseidon.app.domain.dto.RuleDto;
import com.poseidon.app.exceptions.RuleServiceException;
import com.poseidon.app.services.RuleService;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class RuleControllerTests {

	// **** Setting up IT fields ****
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webContext;

	@MockBean
	RuleService ruleServiceMock;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}

	/**
	 * @Rule from Mockito is overriding the model, given that
	 * we have to call the entire path for each call on Rule Model.
	 */
	static com.poseidon.app.domain.Rule mockFirstRuleName;
	static List<com.poseidon.app.domain.Rule> ruleNameListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mockFirstRuleName = new com.poseidon.app.domain.Rule("Name", "Description", "Json", "Template", "SqlStr",
				"SqlPart");
		ruleNameListMock = new ArrayList<>();
		ruleNameListMock.add(mockFirstRuleName);
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_Rules_Successful() throws Exception {

		// ARRANGE
		when(ruleServiceMock.findAllRules()).thenReturn(ruleNameListMock);

		// ACT AND ASSERT
		mockMvc.perform(get("/ruleName/list")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("rules")); //
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_CreateRule_Successful() throws Exception {

		// ACT AND ASSERT
		mockMvc.perform(get("/ruleName/add")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("ruleDto")) //
				.andExpect(view().name("ruleName/add"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void post_CreateRule_Successful() throws Exception {

		// ARRANGE
		com.poseidon.app.domain.Rule ruleEntity = new com.poseidon.app.domain.Rule("Name", "Description", "Json",
				"Template", "SqlStr", "SqlPart");
		when(ruleServiceMock.convertDtoToEntity(any(RuleDto.class))).thenReturn(ruleEntity);
		when(ruleServiceMock.createRule(any(com.poseidon.app.domain.Rule.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/ruleName/validate") //
				.param("name", "Name") //
				.param("description", "Description") //
				.param("json", "Json") //
				.param("template", "Template") //
				.param("sqlStr", "SqlStr") //
				.param("sqlPart", "SqlPart")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/ruleName/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void post_UpdateRule_Successful() throws Exception {

		// ARRANGE
		com.poseidon.app.domain.Rule ruleEntity = new com.poseidon.app.domain.Rule("Name", "Description", "Json",
				"Template", "SqlStr", "SqlPart");
		when(ruleServiceMock.convertDtoToEntity(any(RuleDto.class))).thenReturn(ruleEntity);
		when(ruleServiceMock.updateRule(anyInt(), any(com.poseidon.app.domain.Rule.class))).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(post("/ruleName/update/{id}", "1") //
				.param("name", "Name") //
				.param("description", "Description") //
				.param("json", "Json") //
				.param("template", "Template") //
				.param("sqlStr", "SqlStr") //
				.param("sqlPart", "SqlPart")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/ruleName/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_UpdateRule_Successful() throws Exception {

		// ARRANGE
		com.poseidon.app.domain.Rule ruleEntity = new com.poseidon.app.domain.Rule("Name", "Description", "Json",
				"Template", "SqlStr", "SqlPart");
		RuleDto ruleDto = new RuleDto();
		ruleDto.setName("Name");
		ruleDto.setDescription("Description");
		ruleDto.setJson("Json");
		ruleDto.setTemplate("Template");
		ruleDto.setSqlStr("SqlStr");
		ruleDto.setSqlPart("SqlPart");

		when(ruleServiceMock.findRuleById(1)).thenReturn(ruleEntity);
		when(ruleServiceMock.convertEntityToDto(any(com.poseidon.app.domain.Rule.class))).thenReturn(ruleDto);

		// ACT AND ASSERT
		mockMvc.perform(get("/ruleName/update/{id}", "1")) //
				.andExpect(status().is2xxSuccessful()) //
				.andExpect(model().attributeExists("ruleDto")) //
				.andExpect(view().name("ruleName/update"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_UpdateRule_Error() throws Exception {

		// ARRANGE
		when(ruleServiceMock.findRuleById(23)).thenThrow(RuleServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/ruleName/update/{id}", "23")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/ruleName/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_DeleteRule_Successful() throws Exception {

		// ARRANGE
		when(ruleServiceMock.deleteRule(1)).thenReturn(true);

		// ACT AND ASSERT
		mockMvc.perform(get("/ruleName/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(flash().attributeExists("message")) //
				.andExpect(flash().attributeExists("message_type")) //
				.andExpect(view().name("redirect:/ruleName/list"));
	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void get_DeleteRule_Error() throws Exception {

		// ARRANGE
		when(ruleServiceMock.deleteRule(1)).thenThrow(RuleServiceException.class);

		// ACT AND ASSERT
		mockMvc.perform(get("/ruleName/delete/{id}", "1")) //
				.andExpect(status().is3xxRedirection()) //
				.andExpect(view().name("redirect:/ruleName/list"));
	}

}
