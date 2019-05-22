package ab_developer.com.foodcorner;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mikepenz.actionitembadge.library.ActionItemBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SliderLayout slider;
    RecyclerView rvRecentProducts, rvCategories, rvTopProducts;
    NavigationView navigationView;
    CartHelper cartHelper;
    //SwipeRefreshLayout swipeRefreshLayout;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cartHelper = new CartHelper(MainActivity.this);
        //gestureObject = new GestureDetectorCompat(this, new learnGesture());

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Code written by developer

        slider = findViewById(R.id.slider);
        //code inline category display
        rvRecentProducts = findViewById(R.id.rv_recent_products);
        rvCategories = findViewById(R.id.rv_categories);
        rvTopProducts = findViewById(R.id.rv_top_products);

        //Categories purpose
        rvCategories.setNestedScrollingEnabled(false);
        rvTopProducts.setNestedScrollingEnabled(false);
        rvRecentProducts.setNestedScrollingEnabled(false);
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(2000);
        mWaveSwipeRefreshLayout = findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                fetchDataFromServer();
            }
        });
/*            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            // your code here
                            fetchDataFromServer();
                        }
                    },
                    5000
            );*/

        //   List<Category> exampleItemList;
        fetchDataFromServer();

      }

    private void fetchDataFromServer() {

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Please Wait");
        dialog.setCancelable(false);
        dialog.show();

        StringRequest request = new StringRequest(ApiConfig.MAIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                mWaveSwipeRefreshLayout.setRefreshing(false);
                //Toast.makeText(MainActivity.this, "Succcess", Toast.LENGTH_SHORT).show();
//                new GlideToast.makeToast(MainActivity.this, "SUCCESS", GlideToast.LENGTHLONG, GlideToast.SUCCESSTOAST).show();
                Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();

                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONArray slidesArray = jObject.getJSONArray("slide_list");
                    JSONArray categoryArray = jObject.getJSONArray("category_list");
                    JSONArray productArray = jObject.getJSONArray("product_list");
                    JSONArray topProductArray = jObject.getJSONArray("topProduct_list");

                    //Code for slider printing
                    for (int i = 0; i < slidesArray.length(); i++) {
                        JSONObject slideObject = slidesArray.getJSONObject(i);
                        String slideImage = slideObject.getString("slide_image");
                        String slideCaption = slideObject.getString("slide_caption");

                        TextSliderView slide = new TextSliderView(MainActivity.this);
                        slide.image(slideImage);
                        slide.description(slideCaption);
                        slider.addSlider(slide);
                    }
                    //end here
/*

                    //Category Code for displaying Categories in as MenuBar
                    //code inline category display

                    ArrayList<Category> linearCategoryList = new ArrayList<>();
                    for(int i=0; i<categoryArray.length(); i++){
                        JSONObject categoryObject = categoryArray.getJSONObject(i);
                        Category category = new Category();
                        category.catId = categoryObject.getInt("cat_id");
                        category.catName = categoryObject.getString("cat_name");
                        linearCategoryList.add(category);
                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    rvRecentProducts.setLayoutManager(linearLayoutManager);
                    LinearCategoryAdapter linearCategoryAdapter = new LinearCategoryAdapter(linearCategoryList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                    rvRecentProducts.setAdapter(linearCategoryAdapter);
                    //end

*/
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

                    //Recent Product Code for displaying
                    final ArrayList<Product> productList = new ArrayList<>();
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject productObject = productArray.getJSONObject(i);
                        Product recentProduct = new Product();
                        recentProduct.productId = productObject.getInt("p_id");
                        recentProduct.name = productObject.getString("p_name");
                        recentProduct.price = productObject.getInt("p_price");
                        recentProduct.imageLink = productObject.getString("p_image");
                        recentProduct.desc = productObject.getString("p_desc");
                        recentProduct.catId = productObject.getInt("cat_id");
                        recentProduct.deal = productObject.getInt("p_deal");
                       // recentProduct.pRating = (float) productObject.getDouble("p_rating");

                        productList.add(recentProduct);
                    }


                    ProductAdapter productAdapter = new ProductAdapter(productList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Product selectedProduct = productList.get(position);
                            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                            intent.putExtra("product", selectedProduct);
                            startActivity(intent);
                        }
                    });


                    //Top Product Code for displaying
                    final ArrayList<Product> topProductList = new ArrayList<>();
                    for (int i = 0; i < topProductArray.length(); i++) {
                        JSONObject productObject1 = productArray.getJSONObject(i);
                        Product topProduct = new Product();
                        topProduct.productId = productObject1.getInt("p_id");
                        topProduct.name = productObject1.getString("p_name");
                        topProduct.price = productObject1.getInt("p_price");
                        topProduct.imageLink = productObject1.getString("p_image");
                        topProduct.desc = productObject1.getString("p_desc");
                        topProduct.catId = productObject1.getInt("cat_id");
                        topProduct.deal = productObject1.getInt("p_deal");
                        //topProduct.pRating = (float) productObject1.getDouble("p_rating");
                        topProductList.add(topProduct);
                    }
                    ProductAdapter topproductAdapter = new ProductAdapter(topProductList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Product selectedProduct = topProductList.get(position);
                            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                            intent.putExtra("product", selectedProduct);
                            startActivity(intent);
                        }
                    });
