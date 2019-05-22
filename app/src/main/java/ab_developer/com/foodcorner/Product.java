package ab_developer.com.foodcorner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    @SerializedName("p_id")
    int productId;
    @SerializedName("p_name")
    String name;
    @SerializedName("p_price")
    int price;
    @SerializedName("p_desc")
    String desc;
    @SerializedName("p_image")
    String imageLink;
    @SerializedName("cat_id")
    int catId;
    @SerializedName("p_deal")
    int deal;
    @SerializedName("p_extra")
    int extra;
    //float pRating;

    @SerializedName("options")
    ArrayList<Option> optionsList;
}
