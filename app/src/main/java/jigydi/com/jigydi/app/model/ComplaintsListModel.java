package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by click2clinic on 05-07-2017.
 */

public class ComplaintsListModel {

    @SerializedName("option_id")
    @Expose
    private String optionId;
    @SerializedName("lookup_value")
    @Expose
    private String lookupValue;

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getLookupValue() {
        return lookupValue;
    }

    public void setLookupValue(String lookupValue) {
        this.lookupValue = lookupValue;
    }

}

