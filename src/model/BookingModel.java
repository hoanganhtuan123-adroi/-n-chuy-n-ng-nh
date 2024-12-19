package model;

import java.util.Date;

public class BookingModel {
    private String bookingID;
    private String customerID;
    private int packageID;
    private String bookingDate;
    private int numberOfPeople;
    private Double totalPrice;
    private String status;
    private String paymentStatus;
    private String paymentDate;
    private String requests;
    private String customerName;
    private String tourName;
    private String packageName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    private int tourID;
    private String tourPrice;
    private String deposit;

    public  BookingModel(){}

    public BookingModel(String bookingId, String cusName, String tourName, String bookingDate, String totalPrice, String status) {
        this.bookingID = bookingId;
        this.customerName = cusName;
        this.tourName = tourName;
        this.bookingDate = bookingDate;
        this.totalPrice = Double.parseDouble(totalPrice);
        this.status = status;
    }

    public BookingModel(String bookingId, String cusName, String tourName, String packageName, String bookingDate, String numberPeople, String totalPrice, String status, String paymentStatus, String paymentDate, String specialRequests) {
        this.bookingID = bookingId;
        this.customerName = cusName;
        this.tourName = tourName;
        this.packageName = packageName;
        this.bookingDate = bookingDate;
        this.numberOfPeople = Integer.parseInt(numberPeople);
        this.totalPrice = Double.parseDouble(totalPrice);
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.requests = specialRequests;
    }

    public BookingModel(String id, String cusName, String tourName, String bookingDate, int member, String status, String payStatus, String payDate, String request) {
        this.bookingID = id;
        this.customerName = cusName;
        this.tourName = tourName;
        this.bookingDate = bookingDate;
        this.numberOfPeople = member;
        this.status = status;
        this.paymentStatus = payStatus;
        this.paymentDate = payDate;
        this.requests = request;
    }

    public BookingModel(String customerName, int totalMember, String tourPrice, String status, String deposit,
                        String email, String phone, String address,
                        String specialRequest, String bookingDate, String paymentStaus) {
        this.customerName = customerName;
        this.numberOfPeople = totalMember;
        this.status = status;
        this.deposit = deposit;
        this.customerEmail = email;
        this.customerPhone = phone;
        this.customerAddress = address;
        this.requests = specialRequest;
        this.bookingDate = bookingDate;
        this.tourPrice = tourPrice;
        this.paymentStatus = paymentStaus;

    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getTourPrice() {
        return tourPrice;
    }

    public void setTourPrice(String tourPrice) {
        this.tourPrice = tourPrice;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int getTourID() {
        return tourID;
    }

    public void setTourID(int tourID) {
        this.tourID = tourID;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getRequests() {
        return requests;
    }

    public void setRequests(String requests) {
        this.requests = requests;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }
}
