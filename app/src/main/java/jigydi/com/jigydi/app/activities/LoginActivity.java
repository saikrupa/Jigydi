package jigydi.com.jigydi.app.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jigydi.com.jigydi.R;
import jigydi.com.jigydi.app.model.LoginResponse;
import jigydi.com.jigydi.app.network.ApiInterface;
import jigydi.com.jigydi.app.network.NetworkUtils;
import jigydi.com.jigydi.app.network.ServiceGenerator;
import jigydi.com.jigydi.app.utils.CoreCustomPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by click2clinic on 10-06-2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public Toolbar toolbar;
    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    ApiInterface apiService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        etEmail=(EditText) findViewById(R.id.etName);
        etPassword=(EditText)findViewById(R.id.etPassword);
        apiService = ServiceGenerator.GetBaseUrl(ApiInterface.class);
        btnLogin.setOnClickListener(this);
        setUpToolBar();
        checkIsUserLoggedIn();
    }

    private void checkIsUserLoggedIn() {

        boolean is_login= CoreCustomPreferenceManager.getInstance(LoginActivity.this).getSharedBooleanValue(
                CoreCustomPreferenceManager.PreferenceKeys.IS_USER_LOGIN);
        if(is_login) {
            Intent i = new Intent(getApplicationContext(), WorksActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void login(View v){

    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        toolbar.setTitleTextAppearance(this, R.style.MyToolbarTitleApperance);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));

    }

    @Override
    public void onClick(View v) {

        if(etEmail.getText().length()<=0){
            Toast.makeText(getApplicationContext(),"Invalid Email",Toast.LENGTH_SHORT).show();
        }else if(etPassword.getText().length()<=0){
            Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_SHORT).show();
        }else{
            loginRequest(etEmail.getText().toString(),etPassword.getText().toString());
        }
    }

    private void loginRequest(final String userName, final String password) {

        final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "", getString(R.string.please_wait), false, false);
        loading.show();
        NetworkUtils.checkInternetConnection(LoginActivity.this, new NetworkUtils.NetworkStatusListener() {
            @Override
            public void onNetworkAvailable() {
                Call<LoginResponse> call = apiService.loginRquest(userName,password,2+"");
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse>call, Response<LoginResponse> response) {
                        LoginResponse movies = response.body();
                        loading.dismiss();
                        if(movies!=null && movies.getSuccess()) {
                            //Log.d("LoginActivity", "Number of movies received: " + movies.getSuccess());
                            CoreCustomPreferenceManager.getInstance(LoginActivity.this).setSharedBooleanValue(
                                    CoreCustomPreferenceManager.PreferenceKeys.IS_USER_LOGIN, true);
                            CoreCustomPreferenceManager.getInstance(LoginActivity.this).setSharedStringValue(
                                    CoreCustomPreferenceManager.PreferenceKeys.USER_ID, movies.getRecord().getUserId());
                            CoreCustomPreferenceManager.getInstance(LoginActivity.this).setSharedStringValue(
                                    CoreCustomPreferenceManager.PreferenceKeys.MEMBER_TYPE, movies.getRecord().getMemType());
                            CoreCustomPreferenceManager.getInstance(LoginActivity.this).setSharedStringValue(
                                    CoreCustomPreferenceManager.PreferenceKeys.WORKER_ID, movies.getRecord().getWorkerId());
                            CoreCustomPreferenceManager.getInstance(LoginActivity.this).setSharedStringValue(
                                    CoreCustomPreferenceManager.PreferenceKeys.WORKER_NAME, movies.getRecord().getWorkerName());
                            CoreCustomPreferenceManager.getInstance(LoginActivity.this).setSharedStringValue(
                                    CoreCustomPreferenceManager.PreferenceKeys.SERVICE_NAME, movies.getRecord().getService_name());

                            Intent i = new Intent(getApplicationContext(), WorksActivity.class);
                            i.putExtra("nav_to_allocated",false);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"Please enter correct details",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse>call, Throwable t) {
                        // Log error here since request failed
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity", t.toString());
                    }
                });
            }

            @Override
            public void onNetworkNotAvailable() {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();
            }
        });




    }


}
