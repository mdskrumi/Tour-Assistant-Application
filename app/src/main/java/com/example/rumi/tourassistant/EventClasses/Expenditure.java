package com.example.rumi.tourassistant.EventClasses;

public class Expenditure {
    private String expenditureId;
    private String itemName , itemType ;
    private int quantity;
    private double value;

    public Expenditure() {
    }

    public Expenditure(String expenditureId, String itemName, String itemType, int quantity, double value) {
        this.expenditureId = expenditureId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.quantity = quantity;
        this.value = value;
    }

    public String getExpenditureId() {
        return expenditureId;
    }

    public void setExpenditureId(String expenditureId) {
        this.expenditureId = expenditureId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
