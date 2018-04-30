package com.test.perf.entity;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 *
 */
public class City implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String countryCode;
	private String district;
	private Integer population;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public Integer getPopulation() {
		return population;
	}
	public void setPopulation(Integer population) {
		this.population = population;
	}
	
	
}
