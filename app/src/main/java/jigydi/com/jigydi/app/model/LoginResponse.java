package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saikrupa on 6/25/2017.
 */

public class LoginResponse{

    @SerializedName("record")
    @Expose
    private LoginRecords record;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public LoginRecords getRecord() {
        return record;
    }

    public void setRecord(LoginRecords record) {
        this.record = record;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
