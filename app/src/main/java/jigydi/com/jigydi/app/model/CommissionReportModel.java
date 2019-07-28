package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saikrupa on 7/3/2018.
 */

public class CommissionReportModel {

    @SerializedName("record")
    @Expose
    private List<CommissionReportRecords> record = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<CommissionReportRecords> getRecord() {
        return record;
    }

    public void setRecord(List<CommissionReportRecords> record) {
        this.record = record;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


}
