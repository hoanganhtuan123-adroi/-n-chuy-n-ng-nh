package model;

import java.sql.Date;
import java.sql.Timestamp;

public class TourModel {
    private int tour_id;
    private int package_id;
    private String tour_name;
    private String description;
    private int duration;
    private Date start_date;
    private Date end_date;
    private String destination;
    private String departure_location;
    private int capacity;
    private int available_seats;
    private int prices;
    private String image;
    private Timestamp created_at;
    private String packageName;
    private String packageDescription;
    public TourModel(int tour_id, int package_id, String tour_name, String description, int duration, Date start_date, Date end_date, String destination, String departure_location, int capacity, int available_seats, int prices, String image, Timestamp created_at, String packageName, String packageDescription) {
        this.tour_id = tour_id;
        this.package_id = package_id;
        this.tour_name = tour_name;
        this.description = description;
        this.duration = duration;
        this.start_date = start_date;
        this.end_date = end_date;
        this.destination = destination;
        this.departure_location = departure_location;
        this.capacity = capacity;
        this.available_seats = available_seats;
        this.prices = prices;
        this.image = image;
        this.created_at = created_at;
        this.packageName = packageName;
        this.packageDescription = packageDescription;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public int getTour_id() {
        return tour_id;
    }

    public void setTour_id(int tour_id) {
        this.tour_id = tour_id;
    }

    public int getPackage_id() {
        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    public String getTour_name() {
        return tour_name;
    }

    public void setTour_name(String tour_name) {
        this.tour_name = tour_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture_location() {
        return departure_location;
    }

    public void setDeparture_location(String departure_location) {
        this.departure_location = departure_location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(int available_seats) {
        this.available_seats = available_seats;
    }

    public int getPrices() {
        return prices;
    }

    public void setPrices(int prices) {
        this.prices = prices;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}