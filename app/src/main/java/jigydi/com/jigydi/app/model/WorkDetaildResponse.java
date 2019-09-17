package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by click2clinic on 04-07-2017.
 */

public class WorkDetaildResponse {

    @SerializedName("record")
    @Expose
    private WorkDetailModel record;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public WorkDetailModel getRecord() {
        return record;
    }

    public void setRecord(WorkDetailModel record) {
        this.record = record;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}