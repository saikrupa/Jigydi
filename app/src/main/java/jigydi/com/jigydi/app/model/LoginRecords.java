package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saikrupa on 6/25/2017.
 */

public class LoginRecords {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("mem_type")
    @Expose
    private String memType;
    @SerializedName("usertype")
    @Expose
    private String usertype;
    @SerializedName("role")
    @Expose
    private Object role;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("modified_by")
    @Expose
    private Object modifiedBy;
    @SerializedName("modified_date")
    @Expose
    private Object modifiedDate;
    @SerializedName("worker_id")
    @Expose
    private String workerId;
    @SerializedName("worker_name")
    @Expose
    private String workerName;
    @SerializedName("worker_address")
    @Expose
    private String workerAddress;
    @SerializedName("worker_phone")
    @Expose
    private String workerPhone;
    @SerializedName("worker_alt_phone")
    @Expose
    private String workerAltPhone;
    @SerializedName("worker_service")
    @Expose
    private String workerService;
    @SerializedName("worker_status")
    @Expose
    private String workerStatus;
    @SerializedName("max_works_allocation")
    @Expose
    private String maxWorksAllocation;

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    @SerializedName("service_name")
    @Expose
    private String service_name;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemType() {
        return memType;
    }

    public void setMemType(String memType) {
        this.memType = memType;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public Object getRole() {
        return role;
    }

    public void setRole(Object role) {
        this.role = role;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerAddress() {
        return workerAddress;
    }

    public void setWorkerAddress(String workerAddress) {
        this.workerAddress = workerAddress;
    }

    public String getWorkerPhone() {
        return workerPhone;
    }

    public void setWorkerPhone(String workerPhone) {
        this.workerPhone = workerPhone;
    }

    public String getWorkerAltPhone() {
        return workerAltPhone;
    }

    public void setWorkerAltPhone(String workerAltPhone) {
        this.workerAltPhone = workerAltPhone;
    }

    public String getWorkerService() {
        return workerService;
    }

    public void setWorkerService(String workerService) {
        this.workerService = workerService;
    }

    public String getWorkerStatus() {
        return workerStatus;
    }

    public void setWorkerStatus(String workerStatus) {
        this.workerStatus = workerStatus;
    }

    public String getMaxWorksAllocation() {
        return maxWorksAllocation;
    }

    public void setMaxWorksAllocation(String maxWorksAllocation) {
        this.maxWorksAllocation = maxWorksAllocation;
    }

}