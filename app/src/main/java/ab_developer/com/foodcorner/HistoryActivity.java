package ab_developer.com.foodcorner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

/*

    ImageView ivProductImage;
    TextView tvUserName;
    TextView tvProductName;
    TextView tvProductPrice;
    TextView tvQuantity;
    TextView tvTotalBill;
*/


    RecyclerView rvHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        rvHistory = findViewById(R.id.rv_history);

        final ProgressDialog dialog = new ProgressDialog(HistoryActivity.this);
        dialog.setMessage("Please Wait");
        dialog.setCancelable(false);
        dialog.show();

        StringRequest request = new StringRequest(ApiConfig.HISTORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dialog.dismiss();
                Toast.makeText(HistoryActivity.this, "HistorySucccess", Toast.LENGTH_SHORT).show();

                try {
                    Log.i("myTag", response);
                    JSONArray jsonArray = new JSONArray(response);
//                    JSONArray historyArray = jObject.getJSONArray("menu_list");

                    final ArrayList<History> historyList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject historyObject = jsonArray.getJSONObject(i);
                        History history = new History();

                    //    history.userName = historyObject.getString("u_name");
                   //     history.imageLink = historyObject.getString("p_image");
                        history.productName = historyObject.getString("p_name");
                        history.quantity = historyObject.getInt("quantity");
                        history.price = historyObject.getInt("p_price");
                        history.total_Bill = historyObject.getInt("total_amount");
                        historyList.add(history);
                    }
                    //end

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoryActivity.this, LinearLayoutManager.VERTICAL, false);
                    rvHistory.setLayoutManager(linearLayoutManager);

                    HistoryAdapter adapter = new HistoryAdapter(historyList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                    rvHistory.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HistoryActivity.this, "HistoryParsing Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(HistoryActivity.this, "HistoryVolley error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                request.setRetryPolicy(retryPolicy);
                queue.add(request);


        /*
        Bundle bundle = getIntent().getExtras();


        ivProductImage = findViewById(R.id.iv_product_image);
        tvUserName = findViewById(R.id.tv_user_name);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductPrice = findViewById(R.id.tv_product_price);
        tvQuantity = findViewById(R.id.tv_quantity);
        tvTotalBill = findViewById(R.id.tv_total_bill);


        History  selectedHistory = (History)bundle.getSerializable("history");

        Picasso.with(HistoryActivity.this)
                .load(selectedHistory.imageLink)
                .into(ivProductImage);
        tvUserName.setText(selectedHistory.userName);
        tvProductName.setText(selectedHistory.productName);
        tvProductPrice.setText("Rs." + selectedHistory.price);
        tvQuantity.setText(selectedHistory.quantity);
        tvTotalBill.setText("Rs." + selectedHistory.total_Bill);
*/


    }
}
