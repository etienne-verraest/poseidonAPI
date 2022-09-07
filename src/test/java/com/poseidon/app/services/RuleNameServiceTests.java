package com.poseidon.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.poseidon.app.domain.RuleName;
import com.poseidon.app.exceptions.RuleNameServiceException;
import com.poseidon.app.repositories.RuleNameRepository;

@RunWith(MockitoJUnitRunner.class)
public class RuleNameServiceTests {

	@InjectMocks
	RuleNameService ruleNameService;

	@Mock
	RuleNameRepository ruleNameRepositoryMock;

	static RuleName mockFirstRuleName;
	static List<RuleName> ruleNameListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mockFirstRuleName = new RuleName("Name", "Description", "Json", "Template", "SqlStr", "SqlPart");
		ruleNameListMock = new ArrayList<>();
		ruleNameListMock.add(mockFirstRuleName);
	}

	@Test
	public void testFindAllRuleName_ShouldReturn_List() {

		// ARRANGE
		when(ruleNameRepositoryMock.findAll()).thenReturn(ruleNameListMock);

		// ACT
		List<RuleName> response = ruleNameService.findAllRuleNames();

		// ASSERT
		assertThat(response.get(0).getName()).isEqualTo("Name");
		assertThat(response).hasSize(1);
	}

	@Test
	public void testFindRuleNameById_ShouldReturn_FirstRuleName() throws RuleNameServiceException {

		// ARRANGE
		when(ruleNameRepositoryMock.findRuleNameById(1)).thenReturn(Optional.of(mockFirstRuleName));

		// ACT
		RuleName response = ruleNameService.findRuleNameById(1);

		// ARRANGE
		assertThat(response.getName()).isEqualTo("Name");
		assertThat(response.getDescription()).isEqualTo("Description");
		assertThat(response.getJson()).isEqualTo("Json");
	}

	@Test
	public void testCreateRuleName_ShouldReturn_True() throws RuleNameServiceException {

		// ACT
		boolean response = ruleNameService.createRuleName(mockFirstRuleName);

		// ASSERT
		assertThat(response).isTrue();
		verify(ruleNameRepositoryMock, times(1)).save(mockFirstRuleName);
	}

	@Test
	public void testUpdateRuleName_ShouldReturn_True() throws RuleNameServiceException {

		// ARRANGE
		when(ruleNameRepositoryMock.findRuleNameById(1)).thenReturn(Optional.of(mockFirstRuleName));
		mockFirstRuleName.setTemplate("Template 2");

		// ACT
		boolean response = ruleNameService.updateRuleName(1, mockFirstRuleName);

		assertThat(response).isTrue();
		verify(ruleNameRepositoryMock, times(1)).save(mockFirstRuleName);

	}

	@Test
	public void testDeleteRuleName_ShouldReturn_True() throws RuleNameServiceException {

		// ARRANGE
		when(ruleNameRepositoryMock.findRuleNameById(1)).thenReturn(Optional.of(mockFirstRuleName));

		// ACT
		boolean response = ruleNameService.deleteRuleName(1);

		assertThat(response).isTrue();
		verify(ruleNameRepositoryMock, times(1)).delete(mockFirstRuleName);
	}

}
