package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by click2clinic on 04-07-2017.
 */

public class WorkDetailModel {

    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("cus_name")
    @Expose
    private String cusName;
    @SerializedName("cus_contact_1")
    @Expose
    private String cusContact1;
    @SerializedName("cus_contact_2")
    @Expose
    private String cusContact2;
    @SerializedName("cus_address")
    @Expose
    private String cusAddress;
    @SerializedName("cus_add_land_mark")
    @Expose
    private String cusAddLandMark;
    @SerializedName("residence_type")
    @Expose
    private Object residenceType;
    @SerializedName("order_area")
    @Expose
    private String orderArea;
    @SerializedName("order_status")
    @Expose
    private String workerStatus;
    @SerializedName("reminder_date")
    @Expose
    private String customer_available_timing;
    @SerializedName("worker_remarks")
    @Expose
    private ArrayList<String> workerRemarks = null;
    @SerializedName("order_description")
    @Expose
    private String orderDescription;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("completed_date")
    @Expose
    private String completedDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }




    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }


    public ArrayList<String> getWorkDetails() {
        return workerRemarks;
    }

    public void setWorkDetails(ArrayList<String> workDetails) {
        this.workerRemarks = workDetails;
    }
    public String getCustomer_available_timing() {
        return customer_available_timing;
    }

    public void setCustomer_available_timing(String customer_available_timing) {
        this.customer_available_timing = customer_available_timing;
    }

    public String getWorkerStatus() {
        return workerStatus;
    }

    public void setWorkerStatus(String workerStatus) {
        this.workerStatus = workerStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusContact1() {
        return cusContact1;
    }

    public void setCusContact1(String cusContact1) {
        this.cusContact1 = cusContact1;
    }

    public String getCusContact2() {
        return cusContact2;
    }

    public void setCusContact2(String cusContact2) {
        this.cusContact2 = cusContact2;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusAddLandMark() {
        return cusAddLandMark;
    }

    public void setCusAddLandMark(String cusAddLandMark) {
        this.cusAddLandMark = cusAddLandMark;
    }

    public Object getResidenceType() {
        return residenceType;
    }

    public void setResidenceType(Object residenceType) {
        this.residenceType = residenceType;
    }

    public String getOrderArea() {
        return orderArea;
    }

    public void setOrderArea(String orderArea) {
        this.orderArea = orderArea;
    }

}