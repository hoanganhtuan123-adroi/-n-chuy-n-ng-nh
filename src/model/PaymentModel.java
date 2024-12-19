package model;

import java.sql.Date;

public class PaymentModel {
    private String paymentID;
    private String customerID;
    private String bookingID;
    private Date paymentDate;
    private String amount;
    private String paymentMethod;
    private String transactionID;
    private String paymentStatus;
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerEmail;
    private String tourName;
    private int tourId;

    public PaymentModel( String tourName,String customerEmail, String customerPhone, String customerAddress, String customerName, String paymentStatus, String paymentMethod, String amount, Date paymentDate) {
        this.tourName = tourName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        this.customerName = customerName;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }


    public PaymentModel(String paymentId, String customerName, String tourName, Date paymentDate, String status) {
        this.paymentID = paymentId;
        this.customerName = customerName;
        this.tourName = tourName;
        this.paymentDate = paymentDate;
        this.paymentStatus = status;
    }
    
     public PaymentModel(String paymentID, String customerName, String tourName, Date paymentDate, String amount, String status, String paymentMethod, String transactionId) {
        this.paymentID = paymentID;
        this.customerName = customerName;
        this.tourName = tourName;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionID = transactionId;
        this.paymentStatus = status;
        this.paymentDate = paymentDate;
    }

    public PaymentModel(String paymentID, String customerName, int tourId, Date paymentDate, String amount, String status, String paymentMethod, String transactionId) {
        this.paymentID = paymentID;
        this.customerName = customerName;
        this.tourId = tourId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionID = transactionId;
        this.paymentStatus = status;
        this.paymentDate = paymentDate;
    }

    public PaymentModel(String paymentId, String customerIdDisplay, String customerDisplayName, String tourName, Date paymentDate, String status) {
        this.paymentID = paymentId;
        this.customerID = customerIdDisplay;
        this.customerName = customerDisplayName;
        this.tourName = tourName;
        this.paymentDate = paymentDate;
        this.paymentStatus = status;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }
    
    

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
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

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
