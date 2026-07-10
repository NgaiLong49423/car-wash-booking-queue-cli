package model;

public class WashPackage {
    private String id;
    private String name;
    private double price;
    private int duration;
    private String status;

    public WashPackage(String id, String name, double price, int duration, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return id + "|" + name + "|" + price + "|" + duration + "|" + status;
    }
}