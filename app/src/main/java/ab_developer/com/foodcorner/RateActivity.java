package ab_developer.com.foodcorner;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RateActivity extends AppCompatActivity {

    TextView tvRate;
    MaterialRatingBar mrbRating;
    String rating;
    Button btnRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        tvRate = findViewById(R.id.tv_rate);
        mrbRating = findViewById(R.id.mrb_rating);
        rating = String.valueOf(mrbRating.getRating());
        btnRating = findViewById(R.id.btn_rating);
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating();
            }
        });
        mrbRating.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                Toast.makeText(getApplicationContext(), String.valueOf(mrbRating.getRating()), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void rating(){
        final ProgressDialog dialog = new ProgressDialog(RateActivity.this);
        dialog.setMessage("Please Wait");
        dialog.setCancelable(false);
        dialog.show();

        StringRequest request = new StringRequest(ApiConfig.RATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(RateActivity.this, "Success", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ratingArray = jsonObject.getJSONArray("rate_list");

                    for(int i=0; i < ratingArray.length(); i++){
                        JSONObject ratingObject = ratingArray.getJSONObject(i);
                        int status = ratingObject.getInt("status");
                        String message = ratingObject.getString("message");
                        if (status == 0) {
                            //failed
                            Toast.makeText(RateActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            //success
                            Toast.makeText(RateActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(RateActivity.this, "Parsing Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(RateActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rate", rating);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}
