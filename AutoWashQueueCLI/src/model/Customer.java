package model;

public class Customer {
    private String id;
    private String name;
    private String phone;
    private String membershipLevel;
    private int points;
    private double totalSpent;
    private int visitCount;

    public Customer(String id, String name, String phone, String membershipLevel, int points, double totalSpent, int visitCount) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.membershipLevel = membershipLevel;
        this.points = points;
        this.totalSpent = totalSpent;
        this.visitCount = visitCount;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMembershipLevel() { return membershipLevel; }
    public void setMembershipLevel(String membershipLevel) { this.membershipLevel = membershipLevel; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }

    public int getVisitCount() { return visitCount; }
    public void setVisitCount(int visitCount) { this.visitCount = visitCount; }

    @Override
    public String toString() {
        return id + "|" + name + "|" + phone + "|" + membershipLevel + "|" + points + "|" + totalSpent + "|" + visitCount;
    }
}