/*
                    GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 2);
                    rvCategories.setLayoutManager(manager);*/
                    CategoryAdapter adapter = new CategoryAdapter(categoryList, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Category selectedCategory = categoryList.get(position);
                            //      Intent intent = new Intent(MainActivity.this, TabCategoryActivity.class);

                            Intent intent = new Intent(MainActivity.this, TabActivity.class);
                            intent.putExtra("cat_id", selectedCategory.catId);
                            intent.putExtra("cat_name", selectedCategory.catName);
                            intent.putExtra("position", position);
                            startActivity(intent);


                        }
                    });
                    int count = 0;
                    if (adapter != null) {
                        count = adapter.getItemCount();

                        if (count % 2 == 0) {
                            GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 2);
                            rvCategories.setLayoutManager(manager);
                        } else {
                            LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                            rvCategories.setLayoutManager(manager);
                        }
                    }

                    rvCategories.setAdapter(adapter);
                    //end

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    rvRecentProducts.setLayoutManager(linearLayoutManager);
                    rvRecentProducts.setAdapter(productAdapter);

                    LinearLayoutManager topLinearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    rvTopProducts.setLayoutManager(topLinearLayoutManager);
                    rvTopProducts.setAdapter(topproductAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "parsing error", Toast.LENGTH_SHORT).show();
                    //new GlideToast.makeToast(MainActivity.this, "PARSING ERROR", 3000, GlideToast.WARNINGTOAST).show();
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
                //swipeRefreshLayout.setRefreshing(false);
                mWaveSwipeRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                //new GlideToast.makeToast(MainActivity.this, "VOLLEY ERROR", GlideToast.LENGTHLONG, GlideToast.FAILTOAST).show();
                Toast.makeText(MainActivity.this, "VOLLEY ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        queue.add(request);

    }

/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);

    }    class learnGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (e2.getX() > e1.getX()) {

                //Left To Right Swipe
            } else if (e2.getX() < e1.getX()) {
                //Right ToLeft Swipe
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }

            return true;
        }
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        int cartCount = cartHelper.getCartCount();
        if (cartCount > 0) {
            Drawable cartDrawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_shopping_cart_white_24dp, null);
            ActionItemBadge.update(MainActivity.this, menu.findItem(R.id.action_cart),
                    cartDrawable, ActionItemBadge.BadgeStyles.YELLOW, cartCount);
        } else {
            ActionItemBadge.hide(menu.findItem(R.id.action_cart));
        }

        final MenuItem searchItem = menu.findItem(R.id.action_search_product);
        // searchItem.setVisible(false);


/*        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                CategoryAdapter.getFilter().filter(newText);
                return true;
            }
        });*/


        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        updateLogOptions();

    }

    private void updateLogOptions() {
        TextView tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_user_name);
        TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_user_email);

        MenuItem logoutItem = navigationView.getMenu().findItem(R.id.nav_logout);
        MenuItem loginItem = navigationView.getMenu().findItem(R.id.nav_login);

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        //   boolean isUserLoggedIn = sharedPrefs.getBoolean("is_user_logged_in", false);
        if (!SessionHelper.isUserLoggedIn(MainActivity.this)) {
            loginItem.setVisible(true);
            logoutItem.setVisible(false);
            tvName.setText(R.string.app_name);
            tvEmail.setText("Good Food, Good Life.");
        } else {
            loginItem.setVisible(false);
            logoutItem.setVisible(true);

            try {
                JSONObject userObject = SessionHelper.getCurrentUser(MainActivity.this);
                String name = userObject.getString("u_name");
                String email = userObject.getString("u_email");
                tvName.setText(name);
                tvEmail.setText(email);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    private boolean isActivityStarted(Intent rateIntent) {
        try {
            startActivity(rateIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
*/

        if (id == R.id.action_cart) {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        }


        if (id == R.id.action_search_product) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_order_history) {
            if (!SessionHelper.isUserLoggedIn(MainActivity.this)) {
                Toast.makeText(MainActivity.this, "Please LOGIN For History", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_help_call) {
            String number = "03088688693";

            if (number.isEmpty()) {
                Toast.makeText(getApplicationContext(), "But you didn't dialed a number", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Permission to do bhai", Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 10);
                    }
                }
                startActivity(intent);
            }
        } else if (id == R.id.nav_rating) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.tutorial.personal.androidstudiopro"));
            if (!isActivityStarted(intent)) {
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.tutorial.personal.androidstudiopro"));
                if (!isActivityStarted(intent)) {
                    Toast.makeText(this, "Could not open Android market, please check if the market app installed or not. Try again later", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Hardees Application");
            intent.putExtra(Intent.EXTRA_TEXT, "Use Hardees App Really cool.  Link//");
            intent.setType("text/plain");
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(MainActivity.this, PermissionsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
/*
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            sharedPreferences.edit()
                    .remove("is_user_logged_in")
                    .remove("logged_user")
                    .apply();
*/
            SessionHelper.logout(MainActivity.this);
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            updateLogOptions();
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("destination", "finish");
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        slider.stopAutoCycle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        slider.stopAutoCycle();
    }

}