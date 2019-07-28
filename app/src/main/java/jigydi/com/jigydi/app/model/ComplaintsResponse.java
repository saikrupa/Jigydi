package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by click2clinic on 05-07-2017.
 */

public class ComplaintsResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("record")
    @Expose
    private List<ComplaintsListModel> record = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ComplaintsListModel> getRecord() {
        return record;
    }

    public void setRecord(List<ComplaintsListModel> record) {
        this.record = record;
    }

}