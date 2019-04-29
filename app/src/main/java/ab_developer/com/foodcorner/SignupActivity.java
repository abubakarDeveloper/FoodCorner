package ab_developer.com.foodcorner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SignupActivity extends AppCompatActivity {

    TextInputLayout tilName;
    EditText etName;
    TextInputLayout tilEmail;
    EditText etEmail;
    TextInputLayout tilPassword;
    EditText etPassword;
    Button btnSignup;
    TextView tvLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        tilName = findViewById(R.id.til_name);
        etName = findViewById(R.id.et_name);
        tilEmail = findViewById(R.id.til_email);
        etEmail = findViewById(R.id.et_email);
        tilPassword = findViewById(R.id.til_password);
        etPassword = findViewById(R.id.et_password);
        btnSignup = findViewById(R.id.btn_signup);
        tvLogin = findViewById(R.id.tv_login);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tilName.setError(null);
                tilEmail.setError(null);
                tilPassword.setError(null);

                final String name = etName.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();

                if (name.isEmpty()) {
                    tilName.setError("Please Enter Name");
                    etName.requestFocus();
                } else if (!name.matches("^[a-zA-Z .-]{3,}$")) {
                    tilName.setError("Invalid Name. Only Alphabets are allowed.");
                    etName.requestFocus();
                } else if (email.isEmpty()) {
                    tilEmail.setError("Please Enter Email");
                    etEmail.requestFocus();
                } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                    tilEmail.setError("Invalid Email");
                    etEmail.requestFocus();

                } else if (password.isEmpty()) {
                    tilPassword.setError("Please Enter passowrd");
                    etPassword.requestFocus();
                } else if (password.length() < 8) {
                    tilPassword.setError("Password must be atleast 8 character long.");
                    etPassword.requestFocus();
                } else if (password.length() > 12) {
                    tilPassword.setError("Password must be less than 12 character");
                    etPassword.requestFocus();
                } else {
                    //Proceed with signup
                    final ProgressDialog pDialog = new ProgressDialog(SignupActivity.this);
                    pDialog.setMessage("Please Wait");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    StringRequest request = new StringRequest(Request.Method.POST, ApiConfig.SIGNUP_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismiss();
                            try {
                                JSONObject jObject = new JSONObject(response);
                                int status = jObject.getInt("status");
                                String message = jObject.getString("message");
                                if (status == 0) {
                                    //failed
                                    Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else {
                                    //success
                                    Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(SignupActivity.this, "Parsing Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            error.printStackTrace();
                            Toast.makeText(SignupActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", name);
                            params.put("email", email);
                            params.put("pass", password);

                            return params;

                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);
                }
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
