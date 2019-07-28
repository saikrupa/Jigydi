package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saikrupa on 6/15/2018.
 */

public class WorkerDepositModel {


    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("limit")
    @Expose
    private int limit;
    @SerializedName("record")
    @Expose
    private DepositRecords record;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public DepositRecords getRecord() {
        return record;
    }

    public void setRecord(DepositRecords record) {
        this.record = record;
    }
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}


