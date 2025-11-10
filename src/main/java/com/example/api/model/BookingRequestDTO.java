package com.example.api.model;



public class BookingRequestDTO {

    private Long busId;
    private Long seatId;
    private String boardingPoint;
    private String droppingPoint;

    private String name;
    private int age;
    private String phone;
    private String state;

    public BookingRequestDTO() {}

    public BookingRequestDTO(Long busId, Long seatId, String boardingPoint, String droppingPoint,
                             String name, int age, String phone, String state) {
        this.busId = busId;
        this.seatId = seatId;
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

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

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
        return "BookingRequestDTO [busId=" + busId + ", seatId=" + seatId + ", boardingPoint=" + boardingPoint +
               ", droppingPoint=" + droppingPoint + ", name=" + name + ", age=" + age +
               ", phone=" + phone + ", state=" + state + "]";
    }
}
