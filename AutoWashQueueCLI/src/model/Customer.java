package model;

public class Customer {
    private String id;
    private String name;
    private String phone;
    private String membershipLevel;
    private int points;

    public Customer(String id, String name, String phone, String membershipLevel, int points) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.membershipLevel = membershipLevel;
        this.points = points;
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

    @Override
    public String toString() {
        return "Khach Hang [" + id + " | " + name + " | SĐT: " + phone + " | Hang: " + membershipLevel + " | Diem: " + points + "]";
    }
}