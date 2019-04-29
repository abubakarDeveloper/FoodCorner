package ab_developer.com.foodcorner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PhoneLoginActivity extends AppCompatActivity {

    TextInputLayout tilPhone;
    EditText etPhone;
    TextInputLayout tilPassword;
    EditText etPassword;
    Button btnLogin;
    Button btnForgotPassword;
    TextView tvSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        tilPhone = (TextInputLayout) findViewById(R.id.til_phone);
        etPhone = findViewById(R.id.et_phone);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnForgotPassword = findViewById(R.id.btn_forgot_password);
        tvSignup = findViewById(R.id.tv_signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilPhone.setError(null);
                tilPassword.setError(null);
                final String phone = etPhone.getText().toString().trim();
                final String password = etPassword.getText().toString();
                if(phone.isEmpty()){
                    // Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
           /*         etEmail.setError("Please Enter Email");
                    //RequestFocus is compulsory
                    etEmail.requestFocus();*/

                    tilPhone.setError("Please Enter Phone");
                    etPhone.requestFocus();

                }else if(!phone.matches("^((\\+92)|(0092))-{0,1}\\d{3}-{0,1}\\d{7}$|^\\d{11}$|^\\d{4}-\\d{7}$")){
                    tilPhone.setError("Invalid Phone");
                    etPhone.requestFocus();

                }else if (password.isEmpty()){
                    // Toast.makeText(LoginActivity.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                    tilPassword.setError("Please Enter password");
                    //requestFocus is optional in this trick
                    etPassword.requestFocus();
                }else if(password.length() < 8){
                    tilPassword.setError("Password must be atleast 8 Characters Long.");
                    etPassword.requestFocus();
                    // Toast.makeText(LoginActivity.this, "Prodeec with Login", Toast.LENGTH_SHORT).show();
                }else if (password.length() > 12){
                    tilPassword.setError("Password length should not exceed 12 character");
                    etPassword.requestFocus();
                }else {

                    // Toast.makeText(LoginActivity.this, "EveryThing is ok, Go for login. ", Toast.LENGTH_SHORT).show();
                    final ProgressDialog pDialog = new ProgressDialog(PhoneLoginActivity.this);
                    pDialog.setMessage("Please Wait");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    StringRequest request = new StringRequest(Request.Method.POST, ApiConfig.PHONE_LOGIN_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismiss();
                            Log.i("myTag", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int status = jsonObject.getInt("status");
                                String message = jsonObject.getString("message");
                                if (status == 0) {
                                    //failure
                                    Log.i("mytag", message);
                                    Toast.makeText(PhoneLoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else {
                                    //success
                                    JSONObject userObject = jsonObject.getJSONObject("user");
          /*                          int userId = userObject.getInt("user_id");
                                    String email = userObject.getString("email");
                                    String name = userObject.getString("fullname");*/
                                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(PhoneLoginActivity.this);
                                    // put complete object of user
                                    sharedPref.edit()
                                            .putBoolean("is_user_logged_in", true)
                                            .putString("logged_user", userObject.toString())
                                            .apply();
                                  /*  sharedPref.edit().putBoolean("is_user_logged_in", true)
                                            .putString("user_name",name)
                                            .putString("email",email)
                                            .putInt("user_id" ,userId)
                                            .apply();*/

                                    SessionHelper.createLoginSession(PhoneLoginActivity.this, userObject);

                                    Bundle bundle = getIntent().getExtras();
                                    String destination = bundle.getString("destination");
                                    if (bundle == null) {
                                        finish();
                                    } else if (destination.equals("address")) {
                                        Intent intent = new Intent(PhoneLoginActivity.this, AddressActivity.class);
                                        startActivity(intent);
                                    } else {
                                        finish();
                                    }
                                    // intent.putExtra("userName",fullName);
                                    //intent.putExtra("userImage",image);


                                    Toast.makeText(PhoneLoginActivity.this, message, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            error.printStackTrace();
                            Toast.makeText(PhoneLoginActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("phone", phone);
                            params.put("pass", password);

                            return params;

                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);

                    finish();

                }
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneLoginActivity.this, PhoneSignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneLoginActivity.this, NumberTakenActivity.class);
                startActivity(intent);
            }
        });
    }
}
