package model;

public class ServiceModel {
    private int serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceSupplier;
    private String servicePackageName;
    private int packageId;
    private int supplierID;
    public ServiceModel(int serviceId, String serviceName, String serviceDescription, String servicePackageName, String serviceSupplier) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.servicePackageName = servicePackageName;
        this.serviceSupplier = serviceSupplier;
    }

    public ServiceModel(int serviceId, String serviceName, String serviceDescription, int packageId, String serviceSupplier) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.packageId = packageId;
        this.serviceSupplier = serviceSupplier;
    }

    public ServiceModel(String serviceName, String description, int supplierID, int packageID) {
        this.serviceName = serviceName;
        this.serviceDescription = description;
        this.supplierID = supplierID;
        this.packageId = packageID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getServiceSupplier() {
        return serviceSupplier;
    }

    public void setServiceSupplier(String serviceSupplier) {
        this.serviceSupplier = serviceSupplier;
    }

    public String getServicePackageName() {
        return servicePackageName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }
}
