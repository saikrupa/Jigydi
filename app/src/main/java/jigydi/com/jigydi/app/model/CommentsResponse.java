package jigydi.com.jigydi.app.model;

import android.icu.text.AlphabeticIndex;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saikrupa on 10/16/2017.
 */

public class CommentsResponse {

    @SerializedName("record")
    @Expose
    private List<Comments> record = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<Comments> getRecord() {
        return record;
    }

    public void setRecord(List<Comments> record) {
        this.record = record;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}