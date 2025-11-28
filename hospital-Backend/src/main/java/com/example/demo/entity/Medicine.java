package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name="medicines")
public class Medicine {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int medicineId;

	    private String name;

	    private double price;   // ₹ price

	    @Column(name = "is_discontinued")
	    private boolean isDiscontinued;

	    @Column(name = "manufacturer_name")
	    private String manufacturerName;

	    private String type;   // e.g., Tablet, Syrup, Capsule

	    @Column(name = "pack_size_label")
	    private String packSizeLabel; // e.g., "10 tablets", "100 ml bottle"

	    @Column(name = "short_composition1")
	    private String shortComposition1;

	    @Column(name = "short_composition2")
	    private String shortComposition2;
	   
		public Medicine(String name, double price, boolean isDiscontinued, String manufacturerName, String type,
				String packSizeLabel, String shortComposition1, String shortComposition2) {
			super();
			this.name = name;
			this.price = price;
			this.isDiscontinued = isDiscontinued;
			this.manufacturerName = manufacturerName;
			this.type = type;
			this.packSizeLabel = packSizeLabel;
			this.shortComposition1 = shortComposition1;
			this.shortComposition2 = shortComposition2;

		}

		public Medicine() {
			super();
		}

		public int getMedicineId() {
			return medicineId;
		}

		public void setMedicineId(int medicineId) {
			this.medicineId = medicineId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public boolean isDiscontinued() {
			return isDiscontinued;
		}

		public void setDiscontinued(boolean isDiscontinued) {
			this.isDiscontinued = isDiscontinued;
		}

		public String getManufacturerName() {
			return manufacturerName;
		}

		public void setManufacturerName(String manufacturerName) {
			this.manufacturerName = manufacturerName;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getPackSizeLabel() {
			return packSizeLabel;
		}

		public void setPackSizeLabel(String packSizeLabel) {
			this.packSizeLabel = packSizeLabel;
		}

		public String getShortComposition1() {
			return shortComposition1;
		}

		public void setShortComposition1(String shortComposition1) {
			this.shortComposition1 = shortComposition1;
		}

		public String getShortComposition2() {
			return shortComposition2;
		}

		public void setShortComposition2(String shortComposition2) {
			this.shortComposition2 = shortComposition2;
		}

		
		
}
