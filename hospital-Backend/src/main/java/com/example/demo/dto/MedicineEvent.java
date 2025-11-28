package com.example.demo.dto;

public class MedicineEvent {
    private String name;
    private double price;
    private boolean isDiscontinued;
    private String manufacturerName;
    private String type;
    private String packSizeLabel;
    private String shortComposition1;
    private String shortComposition2;

    // ✅ Constructors
    public MedicineEvent() {
        super();
    }

    public MedicineEvent(String name, double price, boolean isDiscontinued, String manufacturerName,
                         String type, String packSizeLabel, String shortComposition1, String shortComposition2) {
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

    // ✅ Getters & Setters
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
    public void setDiscontinued(boolean discontinued) {
        isDiscontinued = discontinued;
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
