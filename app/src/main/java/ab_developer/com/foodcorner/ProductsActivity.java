package ab_developer.com.foodcorner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {
    RecyclerView rvProducts;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        rvProducts = findViewById(R.id.rv_products);
        swipeRefreshLayout = findViewById(R.id.swipe);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDataFromServer();
            }
        });

        fetchDataFromServer();
    }

    public void fetchDataFromServer(){
        Bundle bundle = getIntent().getExtras();
        int catId = bundle.getInt("cat_id");
        String catName = bundle.getString("cat_Name");
        if(catName != null)
            this.setTitle(catName);
        String url = ApiConfig.PRODUCTS_URL + "?cat_id=" + catId;
  /*      final ProgressDialog pDailog = new ProgressDialog(ProductsActivity.this);
        pDailog.setMessage("Please Wait");
        pDailog.setCancelable(false);
        pDailog.show();*/
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // pDailog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ProductsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                try {
                    Log.i("mytag", response);
                    JSONArray productsArray = new JSONArray(response);
                    final ArrayList<Product> productList = new ArrayList<>();
                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject productObject = productsArray.getJSONObject(i);
                        Product p = new Product();
                        p.productId = productObject.getInt("p_id");
                        p.name = productObject.getString("p_name");
                        p.price = productObject.getInt("p_price");
                        p.desc = productObject.getString("p_desc");
                        p.catId = productObject.getInt("cat_id");
                        p.imageLink = productObject.getString("p_image");
                        p.deal = productObject.getInt("p_deal");
                        p.pRating = (float) productObject.getDouble("p_rating");
                        productList.add(p);
                    }
                    GridLayoutManager manager = new GridLayoutManager(ProductsActivity.this, 2);
                    rvProducts.setLayoutManager(manager);
                    ProductAdapter adapter = new ProductAdapter(productList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Product selectedProduct = productList.get(position);
                            Intent intent = new Intent(ProductsActivity.this,ProductDetailActivity.class);
                            intent.putExtra("product", selectedProduct);
                      /*      intent.putExtra("p_id", selectedProduct.productId);
                            intent.putExtra("name", selectedProduct.name);
                            intent.putExtra("price", selectedProduct.price);
                      */      startActivity(intent);
                        }
                    });
                    rvProducts.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(ProductsActivity.this, "Parsing Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pDailog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Toast.makeText(ProductsActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.product, m);
        return true;
    }
}
