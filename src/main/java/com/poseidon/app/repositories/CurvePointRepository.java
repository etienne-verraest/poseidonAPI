package com.poseidon.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidon.app.domain.CurvePoint;

@Repository
public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {

	Optional<CurvePoint> findCurvePointById(Integer id);
}
