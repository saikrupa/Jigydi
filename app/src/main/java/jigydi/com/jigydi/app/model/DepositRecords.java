package jigydi.com.jigydi.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saikrupa on 6/15/2018.
 */

public class DepositRecords {

    @SerializedName("floating_amount")
    @Expose
    private String floatingAmount;

    @SerializedName("work_type")
    @Expose
    private String workType;


    public String getFloatingAmount() {
        return floatingAmount;
    }

    public void setFloatingAmount(String floatingAmount) {
        this.floatingAmount = floatingAmount;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

}