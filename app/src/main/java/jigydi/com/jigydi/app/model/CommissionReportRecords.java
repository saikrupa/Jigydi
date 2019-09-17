package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saikrupa on 7/3/2018.
 */

public class CommissionReportRecords {

    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("cus_name")
    @Expose
    private String cusName;
    @SerializedName("total_payment")
    @Expose
    private String totalPayment;
    @SerializedName("deducted_amount")
    @Expose
    private String jigydiPayment;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("lookup_value")
    @Expose
    private String lookupValue;

    public String getLookupValue() {
        return lookupValue;
    }

    public void setLookupValue(String lookupValue) {
        this.lookupValue = lookupValue;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getJigydiPayment() {
        return jigydiPayment;
    }

    public void setJigydiPayment(String jigydiPayment) {
        this.jigydiPayment = jigydiPayment;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}