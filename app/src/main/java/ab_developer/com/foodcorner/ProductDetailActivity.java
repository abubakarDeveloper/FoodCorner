package ab_developer.com.foodcorner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import yanzhikai.textpath.SyncTextPathView;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView ivProductImage;
    TextView tvName;
    TextView tvPrice;
    TextView tvDesc;

    Button btnAdd;
    Button btnRemove;
    TextView tvQuantity;
    Button btnAddToCart;
    Product selectedProduct;
    CartHelper cartHelper;

    MaterialRatingBar mrbRating;
    Button btnAddMeat;
    Button btnRemoveMeat;
    TextView tvMeat;
    Button btnAddToCartMeat;
    SessionHelper sessionHelper;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;


    SyncTextPathView stpv_2017;
    Integer meat_price;
    Integer pepsi_price;
    RelativeLayout rl_extra;

    Button btnAddPepsi;
    Button btnRemovePepsi;
    TextView tvPepsi;
    RecyclerView rvExtra;

    RadioButton rdoMedium, rdoSmall, rdoLarge, rdoCard;
    RadioGroup rgPayment;

    RadioButton rdoSinglePetty, rdoDoublePetty;
    RadioGroup rgOption;

    CheckBox cbWithCombo;

    RadioGroup rgBottle;
    RadioButton  cbCoke, cbPepsi, cbFanta;

    RadioGroup rlComboOption;
    RadioButton cbOnionRings, cbPlaneFries, cbCurlyFries;

    String size = "";
    int rdPrice = 0;

    String choosePetty = "";
    int pettyPrice = 0;

    String comboName = "";
    int comboPrice = 0;
    String bottleName = "";
    int bottlePrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ivProductImage = findViewById(R.id.iv_product_image);
        tvName = findViewById(R.id.tv_product_name);
        tvPrice = findViewById(R.id.tv_product_price);
        tvDesc = findViewById(R.id.tv_product_desc);

        btnRemove = findViewById(R.id.btn_remove);
        tvQuantity = findViewById(R.id.tv_quantity);
        btnAdd = findViewById(R.id.btn_add);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        // mrbRating = findViewById(R.id.mrb_rating);

        rl_extra = findViewById(R.id.rl_extra);
        btnAddMeat = findViewById(R.id.btn_add_meat);
        tvMeat = findViewById(R.id.tv_meat);
        btnRemoveMeat = findViewById(R.id.btn_remove_meat);

        btnAddPepsi = findViewById(R.id.btn_add_pepsi);
        tvPepsi = findViewById(R.id.tv_pepsi);
        btnRemovePepsi = findViewById(R.id.btn_remove_pepsi);
       // rvExtra = findViewById(R.id.rv_extra);
        stpv_2017 = findViewById(R.id.stpv_2017);
        stpv_2017.startAnimation(1, 0);

        rgPayment = findViewById(R.id.rg_payment);
        rdoLarge = findViewById(R.id.rdo_cod);
        rdoSmall = findViewById(R.id.rdo_jazzcash);
        rdoMedium = findViewById(R.id.rdo_easypaisa);

        rgOption = findViewById(R.id.rg_option);
        rdoSinglePetty = findViewById(R.id.rdo_single_petty);
        rdoDoublePetty = findViewById(R.id.rdo_double_petty);

        rgBottle = findViewById(R.id.rg_bottle);
        cbCoke = findViewById(R.id.cb_coke);
        cbPepsi = findViewById(R.id.cb_pepsi);
        cbFanta = findViewById(R.id.cb_fanta);

        rlComboOption = findViewById(R.id.rg_combo);

        cbWithCombo = findViewById(R.id.cb_with_combo);
        cbCurlyFries = findViewById(R.id.cb_curly_fries);
        cbOnionRings = findViewById(R.id.cb_onion_rings);
        cbPlaneFries = findViewById(R.id.cb_plane_fries);
        //  btnAddToCartMeat = findViewById(R.id.btn_add_to_cart_meat);

        //radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        Bundle bundle = getIntent().getExtras();

        selectedProduct = (Product) bundle.getSerializable("product");
//        final Product extra = (Product) bundle.getSerializable("extraProduct");

        // get the selected RadioButton of the group
        Picasso.with(ProductDetailActivity.this)
                .load(selectedProduct.imageLink)
                .into(ivProductImage);

        tvName.setText(selectedProduct.name);
        tvPrice.setText("Rs." + selectedProduct.price);
        tvDesc.setText(selectedProduct.desc);
//        mrbRating.setRating(selectedProduct.pRating);

