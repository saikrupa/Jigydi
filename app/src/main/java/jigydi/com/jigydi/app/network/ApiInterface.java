package jigydi.com.jigydi.app.network;

import jigydi.com.jigydi.app.model.AvailableWorksResponse;
import jigydi.com.jigydi.app.model.CommentsResponse;
import jigydi.com.jigydi.app.model.CommissionReportModel;
import jigydi.com.jigydi.app.model.ComplaintsResponse;
import jigydi.com.jigydi.app.model.FeedbackResponse;
import jigydi.com.jigydi.app.model.GenericResponse;
import jigydi.com.jigydi.app.model.LoginResponse;
import jigydi.com.jigydi.app.model.UpdateUserLatLongResponse;
import jigydi.com.jigydi.app.model.WorkDetaildResponse;
import jigydi.com.jigydi.app.model.WorkerDepositModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @FormUrlEncoded
    @POST(UrlUtils.LOGIN_URL)
    Call<LoginResponse> loginRquest(@Field("username") String userName,@Field("password") String password,@Field("usertype") String usertype);


    @FormUrlEncoded
    @POST(UrlUtils.AVAILABLE_WORKS)
    Call<AvailableWorksResponse> categoryBasedWorks(@Field("user_id") String usedID);

    @FormUrlEncoded
    @POST(UrlUtils.COMPLAINTS_LIST)
    Call<CommentsResponse> getCommentsList(@Field("order_id") String orderID);

    @FormUrlEncoded
    @POST(UrlUtils.ALLOCATED_WORKS)
    Call<AvailableWorksResponse> allocatedWorks(@Field("user_id") String usedID,@Field("worker_id") String workerID);

    @FormUrlEncoded
    @POST(UrlUtils.ASSIGN_WORKS_IF_AVAILABLE)
    Call<GenericResponse> assignWorkIfAvailable(@Field("order_id") String orderID, @Field("worker_id") String workerID, @Field("user_id") String userID);

    @FormUrlEncoded
    @POST(UrlUtils.GET_ORDER_DETAILS)
    Call<WorkDetaildResponse> getOderDetails(@Field("order_id") String orderID, @Field("user_id") String userID);

    @FormUrlEncoded
    @POST(UrlUtils.GET_COMPLAINTS_LIST)
    Call<ComplaintsResponse> getComplaintsList(@Field("user_id") String userID, @Field("code_id") String codeID);

    @FormUrlEncoded
    @POST(UrlUtils.START_WORK)
    Call<GenericResponse> startWork(@Field("user_id") String userID, @Field("order_id") String orderID,@Field("worker_id") String workerID,
                                        @Field("start_date") String startDate,@Field("completed_date") String completedDate,@Field("material_value") String materialValue,
                                        @Field("labour_value") String labourValue,@Field("worker_remarks") String workerRemarks,@Field("order_status") int workerWorkStatus);

    @FormUrlEncoded
    @POST(UrlUtils.START_WORK)
    Call<GenericResponse> commentOnWork(@Field("user_id") String userID, @Field("order_id") String orderID,@Field("worker_id") String workerID,@Field("worker_remarks") String workerRemarks);

    @FormUrlEncoded
    @POST(UrlUtils.RAISE_COMPLAINTS)
    Call<GenericResponse> raiseComplaint(@Field("user_id") String userID, @Field("order_id") String orderID, @Field("worker_id") String workerID, @Field("worker_complaint_id") String complaintID);

    @FormUrlEncoded
    @POST(UrlUtils.RESCHEDULE_WORK)
    Call<GenericResponse> rescheduleWork(@Field("user_id") String userID, @Field("order_id") String orderID, @Field("worker_id") String workerID, @Field("preferable_date") String date,@Field("preferable_time") String time);


    @FormUrlEncoded
    @POST(UrlUtils.SEND_DEVICE_TOKEN)
    Call<GenericResponse> saveDeviceToken(@Field("user_id") String userID, @Field("mobile_type") String mobileType, @Field("push_token") String pushToken);


    //for supervisor related
    @FormUrlEncoded
    @POST(UrlUtils.AVAILABLE_WORKS_SUPERVISOR)
    Call<AvailableWorksResponse> availableWorksSupervisor(@Field("user_id") String usedID);

    @FormUrlEncoded
    @POST(UrlUtils.ALLOCATED_WORKS_SUPERVISOR)
    Call<AvailableWorksResponse> allocatedWorksSupervisor(@Field("user_id") String usedID);

    @FormUrlEncoded
    @POST(UrlUtils.ASSIGN_WORK_SUPERVISOR)
    Call<GenericResponse> assignWorkSupervisor(@Field("user_id") String usedID,@Field("order_id") String order_id);

    @FormUrlEncoded
    @POST(UrlUtils.INSPECT_WORK_SUPERVISOR)
    Call<GenericResponse> inspectWorkSupervisor(@Field("user_id") String usedID,@Field("order_id") String order_id,@Field("is_inspected") boolean is_inspected  ,@Field("supervisor_remarks") String supervisor_remarks);

    @FormUrlEncoded
    @POST(UrlUtils.REPORT_COMPLAINT_SUPERVISOR)
    Call<GenericResponse> reportComplaintsSupervisor(@Field("user_id") String usedID,@Field("order_id") String order_id,@Field("supervisor_complaint_id") String complaint_id,@Field("supervisor_remarks") String supervisor_remarks);

    @FormUrlEncoded
    @POST(UrlUtils.SHARE_LOCATION_UPDATE)
    Call<UpdateUserLatLongResponse> shareLocationUpdates(@Field("user_id") String usedID, @Field("coordinates") String coordinates);

    @FormUrlEncoded
    @POST(UrlUtils.CHECK_WORKER_DEPOSIT_AMOUNT)
    Call<WorkerDepositModel> getWorkerDepositAmount(@Field("worker_id") String workerID);

    @FormUrlEncoded
    @POST(UrlUtils.COMMISSION_REPORT_LIST)
    Call<CommissionReportModel> getCommissionReport(@Field("worker_id") String workerID);


    @FormUrlEncoded
    @POST(UrlUtils.WORKER_FEEDBACK)
    Call<FeedbackResponse> getWorkerFeedback(@Field("worker_id") String workerID);

}