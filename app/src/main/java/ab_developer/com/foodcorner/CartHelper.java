package ab_developer.com.foodcorner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CartHelper extends SQLiteOpenHelper {

    public CartHelper(Context context) {
        super(context, "foodie_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Integer m_price = 30;
        //String query = "CREATE TABLE cart(p_id INTEGER primary key, p_name TEXT NOT NULL, p_price INTEGER NOT NULL, p_desc TEXT NOT NULL, p_image TEXT NOT NULL, cat_id INTEGER NOT NULL, quantity INTEGER NOT NULL)";
      //  String query = "CREATE TABLE cart(p_id INTEGER primary key, p_name TEXT NOT NULL, p_price INTEGER NOT NULL, p_desc TEXT NOT NULL, p_image TEXT NOT NULL, cat_id INTEGER NOT NULL, quantity INTEGER NOT NULL, meat_qty INTEGER DEFAULT NULL, m_price INTEGER DEFAULT NULL, pepsi_qty INTEGRE DEFAULT NULL, pepsi_price INTEGER DEFAULT NULL, size TEXT NOT NULL, size_price double)";
        String query = "CREATE TABLE cart(cart_id INTEGER primary key AUTOINCREMENT,p_id INTEGER, p_name TEXT NOT NULL, p_price INTEGER NOT NULL, p_desc TEXT NOT NULL, p_image TEXT NOT NULL, cat_id INTEGER NOT NULL, quantity INTEGER NOT NULL, size TEXT NOT NULL, size_price double NOT NULL, bottle TEXT, bottle_price int, petty TEXT, petty_price int, combo TEXT, combo_price int)";
/*
        String extra = "CREATE TABLE extra(extra_id INTEGER primary key, extra_name, extra_quantity, \n" +
                "  p_id INTEGER,\n" +
                "  CONSTRAINT fk_departments\n" +
                "    FOREIGN KEY (department_id)\n" +
                "    REFERENCES cart(p_id))";*/
        db.execSQL(query);
        /*
        String query1 = "CREATE table productOption(cart_id integer NOT NULL,p_id integer NOT NULL,option_id integer,option_name Text NOT NULL,option_price Integer NOT NULL)";
        db.execSQL(query1);
*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cart");
        onCreate(db);
    }

public void addOrUpdateToCart(Product product, int quantity){
    SQLiteDatabase db = getWritableDatabase();
    if(isProductInCart(product.productId)){
        String query = "Update cart set quantity=" + quantity + " WHERE p_id=" + product.productId;
        db.execSQL(query);
    }else{
        ContentValues CV = new ContentValues();
        CV.put("p_id", product.productId);
        CV.put("p_image", product.imageLink);
        CV.put("p_name", product.name);
        CV.put("p_desc", product.desc);
        CV.put("p_price", product.price);
        CV.put("cat_id", product.catId);
        CV.put("quantity", quantity);
        db.insert("cart",null,CV);
    }
    db.close();
}
/*
    public void addOrUpdateToCartExtra(Product product, int quantity, Integer meat_quantity, Integer meat_price, Integer pepsi_qty, Integer pepsi_price, String size, Integer size_price){
        SQLiteDatabase db = getWritableDatabase();
        if(isProductInCart(product.productId)){
           // String query = "Update cart set quantity=" + quantity + ",meat_qty="+ meat_quantity +", m_price=" + meat_price + ", pepsi_qty=" + pepsi_qty +", pepsi_price=" + pepsi_price +", size_price="+size_price+", size="+ size+ " WHERE p_id=" + product.productId;
            String query = "Update cart set quantity=" + quantity + ", size_price="+size_price+", size="+ size+ " WHERE p_id=" + product.productId;
            db.execSQL(query);
        }else{
            ContentValues CV = new ContentValues();
            CV.put("p_id", product.productId);
            CV.put("p_image", product.imageLink);
            CV.put("p_name", product.name);
            CV.put("p_desc", product.desc);
            CV.put("p_price", product.price);
            CV.put("cat_id", product.catId);
            CV.put("quantity", quantity);
            */
/*
            CV.put("meat_qty", meat_quantity);
            CV.put("m_price", (Integer) meat_price);
            CV.put("pepsi_qty", pepsi_qty);
            CV.put("pepsi_price", pepsi_price);*//*

            CV.put("size", size);
            CV.put("size_price", size_price);
            db.insert("cart",null,CV);
        }
        db.close();
    }
*/
    public void addOrUpdateToCartOption(Product product, int quantity, String size, double size_price, String bottle, int bottle_price, String petty, int petty_price, String combo_item, int combo_price){
        SQLiteDatabase db = getWritableDatabase();
        if(isProductItemInCart(product.productId)){
            ContentValues CV = new ContentValues();
            CV.put("p_id", product.productId);
            CV.put("p_image", product.imageLink);
            CV.put("p_name", product.name);
            CV.put("p_desc", product.desc);
            CV.put("p_price", product.price);
            CV.put("cat_id", product.catId);
            CV.put("quantity", quantity);
            /*
            CV.put("meat_qty", meat_quantity);
            CV.put("m_price", (Integer) meat_price);
            CV.put("pepsi_qty", pepsi_qty);
            CV.put("pepsi_price", pepsi_price);*/
            CV.put("size", size);
            CV.put("size_price", size_price);
            CV.put("bottle", bottle);
            CV.put("bottle_price", bottle_price);
            CV.put("petty", petty);
            CV.put("petty_price", petty_price);
            CV.put("combo", combo_item);
            CV.put("combo_price", combo_price);
            db.insert("cart",null,CV);

            // String query = "Update cart set quantity=" + quantity + ",meat_qty="+ meat_quantity +", m_price=" + meat_price + ", pepsi_qty=" + pepsi_qty +", pepsi_price=" + pepsi_price +", size_price="+size_price+", size="+ size+ " WHERE p_id=" + product.productId;
        }else{
            String query = "Update cart set quantity=" + quantity + ", size_price="+size_price+", size='"+ size+ "', bottle='"+ bottle +"', bottle_price="+ bottle_price +", petty='" + petty +"', petty_price="+ petty_price +", combo='"+ combo_item +"', combo_price=" + combo_price+" WHERE p_id=" + product.productId;
            db.execSQL(query);
        }
        db.close();
    }
    public boolean isProductInCart (int productId){
        //  boolean alreadyInCart = false;
        String query = "SELECT * FROM cart WHERE p_id=" + productId;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() > 0 ){
            c.close();
            return true;
        }else {
            c.close();
            return false;
        }
    }
    public boolean isProductItemInCart (int productId){
        //  boolean alreadyInCart = false;
        String query = "SELECT * FROM cart WHERE p_id=" + productId + " AND petty='false' AND combo='false' AND bottle='false' AND size='false'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() > 0 ){
            c.close();
            return true;
        }else {
            c.close();
            return false;
        }
    }
    public void removeFromCart(int productId){
        String query = "DELETE FROM cart WHERE p_id=" + productId;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }
    public void clearCart(){
        String query = "DELETE FROM cart";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
/*        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart", null, null);*/
    }

    public ArrayList<CartItem>getAllCartProducts(){
        ArrayList<CartItem> cartList = new ArrayList<>();
        String query = "SELECT * FROM cart";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        while(c.moveToNext()){
            CartItem cartItem = new CartItem();
            Product p = new Product();
            int quantity;
            int meat_quantity;
            int meat_price;

            String size;
            int size_price;
            String bottle;
            int bottle_price;
            String petty;
            int petty_price;
            String combo;
            int combo_price;

                p.productId = c.getInt(c.getColumnIndex("p_id"));
                p.name = c.getString(c.getColumnIndex("p_name"));
                p.imageLink = c.getString(c.getColumnIndex("p_image"));
                p.desc = c.getString(c.getColumnIndex("p_desc"));
                p.price = c.getInt(c.getColumnIndex("p_price"));
                p.catId = c.getInt(c.getColumnIndex("cat_id"));
                quantity = c.getInt(c.getColumnIndex("quantity"));
                cartItem.cartId = c.getInt(c.getColumnIndex("cart_id"));
                /*meat_quantity = c.getInt(c.getColumnIndex("meat_qty"));
                meat_price = c.getInt(c.getColumnIndex("m_price"));
              */
                bottle = c.getString(c.getColumnIndex("bottle"));
                bottle_price = c.getInt(c.getColumnIndex("bottle_price"));
                petty = c.getString(c.getColumnIndex("petty"));
                petty_price = c.getInt(c.getColumnIndex("petty_price"));
                combo = c.getString(c.getColumnIndex("combo"));
                combo_price = c.getInt(c.getColumnIndex("combo_price"));
                size = c.getString(c.getColumnIndex("size"));
                size_price = c.getInt(c.getColumnIndex("size_price"));

                cartItem.p = p;
                cartItem.quantity = quantity;

                cartItem.size = size;
                cartItem.size_price = size_price;
                cartItem.bottle = bottle;
                cartItem.bottle_price = bottle_price;
                cartItem.petty = petty;
                cartItem.petty_price = petty_price;
                cartItem.combo = combo;
                cartItem.combo_price = combo_price;
                /*cartItem.meat_quantity = meat_quantity;
                cartItem.meat_price = meat_price;
                */
                cartList.add(cartItem);


        }
        c.close();
        db.close();
        return cartList;
    }
    public int getCartCount(){
        String query = "SELECT * FROM cart";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        int count = c.getCount();
        c.close();
        return count;
    }
    public int getCartTotalAmount(){
        String query = "SELECT SUM(quantity * (p_price + petty_price + bottle_price + combo_price)) as total_amount FROM cart";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c.getCount() == 0){
            c.close();
            return 0;
        }else {
            c.moveToFirst();
            int totalAmount = c.getInt(c.getColumnIndex("total_amount"));
            c.close();
            return  totalAmount;
        }
    }
    public int getCartExtraTotalAmount(){
        String query = "SELECT SUM(quantity * (p_price * size_price)) as total_amount FROM cart";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c.getCount() == 0){
            c.close();
            return 0;
        }else {
            c.moveToFirst();
            int totalAmount = c.getInt(c.getColumnIndex("total_amount"));
            c.close();
            return  totalAmount;
        }
    }
    public int  getProductQuantity(int productId){

        String query = "SELECT quantity FROM cart WHERE p_id=" + productId;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() == 0){
            c.close();
            return 0;
        }else {
            c.moveToFirst();
            int qty = c.getInt(c.getColumnIndex("quantity"));
            c.close();
            return qty;
        }
    }

    public int  getMeatQuantity(int productId){

        String query = "SELECT meat_qty FROM cart WHERE p_id=" + productId;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() == 0){
            c.close();
            return 0;
        }else {
            c.moveToFirst();
            int m_qty = c.getInt(c.getColumnIndex("meat_qty"));
            c.close();
            return m_qty;
        }
    }
    public int  getPepsiQuantity(int productId){

        String query = "SELECT pepsi_qty FROM cart WHERE p_id=" + productId;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() == 0){
            c.close();
            return 0;
        }else {
            c.moveToFirst();
            int pepsi_qty = c.getInt(c.getColumnIndex("pepsi_qty"));
            c.close();
            return pepsi_qty;
        }
    }
    public int  getSize(int productId){

        String query = "SELECT size FROM cart WHERE p_id=" + productId;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() == 0){
            c.close();
            return 0;
        }else {
            c.moveToFirst();
            int size = c.getInt(c.getColumnIndex("size"));
            c.close();
            return size;
        }
    }
}
