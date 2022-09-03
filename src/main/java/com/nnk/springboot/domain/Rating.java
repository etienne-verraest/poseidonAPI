package com.nnk.springboot.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "rating")
public class Rating {

	public Rating(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
		this.moodysRating = moodysRating;
		this.sandPRating = sandPRating;
		this.fitchRating = fitchRating;
		this.orderNumber = orderNumber;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Id")
	private Integer id;

	@Column
	private String moodysRating;

	@Column
	private String sandPRating;

	@Column
	private String fitchRating;

	@Column
	private Integer orderNumber;
}