/*
        if (selectedProduct.catId == 2) {
            rl_extra.setVisibility(View.VISIBLE);
        } else {
            rl_extra.setVisibility(View.INVISIBLE);
        }
*/


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty =  Integer.parseInt(tvQuantity.getText().toString());
                qty++;
                tvQuantity.setText(String.valueOf(qty));
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(tvQuantity.getText().toString());
                if(qty > 1){
                    qty--;
                    tvQuantity.setText(String.valueOf(qty));
                }
            }
        });
//button coe for meat insertion
        btnAddMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(tvMeat.getText().toString());
                qty++;
                tvMeat.setText(String.valueOf(qty));
            }
        });

        btnRemoveMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(tvMeat.getText().toString());
                if(qty > 0)
                    qty--;
                    tvMeat.setText(String.valueOf(qty));
            }
        });
        btnAddPepsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pepsi_qty = Integer.parseInt(tvPepsi.getText().toString());
                    pepsi_qty++;
                    tvPepsi.setText(String.valueOf(pepsi_qty));
            }
        });
        btnRemovePepsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pepsi_qty = Integer.parseInt(tvPepsi.getText().toString());
                if(pepsi_qty > 0 )
                    pepsi_qty--;
                tvPepsi.setText(String.valueOf(pepsi_qty));
            }
        });
        cartHelper = new CartHelper(ProductDetailActivity.this);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                
                int qty = Integer.parseInt(tvQuantity.getText().toString());
                size = "";
                rdPrice = 0;

                if (rdoLarge.isChecked()) {
                    Toast.makeText(ProductDetailActivity.this, "Large", Toast.LENGTH_LONG).show();
                    size = "Large";
                    rdPrice= 1 ;
                    //rdPrice = 1;
                } else if (rdoMedium.isChecked()){
                    Toast.makeText(ProductDetailActivity.this, "Large", Toast.LENGTH_LONG).show();
                    size = "Medium";
                    rdPrice = (int) 0.25;
                }else if (rdoSmall.isChecked()) {
                    Toast.makeText(ProductDetailActivity.this, "Small", Toast.LENGTH_LONG).show();
                    size = "Small";
                    rdPrice = (int) 0.50;
                }else{
                    size = "false";
                    rdPrice = 1;
                }

                choosePetty = "";
                pettyPrice = 0;

                if(rdoSinglePetty.isChecked()){
                    choosePetty = rdoSinglePetty.getText().toString();
                    pettyPrice = 120;
                }else if(rdoDoublePetty.isChecked()){
                    choosePetty = rdoDoublePetty.getText().toString();
                    pettyPrice = 150;
                }else{
                    choosePetty = "false";
                    pettyPrice = 1;
                }

                bottleName = "";
                bottlePrice = 0;
                if(cbPepsi.isChecked()){
                    bottleName = cbPepsi.getText().toString();
                    bottlePrice = 30;
                }else if(cbCoke.isChecked()){
                    bottleName = cbCoke.getText().toString();
                    bottlePrice = 30;
                }else if(cbFanta.isChecked()){
                    bottleName = cbFanta.getText().toString();
                    bottlePrice = 30;
                }else{
                    bottleName = "false";
                    bottlePrice = 1;
                }

                comboName = "";
                comboPrice = 0;

                if(cbWithCombo.isChecked()){
                    //   rlComboOption.setVisibility(View.VISIBLE);
                    comboName = "false";
                    comboPrice = 1;
                }else {
                    if (cbPlaneFries.isChecked()) {
                        comboName = cbPlaneFries.getText().toString();
                        comboPrice = 120;
                    } else if (cbOnionRings.isChecked()) {
                        comboName = cbOnionRings.getText().toString();
                        comboPrice = 140;
                    } else if (cbCurlyFries.isChecked()) {
                        comboName = cbCurlyFries.getText().toString();
                        comboPrice = 170;
                    }else{
                        comboName = "false";
                        comboPrice = 1;
                    }
                }
/*
                int meat_quantity = Integer.parseInt(tvMeat.getText().toString());
                int pepsi_qty = Integer.parseInt(tvPepsi.getText().toString());
                if(meat_quantity > 0 && pepsi_qty > 0){
                    meat_price = 20;
                    pepsi_price = 30;
                }else
                {

                    meat_price = null;
                    pepsi_price = null;
                }
                if(meat_quantity > 0){
*/
                    cartHelper.addOrUpdateToCartOption(selectedProduct, qty, size, rdPrice,bottleName, bottlePrice, choosePetty, pettyPrice, comboName, comboPrice);
    //                cartHelper.addOrUpdateToCartExtra(selectedProduct, qty, meat_quantity, meat_price, pepsi_qty, pepsi_price, size, rdPrice);
                    updateCartButton();
            //        updateCartButtonForMeat();
                    Toast.makeText(ProductDetailActivity.this, "Extra's Added to Cart successfully", Toast.LENGTH_SHORT).show();
