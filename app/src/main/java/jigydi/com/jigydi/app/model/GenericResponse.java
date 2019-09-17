package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saikrupa on 6/28/2017.
 */

public class GenericResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("record")
    @Expose
    private String record;
    @SerializedName("error")
    @Expose
    private String error;


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }



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