package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saikrupa on 6/28/2017.
 */

public class AvilableWorksList {



    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
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
    @SerializedName("order_description")
    @Expose
    private String orderDescription;
    @SerializedName("order_area")
    @Expose
    private Object orderArea;
    @SerializedName("reminder_date")
    @Expose
    private String availability;
    @SerializedName("reminder_time")
    @Expose
    private String reminderTime;

    @SerializedName("zone_name")
    @Expose
    private String zone_center;
    @SerializedName("order_status")
    @Expose
    private int orderStatus;

    public int getOrderStatus() {
        return orderStatus;
    }


    public String getZone_center() {
        return zone_center;
    }

    public void setZone_center(String zone_center) {
        this.zone_center = zone_center;
    }


    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }




    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Object getOrderArea() {
        return orderArea;
    }

    public void setOrderArea(Object orderArea) {
        this.orderArea = orderArea;
    }
}