package com.example.quan_li_tro;

public class Room {
    private String id;
    private String name;
    private double price;
    private boolean isOccupied;
    private String tenantName;
    private String phoneNumber;

    public Room(String id, String name, double price, boolean isOccupied, String tenantName, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isOccupied = isOccupied;
        this.tenantName = tenantName;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
