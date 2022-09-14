package com.poseidon.app.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curvepoint")
@NoArgsConstructor
@Data
public class CurvePoint {

	public CurvePoint(Integer curveId, Double term, Double value) {
		this.curveId = curveId;
		this.term = term;
		this.value = value;
	}

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "curve_id")
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
