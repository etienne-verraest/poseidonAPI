package com.poseidon.app.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "curvepoint")
public class CurvePoint {

	public CurvePoint(Integer curveId, Double term, Double value) {
		this.curveId = curveId;
		this.term = term;
		this.value = value;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private Integer id;

	@Column(name = "CurveId")
	private Integer curveId;

	@Column
	private Timestamp asOfDate;

	@Column
	private Double term;

	@Column
	private Double value;

	@Column
	private Timestamp creationDate;

}
