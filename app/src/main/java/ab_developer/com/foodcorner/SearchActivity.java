package ab_developer.com.foodcorner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    RecyclerView rvSearch;
    ProductAdapter adapter;
    SearchView searchView;
    SlidrInterface slider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        rvSearch = findViewById(R.id.rv_search);
        searchView = findViewById(R.id.searchView);
        slider=Slidr.attach(this);
        final ProgressDialog pDailog = new ProgressDialog(SearchActivity.this);
        pDailog.setMessage("Please Wait");
        pDailog.setCancelable(false);
        pDailog.show();
        StringRequest request = new StringRequest(ApiConfig.SEARCH_PRODUCTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDailog.dismiss();
                Toast.makeText(SearchActivity.this, "Success", Toast.LENGTH_SHORT).show();
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
                        p.pRating = Float.parseFloat(productObject.getString("p_rating"));
                        productList.add(p);
                    }
                    GridLayoutManager manager = new GridLayoutManager(SearchActivity.this, 2);
                    rvSearch.setLayoutManager(manager);
                    adapter = new ProductAdapter(productList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Product selectedProduct = productList.get(position);
                            Intent intent = new Intent(SearchActivity.this,ProductDetailActivity.class);
                            intent.putExtra("product", selectedProduct);
                      /*      intent.putExtra("p_id", selectedProduct.productId);
                            intent.putExtra("name", selectedProduct.name);
                            intent.putExtra("price", selectedProduct.price);
                      */      startActivity(intent);
                        }
                    });
                    rvSearch.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SearchActivity.this, "Parsing Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDailog.dismiss();
                error.printStackTrace();
                Toast.makeText(SearchActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
                searchView.requestFocus();
                searchView.getQuery();
                searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);

                        return false;
                    }
                });
    }

    public void lockSlide(View view){
        slider.lock();
    }
    public void unLockSlide(View view){
        slider.unlock();
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
*/
/*
        MenuItem searchItem = menu.findItem(R.id.action_search_product);

        SearchView searchView = (SearchView) searchItem.getActionView();
        //  searchItem.setVisible(false);
        searchView.requestFocus();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });
*//*


        return super.onCreateOptionsMenu(menu);
  //      return true;
    }
*/


}
