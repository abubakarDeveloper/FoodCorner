package ab_developer.com.foodcorner;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {
    TextInputLayout tilAddress;
    RadioButton rdoEasyPaisa, rdoJazz, rdoCOD, rdoCard;
    EditText etAddress;
    Spinner spCity;
    EditText etContact;
    TextInputLayout tilContact;
    RelativeLayout submitOrderLayout;
    RadioGroup rgPayment;

    String contactNumber;
    String city;
    int userId;
    String address;
    String paymentMethod;
    ArrayList<CartItem> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        tilAddress = findViewById(R.id.til_address);
        tilContact = findViewById(R.id.til_phone);
        spCity = findViewById(R.id.sp_city);

        rgPayment =findViewById(R.id.rg_payment);
        etAddress = findViewById(R.id.et_address);
        etContact = findViewById(R.id.et_phone);
        rdoCard = findViewById(R.id.rdo_card);
        rdoCOD = findViewById(R.id.rdo_cod);
        rdoJazz = findViewById(R.id.rdo_jazzcash);
        rdoEasyPaisa = findViewById(R.id.rdo_easypaisa);
        submitOrderLayout = findViewById(R.id.layout_submit_order);


        submitOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(spCity.getSelectedItemPosition()==o){
                    Toast.makeText(AddressActivity.this, "please select a city", Toast.LENGTH_SHORT).show();
                }
                */


                address = etAddress.getText().toString();
                contactNumber = etContact.getText().toString().trim();
                city = spCity.getSelectedItem().toString();

                if (address.isEmpty()) {
                    etAddress.setError("Please Enter Address");
                    etAddress.requestFocus();

                } else {

                    paymentMethod = "";
                    if (rdoCard.isChecked())
                        paymentMethod = "Card";
                    else if (rdoCOD.isChecked())
                        paymentMethod = "Cash on delivery";
                    else if (rdoEasyPaisa.isChecked())
                        paymentMethod = "Easypaisa";
                    else if (rdoJazz.isChecked())
                        paymentMethod = "JazzCash";


                    final CartHelper cartHelper = new CartHelper(AddressActivity.this);
                    cartList = cartHelper.getAllCartProducts();

                    final ProgressDialog pdialog = new ProgressDialog(AddressActivity.this);
                    pdialog.setCancelable(false);
                    pdialog.setMessage("pleae wait");
                    pdialog.show();

                    StringRequest request = new StringRequest(Request.Method.POST, ApiConfig.SAVE_ORDER_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            pdialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int status = jsonObject.getInt("status");
                                String message = jsonObject.getString("message");
                                if (status == 0) {

                                    Toast.makeText(AddressActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else {

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder( AddressActivity.this);
                                    builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
                                    Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.journaldev.com/"));
                                    PendingIntent pendingIntent = PendingIntent.getActivity( AddressActivity.this, 0, notificationIntent, 0);
                                    builder.setContentIntent(pendingIntent);
                                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                                    builder.setContentTitle("Hardeez");
                                    builder.setContentText(message+" Your order will be submitted in 30 Minutes!");
                                    builder.setSubText("Tap to track your order");

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                    // Will display the notification in the notification bar
                                    notificationManager.notify(1, builder.build());


                                  //  Toast.makeText(AddressActivity.this, message, Toast.LENGTH_SHORT).show();
                                    cartHelper.clearCart();
                                    Intent intent = new Intent(AddressActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(AddressActivity.this, "pasring error", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pdialog.dismiss();
                            error.printStackTrace();
                            Toast.makeText(AddressActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();


                        }
                    }) {


                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            JSONObject userObject = SessionHelper.getCurrentUser(AddressActivity.this);
                            try {
                                int userId = userObject.getInt("user_id");
                                params.put("user_id", String.valueOf(userId));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            params.put("delivery_address", address);
                            params.put("contact_number", contactNumber);
                            params.put("payment_method", paymentMethod);
                            params.put("city", city);
                            JSONArray cartArray = new JSONArray();
                            for (int i = 0; i < cartList.size(); i++) {
                                CartItem cItem = cartList.get(i);
                                JSONObject cartObject = new JSONObject();
                                try {
                                    cartObject.put("p_id", cItem.p.productId);
                                    cartObject.put("quantity", cItem.quantity);
                                    cartObject.put("p_price", cItem.p.price);
                                    cartObject.put("size", cItem.size);
                                    cartArray.put(cartObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.i("mytag", cartArray.toString());
                            params.put("cart_list", cartArray.toString());
                            params.put("total_amount", String.valueOf(cartHelper.getCartTotalAmount()));

                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);
                }
            }
        });


    }
}

