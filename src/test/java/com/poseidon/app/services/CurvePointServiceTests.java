package com.poseidon.app.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.poseidon.app.domain.CurvePoint;
import com.poseidon.app.exceptions.CurvePointServiceException;
import com.poseidon.app.repositories.CurvePointRepository;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CurvePointServiceTests {

	@InjectMocks
	CurvePointService curvePointService;

	@Mock
	CurvePointRepository curvePointRepositoryMock;

	static CurvePoint mockFirstCurvePoint;
	static CurvePoint mockSecondCurvePoint;
	static List<CurvePoint> curvePointListMock;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		curvePointListMock = new ArrayList<>();
		mockFirstCurvePoint = new CurvePoint(123, 15d, 15d);
		mockSecondCurvePoint = new CurvePoint(456, 20d, 20d);
		curvePointListMock.add(mockFirstCurvePoint);
		curvePointListMock.add(mockSecondCurvePoint);
	}

	@Test
	public void test1_FindAllCurvePoint_ShouldReturn_List() {

		// ARRANGE
		when(curvePointRepositoryMock.findAll()).thenReturn(curvePointListMock);

		// ACT
		List<CurvePoint> response = curvePointService.findAllCurvePoint();

		// ASSERT
		assertThat(response.get(0).getCurveId()).isEqualTo(123);
		assertThat(response.get(1).getCurveId()).isEqualTo(456);
		assertThat(response).hasSize(2);
	}

	@Test
	public void test1_FindCurvePointById_ShouldReturn_FirstCurvePoint() throws CurvePointServiceException {

		// ARRANGE
		when(curvePointRepositoryMock.findCurvePointById(1)).thenReturn(Optional.of(mockFirstCurvePoint));

		// ACT
		CurvePoint response = curvePointService.findCurvePointById(1);

		// ARRANGE
		assertThat(response.getValue()).isEqualTo(15d);
		assertThat(response.getTerm()).isEqualTo(15d);
	}

	@Test
	public void testCreateCurvePoint_ShouldReturn_True() throws CurvePointServiceException {

		// ACT
		boolean response = curvePointService.createCurvePoint(mockFirstCurvePoint);

		// ASSERT
		assertThat(response).isTrue();
		verify(curvePointRepositoryMock, times(1)).save(mockFirstCurvePoint);

	}

	@Test
	public void testUpdateCurvePoint_ShouldReturn_True() throws CurvePointServiceException {

		// ARRANGE
		when(curvePointRepositoryMock.findCurvePointById(1)).thenReturn(Optional.of(mockFirstCurvePoint));
		mockFirstCurvePoint.setValue(30d);

		// ACT
		boolean response = curvePointService.updateCurvePoint(1, mockFirstCurvePoint);

		// ASSERT
		assertThat(response).isTrue();
		verify(curvePointRepositoryMock, times(1)).save(mockFirstCurvePoint);
	}

	@Test
	public void testDeleteCurvePoint_ShouldReturn_True() throws CurvePointServiceException {

		// ARRANGE
		when(curvePointRepositoryMock.findCurvePointById(2)).thenReturn(Optional.of(mockSecondCurvePoint));

		// ACT
		boolean response = curvePointService.deleteCurvePoint(2);

		// ASSERT
		assertThat(response).isTrue();
		verify(curvePointRepositoryMock, times(1)).delete(mockSecondCurvePoint);
	}

}
