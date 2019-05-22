package ab_developer.com.foodcorner;

import java.io.Serializable;

public class CartItem implements Serializable {
    Product p;
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
    int cartId;
}