/*
                }else {

                    cartHelper.addOrUpdateToCart(selectedProduct, qty);
                    updateCartButton();
                    Toast.makeText(ProductDetailActivity.this, "Added to Cart successfully", Toast.LENGTH_SHORT).show();
                }
*/
            }
        });
  /*      //Addcart for meat
        btnAddToCartMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder( ProductDetailActivity.this);
                builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.journaldev.com/"));
                PendingIntent pendingIntent = PendingIntent.getActivity( ProductDetailActivity.this, 0, intent, 0);
                builder.setContentIntent(pendingIntent);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                builder.setContentTitle("Notifications Title");
                builder.setContentText("Your notification content here.");
                builder.setSubText("Tap to view the website.");

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Will display the notification in the notification bar
                notificationManager.notify(1, builder.build());

             *//*
                int qty = Integer.parseInt(tvMeat.getText().toString());

                cartHelper.addOrUpdateToCart(selectedProduct, qty);
                updateCartButtonForMeat();
                Toast.makeText(ProductDetailActivity.this, "Added to Cart successfully", Toast.LENGTH_SHORT).show();*//*
            }
        });
*/
        /*updateCartButtonForMeat();
        updateCartButtonForPepsi();*/
        updateCartButton();
    }
    private void updateCartButton(){
        int qty = cartHelper.getProductQuantity(selectedProduct.productId);
/*
        int m_qty = cartHelper.getMeatQuantity(selectedProduct.productId);
        int pepsi_qty = cartHelper.getPepsiQuantity(selectedProduct.productId);
*/

        if(qty > 0){
            tvQuantity.setText(String.valueOf(qty));
            btnAddToCart.setText("Update");
        }else{
            tvQuantity.setText("1");
            btnAddToCart.setText("Add To Cart");
        }

/*
        if(qty > 0 && m_qty > 0){
            tvQuantity.setText(String.valueOf(qty));
            tvMeat.setText(String.valueOf(m_qty));
            btnAddToCart.setText("Update");
            // btnAddToCartMeat.setText("Update");
        }else{
            tvMeat.setText("0");
            //btnAddToCartMeat.setText("Add To Cart");
        }
        if(qty > 0 && pepsi_qty > 0){
            tvQuantity.setText(String.valueOf(qty));
            tvPepsi.setText(String.valueOf(pepsi_qty));
            btnAddToCart.setText("Update");
        }else{
            tvPepsi.setText("0");
        }
        if(m_qty > 0 && pepsi_qty > 0){
            tvMeat.setText(String.valueOf(m_qty));
            tvPepsi.setText(String.valueOf(pepsi_qty));
            btnAddToCart.setText("Update");
        }else{
            tvPepsi.setText("0");
            tvMeat.setText("0");
        }
        if(qty > 0 && m_qty > 0 && pepsi_qty > 0){
            tvQuantity.setText(String.valueOf(qty));
            tvMeat.setText(String.valueOf(m_qty));
            tvPepsi.setText(String.valueOf(pepsi_qty));
            btnAddToCart.setText("Update");
        }
        else{
            tvMeat.setText("0");
            tvPepsi.setText("0");
        }
*/
    }

/*
//Update button for meat
    private void updateCartButtonForMeat(){
        int m_qty = cartHelper.getMeatQuantity(selectedProduct.productId);
        if(m_qty > 0){
            tvMeat.setText(String.valueOf(m_qty));
            btnAddToCart.setText("Update");
           // btnAddToCartMeat.setText("Update");
        }else{
            tvMeat.setText("0");
            btnAddToCart.setText("Add To Cart");
            //btnAddToCartMeat.setText("Add To Cart");
        }
    }
    private void updateCartButtonForPepsi(){
        int pepsi_qty = cartHelper.getPepsiQuantity(selectedProduct.productId);
        if(pepsi_qty > 0){
            tvPepsi.setText(String.valueOf(pepsi_qty));
            btnAddToCart.setText("Update");
            // btnAddToCartMeat.setText("Update");
        }else{
            tvPepsi.setText("0");
            btnAddToCart.setText("Add To Cart");
            //btnAddToCartMeat.setText("Add To Cart");
        }
    }
*/
}
