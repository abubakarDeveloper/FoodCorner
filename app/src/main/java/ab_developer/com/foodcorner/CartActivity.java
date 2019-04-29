package ab_developer.com.foodcorner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {


    RecyclerView rvCartItems;
    ArrayList<CartItem> cartList;
    CartAdapter cartAdapter;
    CartHelper cartHelper;
    TextView tvCartTotal;
    RelativeLayout checkoutLayout;
    CartItem cItem;
    //public int m_qty;

    String size;

    int size_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        rvCartItems = findViewById(R.id.rv_cartItems);
        tvCartTotal = findViewById(R.id.tv_cart_total);
        checkoutLayout = findViewById(R.id.checkout_layout);

        cartHelper = new CartHelper(CartActivity.this);

        cartList = cartHelper.getAllCartProducts();

        cartAdapter = new CartAdapter(cartList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Add Listener
                CartItem selectedCartItem = cartList.get(position);
                int qty = selectedCartItem.quantity;
               // m_qty = selectedCartItem.meat_quantity;
                qty++;
                selectedCartItem.quantity = qty;
                cartHelper.addOrUpdateToCart(selectedCartItem.p, qty);
                cartAdapter.notifyDataSetChanged();
                updateCartTotal();
            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Subtract Listener
                CartItem selectedCartItem = cartList.get(position);
                int qty = selectedCartItem.quantity;
                if (qty > 1) {
                    qty--;
                    selectedCartItem.quantity = qty;
                    cartHelper.addOrUpdateToCart(selectedCartItem.p, qty);
                    cartAdapter.notifyDataSetChanged();
                    updateCartTotal();
                }
            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Remove listener
                CartItem selectedCartItem = cartList.get(position);
                cartHelper.removeFromCart(selectedCartItem.p.productId);
                cartList.remove(position);
                cartAdapter.notifyItemRemoved(position);
                updateCartTotal();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
        rvCartItems.setLayoutManager(manager);
        rvCartItems.setAdapter(cartAdapter);

/*
        rvCartItems.addOnItemTouchListener(new SwipeOnItemTouchAdapter(this, rvCartItems, manager) {
            @Override
            public void onItemHiddenClick(SwipeHolder swipeHolder, int position) {
                Toast.makeText(CartActivity.this, "onItemHiddenClick " + position, Toast.LENGTH_SHORT).show();
                //call reset to hide.
                //or you will notified a onItemClick event when you click other area in this item which
                //may be not what your want.
                //or if you want click to delete you can add your logic here and don't necessarily need to call below.
                swipeHolder.reset();
            }

            @Override
            public void onItemClick(int position) {
                Toast.makeText(CartActivity.this, "onItemClick " + position, Toast.LENGTH_SHORT).show();
            }
        });*/

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // callback for drag-n-drop, false to skip this feature
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // callback for swipe to dismiss, removing item from data and adapter
                cartHelper.removeFromCart(cartList.get(viewHolder.getAdapterPosition()).p.productId);
                cartList.remove(viewHolder.getAdapterPosition());
                cartAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(rvCartItems);
        checkoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if user is logged in then show address activity
                //else take user to login activity
                //        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CartActivity.this);
                //      boolean isUserLoggedIn = sharedPrefs.getBoolean("is_user_logged_in", false);
                if (!SessionHelper.isUserLoggedIn(CartActivity.this)) {
                    Intent intent = new Intent(CartActivity.this, ChooseLoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                    startActivity(intent);
                }
            }
        });
        updateCartTotal();
    }

    private void updateCartTotal() {

/*        if(m_qty > 0) {
            int cartTotal = cartHelper.getCartExtraTotalAmount();
            tvCartTotal.setText("Rs. " + cartTotal);
        }else{*/
            int cartTotal = cartHelper.getCartTotalAmount();
            tvCartTotal.setText("Rs. " + cartTotal);
        //}
    }

}
