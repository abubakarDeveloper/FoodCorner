package ab_developer.com.foodcorner;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.ibrahimsn.particle.ParticleView;

public class TabActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    ArrayList<Category> categoryList;
    ViewPagerAdapter viewPagerAdapter;

    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        fetchDataFromServer();
//           Toolbar_Code for icon app:layout_scrollFlags="scroll|enterAlways"
    }

    private void fetchDataFromServer() {

        Bundle bundle = getIntent().getExtras();
        //int catId = bundle.getInt("cat_id");
        position = bundle.getInt("position");
//      String catName=bundle.getString("cat_name");

/*
        if (catName!=null)
        {
            this.setTitle(catName);
        }
*/

        StringRequest request = new StringRequest(ApiConfig.MAIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("myTag", response);
                Toast.makeText(TabActivity.this, "Succcess", Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONArray categoryArray = jObject.getJSONArray("category_list");

                    //Category Code for displaying
                    categoryList = new ArrayList<>();
                    for (int i = 0; i < categoryArray.length(); i++) {
                        JSONObject categoryObject = categoryArray.getJSONObject(i);
                        Category category = new Category();
                        category.catId = categoryObject.getInt("cat_id");
                        category.catName = categoryObject.getString("cat_name");
                        category.catImage = categoryObject.getString("cat_image");
                        categoryList.add(category);
                    }

                    //end
                    viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                    viewPager.setAdapter(viewPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                    viewPager.setCurrentItem(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TabActivity.this, "parsing error", Toast.LENGTH_SHORT).show();
                }

/*
                Log.i("myTag", response);
                Toast.makeText(MainActivity.this, "Successed", Toast.LENGTH_SHORT).show();
*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(TabActivity.this, "Volley error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        queue.add(request);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(categoryList.get(position).catId);
        }

        @Override
        public int getCount() {
            return categoryList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categoryList.get(position).catName;
        }
    }


    public static class PlaceholderFragment extends Fragment {

        ArrayList<Product> productsList;

        public static PlaceholderFragment newInstance(int catId) {
            Bundle bundle = new Bundle();
            bundle.putInt("catId", catId);
            PlaceholderFragment instance = new PlaceholderFragment();
            instance.setArguments(bundle);
            return instance;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);


            int catId = getArguments().getInt("catId");

            final RecyclerView rvProducts;
            final ParticleView particleView= rootView.findViewById(R.id.particleView);

            rvProducts = rootView.findViewById(R.id.rv_products);
            final SwipeRefreshLayout swipe;
            swipe = rootView.findViewById(R.id.swipe);
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please Wait");
            dialog.setCancelable(false);
            dialog.show();

            String url = ApiConfig.PRODUCTS_URL + "?cat_id=" + catId;
            final StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    swipe.setRefreshing(false);
                    Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_LONG).show();

//                    new GlideToast.makeToast(getActivity(), "SUCCESS", 3000, GlideToast.SUCCESSTOAST, GlideToast.BOTTOM, R.mipmap.ic_launcher, "#ffffff").show();
                    try {
                        Log.i("mytag", response);
                        JSONArray productsArray = new JSONArray(response);
                        Gson gson = new Gson();
                        productsList = gson.fromJson(productsArray.toString(), new TypeToken<ArrayList<Product>>() {
                        }.getType());

                        /*for (int i = 0; i < productsArray.length(); i++) {
                            JSONObject productObject = productsArray.getJSONObject(i);
                            Product p = new Product();

                            p.productId = productObject.getInt("p_id");
                            p.name = productObject.getString("p_name");
                            p.price = productObject.getInt("p_price");
                            p.desc = productObject.getString("p_desc");
                            p.catId = productObject.getInt("cat_id");
                            p.imageLink = productObject.getString("p_image");
                            p.deal = productObject.getInt("p_deal");
                            //p.pRating = (float) productObject.getDouble("p_rating");
                            fullProductList.add(p);
                        }*/

                        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
                        rvProducts.setLayoutManager(manager);
                        ProductAdapter adapter = new ProductAdapter(productsList, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Product selectedProduct = productsList.get(position);
                                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                                intent.putExtra("product", selectedProduct);
                      /*      intent.putExtra("p_id", selectedProduct.productId);
                            intent.putExtra("name", selectedProduct.name);
                            intent.putExtra("price", selectedProduct.price);
                      */
                                startActivity(intent);
                            }
                        });
                        rvProducts.setAdapter(adapter);
                    } catch (JSONException e) {
                        swipe.setRefreshing(false);
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Parsing Error", Toast.LENGTH_SHORT).show();
                        //new GlideToast.makeToast(getActivity(), "PARSING ERROR", 3000, GlideToast.WARNINGTOAST, GlideToast.TOP, R.mipmap.ic_launcher, "#ffff00").show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    swipe.setRefreshing(false);
                    error.printStackTrace();
                    Toast.makeText(getActivity(), "Volley Error", Toast.LENGTH_SHORT).show();
                    //new GlideToast.makeToast(getActivity(), "VOLLEY ERROR", 3000, GlideToast.FAILTOAST, GlideToast.CENTER, R.mipmap.ic_launcher, "#ff0000").show();

                }
            });
            final RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);

            swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    queue.add(request);
                }
            });

            return rootView;
        }
    }
}