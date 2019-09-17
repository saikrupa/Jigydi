package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("record")
    @Expose
    private String record;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

}