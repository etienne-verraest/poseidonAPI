package com.poseidon.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidon.app.domain.CurvePoint;


public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {

}
