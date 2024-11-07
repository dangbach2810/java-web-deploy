package com.laptrinhjavaweb.builder;

import jakarta.persistence.Column;
import java.util.ArrayList;
import java.util.List;

public class BuildingSearchBuilder {
	@Column(name = "name")
	private String name;
	@Column(name = "floorarea")
	private Integer floorArea;
	@Column(name = "district")
	private String district;
	@Column(name = "ward")
	private String ward;
	@Column(name = "street")
	private String street;
	@Column(name = "numberofbasement")
	private Integer numberOfBasement;
	@Column(name = "direction")
	private String direction;
	@Column(name = "level")
	private Integer level;
	@Column(name = "value")
	private Integer rentAreaFrom;
	@Column(name = "value")
	private Integer rentAreaTo;
	@Column(name = "rentprice")
	private Integer rentPriceFrom;
	@Column(name = "rentprice")
	private Integer rentPriceTo;
	@Column(name = "managername")
	private String managerName;
	@Column(name = "managerphone")
	private String managerPhone;
	@Column(name = "staffid")
	private Long staffId;
	@Column(name = "type")
	private List<String> buildingTypes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFloorArea() {
		return floorArea;
	}

	public void setFloorArea(Integer floorArea) {
		this.floorArea = floorArea;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getNumberOfBasement() {
		return numberOfBasement;
	}

	public void setNumberOfBasement(Integer numberOfBasement) {
		this.numberOfBasement = numberOfBasement;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getRentAreaFrom() {
		return rentAreaFrom;
	}

	public void setRentAreaFrom(Integer rentAreaFrom) {
		this.rentAreaFrom = rentAreaFrom;
	}

	public Integer getRentAreaTo() {
		return rentAreaTo;
	}

	public void setRentAreaTo(Integer rentAreaTo) {
		this.rentAreaTo = rentAreaTo;
	}

	public Integer getRentPriceFrom() {
		return rentPriceFrom;
	}

	public void setRentPriceFrom(Integer rentPriceFrom) {
		this.rentPriceFrom = rentPriceFrom;
	}

	public Integer getRentPriceTo() {
		return rentPriceTo;
	}

	public void setRentPriceTo(Integer rentPriceTo) {
		this.rentPriceTo = rentPriceTo;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getManagerPhone() {
		return managerPhone;
	}

	public void setManagerPhone(String managerPhone) {
		this.managerPhone = managerPhone;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public List<String> getBuildingTypes() {
		return buildingTypes;
	}

	public void setBuildingTypes(List<String> buildingTypes) {
		this.buildingTypes = buildingTypes;
	}

	private BuildingSearchBuilder(Builder builder) {
		this.name = builder.name;
		this.district = builder.district;
		this.floorArea = builder.floorArea;
		this.numberOfBasement = builder.numberOfBasement;
		this.street = builder.street;
		this.ward = builder.ward;
		this.direction = builder.direction;
		this.level = builder.level;
		this.buildingTypes = builder.buildingTypes;
		this.rentAreaFrom = builder.rentAreaFrom;
		this.rentAreaTo = builder.rentAreaTo;
		this.rentPriceFrom = builder.rentPriceFrom;
		this.rentPriceTo = builder.rentPriceTo;
		this.staffId = builder.staffId;
		this.managerName = builder.managerName;
		this.managerPhone = builder.managerPhone;
	}
	
	public static class Builder {

		private String name;
		private String district;
		private String street;
		private String ward;
		private Integer floorArea;
		private Integer numberOfBasement;
		private List<String> buildingTypes;
		private Integer rentAreaFrom;
		private Integer rentAreaTo;
		private Integer rentPriceFrom;
		private Integer rentPriceTo;
		private Long staffId;
		private String managerPhone;
		private String managerName;
		private String direction;
		private Integer level;
		
		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setLevel(Integer level) {
			this.level = level;
			return this;
		}

		public Builder setDirection(String direction) {
			this.direction = direction;
			return this;
		}
		public Builder setDistrict(String district) {
			this.district = district;
			return this;
		}

		public Builder setManagerName(String managerName) {
			this.managerName = managerName;
			return this;
		}

		public Builder setManagerPhone(String managerPhone) {
			this.managerPhone = managerPhone;
			return this;
		}

		public Builder setFloorArea(Integer floorArea) {
			this.floorArea = floorArea;
			return this;
		}

		public Builder setNumberOfBasement(Integer numberOfBasement) {
			this.numberOfBasement = numberOfBasement;
			return this;
		}

		public Builder setStreet(String street) {
			this.street = street;
			return this;
		}

		public Builder setWard(String ward) {
			this.ward = ward;
			return this;
		}

		public Builder setBuildingTypes(List<String> buildingTypes) {
			this.buildingTypes = buildingTypes;
			return this;
		}

		public Builder setRentPriceFrom(Integer rentPriceFrom) {
			this.rentPriceFrom = rentPriceFrom;
			return this;
		}

		public Builder setRentPriceTo(Integer rentPriceTo) {
			this.rentPriceTo = rentPriceTo;
			return this;
		}

		public Builder setRentAreaFrom(Integer rentAreaFrom) {
			this.rentAreaFrom = rentAreaFrom;
			return this;
		}

		public Builder setRentAreaTo(Integer rentAreaTo) {
			this.rentAreaTo = rentAreaTo;
			return this;
		}

		public Builder setStaffId(Long staffId) {
			this.staffId = staffId;
			return this;
		}
		public BuildingSearchBuilder build() {
			return new BuildingSearchBuilder(this);
		}
	}
}
