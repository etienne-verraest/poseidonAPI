package com.poseidon.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.CurvePoint;
import com.poseidon.app.exceptions.CurvePointServiceException;
import com.poseidon.app.repositories.CurvePointRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurvePointService {

	@Autowired
	CurvePointRepository curvePointRepository;

	/**
	 * Get a list of every CurvePoint
	 * @return									List<CurvePoint> with existing CurvePoints
	 */
	public List<CurvePoint> findAllCurvePoint() {
		return curvePointRepository.findAll();
	}

	/**
	 * Find a CurvePoint by its ID
	 *
	 * @param id								The CurvePoint ID to find
	 * @return									CurvePoint if it exists, otherwise an error is thrown
	 * @throws CurvePointServiceException	 	Thrown if the CurvePoint was not found
	 */
	public CurvePoint findCurvePointById(Integer id) throws CurvePointServiceException {
		Optional<CurvePoint> curvePoint = curvePointRepository.findCurvePointById(id);
		if (id != null && curvePoint.isPresent()) {
			return curvePoint.get();
		}
		throw new CurvePointServiceException("CurvePoint was not found with given ID");
	}

	/**
	 * Create a CurvePoint
	 *
	 * @param curvePointEntity					The CurvePoint Entity to create
	 * @return									True if the creation was successful
	 * @throws CurvePointServiceException		Thrown if there was an error while creating the CurvePoint
	 *
	 */
	public boolean createCurvePoint(CurvePoint curvePointEntity) throws CurvePointServiceException {
		if (curvePointEntity != null
				&& !curvePointRepository.findCurvePointById(curvePointEntity.getId()).isPresent()) {
			curvePointRepository.save(curvePointEntity);
			log.info("[CURVEPOINT SERVICE] Created new Curve Point with id : '{}', term : '{}' and value : '{}'",
					curvePointEntity.getCurveId(), curvePointEntity.getTerm(), curvePointEntity.getValue());
			return true;
		}
		throw new CurvePointServiceException("There was an error while creating the Curve Point");
	}

	/**
	 * Update an existing CurvePoint
	 *
	 * @param id								The CurvePoint ID to update
	 * @param bidListEntityUpdated				The new fields given for update
	 * @return									True if the update was successful
	 * @throws CurvePointServiceException		Thrown if CurvePoint with given ID is not found
	 */
	public boolean updateCurvePoint(Integer id, CurvePoint curvePointEntityUpdated) throws CurvePointServiceException {
		Optional<CurvePoint> curvePoint = curvePointRepository.findCurvePointById(id);
		if (id != null && curvePoint.isPresent()) {
			curvePointEntityUpdated.setId(id);
			curvePointRepository.save(curvePointEntityUpdated);

			log.info("[CURVEPOINT SERVICE] Updated Curve Point '{}' with term '{}' and value '{}'",
					curvePointEntityUpdated.getCurveId(), curvePointEntityUpdated.getTerm(),
					curvePointEntityUpdated.getValue());
			return true;
		}
		throw new CurvePointServiceException("Could not find Curve point with id : " + id);
	}

	/**
	 * Delete a CurvePoint
	 *
	 * @param id								The CurvePoint ID to delete
	 * @return									True if the deletion was successful
	 * @throws CurvePointServiceException		Thrown if CurvePoint with given ID is not found
	 */
	public boolean deleteCurvePoint(Integer id) throws CurvePointServiceException {
		Optional<CurvePoint> curvePoint = curvePointRepository.findCurvePointById(id);
		if (id != null && curvePoint.isPresent()) {
			curvePointRepository.delete(curvePoint.get());
			log.info("[CURVEPOINT SERVICE] Deleted Curve Point with id '{}'", id);
			return true;
		}

		throw new CurvePointServiceException("Could not find bid list with id : " + id);
	}
}
