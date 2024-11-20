package model;

public class ServiceModel {
    private int serviceId;
    private String serviceName;
    private String serviceDescription;
    private String serviceSupplier;
    private String servicePackageName;
    private boolean included;
    public ServiceModel(int serviceId, String serviceName, String serviceDescription, String servicePackageName, String serviceSupplier) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.servicePackageName = servicePackageName;
        this.serviceSupplier = serviceSupplier;
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
