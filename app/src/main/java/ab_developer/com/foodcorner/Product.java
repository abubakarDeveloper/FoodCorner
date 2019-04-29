package ab_developer.com.foodcorner;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    int productId;
    String name;
    int price;
    String desc;
    String imageLink;
    int catId;
    int deal;
    float pRating;
    int extraId;
    String extrName;
    int extraPrice;
}
