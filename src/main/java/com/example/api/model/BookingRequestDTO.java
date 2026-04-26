package com.example.api.model;

public class BookingRequestDTO {

    private Long busId;
    private String seatCode;   // passenger’s chosen seat code like "A1"
    private String boardingPoint;
    private String droppingPoint;

    private String name;
    private int age;
    private String phone;
    private String state;

    public BookingRequestDTO() {}

    public BookingRequestDTO(Long busId, String seatCode, String boardingPoint, String droppingPoint,
                             String name, int age, String phone, String state) {
        this.busId = busId;
        this.seatCode = seatCode;
        this.boardingPoint = boardingPoint;
        this.droppingPoint = droppingPoint;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.state = state;
    }

    // Getters and setters
    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }

    public String getSeatCode() { return seatCode; }
    public void setSeatCode(String seatCode) { this.seatCode = seatCode; }

    public String getBoardingPoint() { return boardingPoint; }
    public void setBoardingPoint(String boardingPoint) { this.boardingPoint = boardingPoint; }

    public String getDroppingPoint() { return droppingPoint; }
    public void setDroppingPoint(String droppingPoint) { this.droppingPoint = droppingPoint; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    @Override
    public String toString() {
        return "BookingRequestDTO [busId=" + busId + ", seatCode=" + seatCode +
               ", boardingPoint=" + boardingPoint + ", droppingPoint=" + droppingPoint +
               ", name=" + name + ", age=" + age + ", phone=" + phone + ", state=" + state + "]";
    }
}
