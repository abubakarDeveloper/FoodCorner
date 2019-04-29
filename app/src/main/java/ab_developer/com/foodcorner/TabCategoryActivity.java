package ab_developer.com.foodcorner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TabCategoryActivity extends AppCompatActivity {

    RecyclerView rvTabcategory;
    private android.support.v7.widget.Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    RecyclerView rvTabproduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_category);
        //rvTabcategory = findViewById(R.id.rv_tab_category);
       // rvTabproduct  =findViewById(R.id.rv_tab_product);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

/*

        for (int k = 0; k <10; k++) {
            tabLayout.addTab(tabLayout.newTab().setText("category.catName" + k));
        }

        PlansPagerAdapter adapter = new PlansPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (tabLayout.getTabCount() == 2) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
*/
        fetchDataFromServer();

    }
/*
    public class PlansPagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        public PlansPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
*/
private void fetchDataFromServer() {

    final ProgressDialog dialog = new ProgressDialog(TabCategoryActivity.this);
    dialog.setMessage("Please Wait");
    dialog.setCancelable(false);
    dialog.show();

    StringRequest request = new StringRequest(ApiConfig.TAB_CAT_URL, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            dialog.dismiss();
            Toast.makeText(TabCategoryActivity.this, "Succcess", Toast.LENGTH_SHORT).show();


            try {
                JSONObject jObject = new JSONObject(response);
                JSONArray categoryArray = jObject.getJSONArray("category_list");
                JSONArray productArray = jObject.getJSONArray("product_list");

                //end here
                //Category Code for displaying
                final ArrayList<Category> categoryList = new ArrayList<>();
                for (int i = 0; i < categoryArray.length(); i++) {
                    JSONObject categoryObject = categoryArray.getJSONObject(i);
                    Category category = new Category();
                    category.catId = categoryObject.getInt("cat_id");
                    category.catName = categoryObject.getString("cat_name");
                    category.catImage = categoryObject.getString("cat_image");
                    categoryList.add(category);
                }



                //Top Product Code for displaying
                final ArrayList<Product> topProductList = new ArrayList<>();
                for (int i = 0; i < productArray.length(); i++) {
                    JSONObject productObject1 = productArray.getJSONObject(i);
                    Product topProduct = new Product();
                    topProduct.productId = productObject1.getInt("p_id");
                    topProduct.name = productObject1.getString("p_name");
                    topProduct.price = productObject1.getInt("p_price");
                    topProduct.imageLink = productObject1.getString("p_image");
                    topProduct.desc = productObject1.getString("p_desc");
                    topProduct.catId = productObject1.getInt("cat_id");
                    topProduct.deal = productObject1.getInt("p_deal");
                    topProduct.pRating = (float) productObject1.getDouble("p_rating");
                    topProductList.add(topProduct);
                }
                ProductAdapter topproductAdapter = new ProductAdapter(topProductList, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Product selectedProduct = topProductList.get(position);
                        Intent intent = new Intent(TabCategoryActivity.this, ProductDetailActivity.class);
                        intent.putExtra("product", selectedProduct);
                        startActivity(intent);
                    }
                });

                    GridLayoutManager manager = new GridLayoutManager(TabCategoryActivity.this, 2);
                  //  rvTabproduct.setLayoutManager(manager);

                //rvTabproduct.setAdapter(topproductAdapter);
                TabCategoryAdapter adapter = new TabCategoryAdapter(categoryList, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Category selectedCategory = categoryList.get(position);

                            Intent intent = new Intent(TabCategoryActivity.this, ProductsActivity.class);
                            intent.putExtra("cat_id", selectedCategory.catId);
                            intent.putExtra("cat_name", selectedCategory.catName);

                        startActivity(intent);


                    }
                });
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TabCategoryActivity.this, LinearLayoutManager.HORIZONTAL, false);
                 //       rvTabcategory.setLayoutManager(linearLayoutManager);



            //rvTabcategory.setAdapter(adapter);
                //end

            } catch (JSONException e) {
                e.printStackTrace();

                Toast.makeText(TabCategoryActivity.this, "parsing error", Toast.LENGTH_SHORT).show();
            }

/*
                Log.i("myTag", response);
                Toast.makeText(MainActivity.this, "Successed", Toast.LENGTH_SHORT).show();
*/
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            dialog.dismiss();
            error.printStackTrace();
            Toast.makeText(TabCategoryActivity.this, "Volley error", Toast.LENGTH_SHORT).show();
        }
    });

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    request.setRetryPolicy(retryPolicy);
    queue.add(request);

}

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.product, m);
        return true;
    }
    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new MyFragment(), "TWO");
        adapter.addFrag(new MyFragment(), "THREE");
        adapter.addFrag(new MyFragment(), "FOUR");
        adapter.addFrag(new MyFragment(), "FIVE");
        adapter.addFrag(new MyFragment(), "SIX");
        adapter.addFrag(new MyFragment(), "SEVEN");
        adapter.addFrag(new MyFragment(), "EIGHT");
        adapter.addFrag(new MyFragment(), "NINE");
        adapter.addFrag(new MyFragment(), "TEN");

        viewPager.setAdapter(adapter);



    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final Category catFragmentTitleList = new Category();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();

        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        public void addFrag1(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
