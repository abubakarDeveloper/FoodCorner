package ab_developer.com.foodcorner;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Option implements Serializable {

    int option_id;
    String option_name;
    int p_id;

    Value selectedValue;

    @SerializedName("values")
    ArrayList<Value> valuesList;
}
