package model;

import java.sql.Date;

public class BookingModel {
    private int bookingID;
    private int customerID;
    private int packageID;
    private Date bookingDate;
    private int numberOfPeople;
    private Double totalPrice;
    private String status;
    private String paymentStatus;
    private Date paymentDate;
    private String requests;
    private String customerName;
    private String tourName;
    private String packageName;

    public BookingModel(int bookingID, int customerID, int packageID, Date bookingDate, int numberOfPeople, Double totalPrice, String status, String paymentStatus, Date paymentDate, String customerName, String requests, String tourName) {
        this.bookingID = bookingID;
        this.customerID = customerID;
        this.packageID = packageID;
        this.bookingDate = bookingDate;
        this.numberOfPeople = numberOfPeople;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.customerName = customerName;
        this.requests = requests;
        this.tourName = tourName;
    }

    public BookingModel(String bookingId, String cusName, String tourName, String bookingDate, String totalPrice, String status) {
        this.bookingID = Integer.parseInt(bookingId);
        this.customerName = cusName;
        this.tourName = tourName;
        this.bookingDate = Date.valueOf(bookingDate);
        this.totalPrice = Double.parseDouble(totalPrice);
        this.status = status;
    }

    public BookingModel(String bookingId, String cusName, String tourName, String packageName, String bookingDate, String numberPeople, String totalPrice, String status, String paymentStatus, String paymentDate, String specialRequests) {
        this.bookingID = Integer.parseInt(bookingId);
        this.customerName = cusName;
        this.tourName = tourName;
        this.packageName = packageName;
        this.bookingDate = Date.valueOf(bookingDate);
        this.numberOfPeople = Integer.parseInt(numberPeople);
        this.totalPrice = Double.parseDouble(totalPrice);
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.paymentDate = Date.valueOf(paymentDate);
        this.requests = specialRequests;
    }

    public BookingModel(int id, String cusName, String tourName, String bookingDate, int member, String status, String payStatus, String payDate, String request) {
        this.bookingID = id;
        this.customerName = cusName;
        this.tourName = tourName;
        this.bookingDate = Date.valueOf(bookingDate);
        this.numberOfPeople = member;
        this.status = status;
        this.paymentStatus = payStatus;
        this.paymentDate = Date.valueOf(payDate);
        this.requests = request;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
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

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
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

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
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
