package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saikrupa on 6/28/2017.
 */

public class AvailableWorksResponse {

    @SerializedName("record")
    @Expose
    private List<AvilableWorksList> record = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<AvilableWorksList> getRecord() {
        return record;
    }

    public void setRecord(List<AvilableWorksList> record) {
        this.record = record;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
