package com.dev.illiaka.Utils;

import com.dev.illiaka.ProductsController;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonicmaster on 11.09.16.
 * class parse JSON and returns arrayList of products and denomitnations array
 *
 * @see ProductsController
 */
public class JSONParser {

    /**
     * @param jsonString
     * @return ArrayList with products from JSON
     */
    public static ArrayList<ProductsController> getProductsArrayList(String jsonString) {

        ArrayList productsListToReturn = new ArrayList<ProductsController>(5);

        JSONObject rootJSONObj = new JSONObject(jsonString);

        JSONArray productsArray = rootJSONObj.getJSONArray("products");

        for (int i = 0; i < productsArray.length(); i++) {
            productsListToReturn.add(new ProductsController(productsArray.getJSONObject(i).get("type").toString(),
                    productsArray.getJSONObject(i).optDouble("price"),
                    productsArray.getJSONObject(i).optInt("amount", 0)));
        }

        return productsListToReturn;
    }

    /**
     * @param jsonString
     * @return integer array with denominations from JSON
     * indexes of denominations and amount of denominations are shown below
     * [0] - 5$
     * [1] - 2$
     * [2] - 1$
     * [3] - 0.5$
     * [4] - 0.2$
     * [5] - 0.1&
     */
    public static int[] getDenominationsArray(String jsonString) {

        int[] denominationToReturn = new int[6];

        JSONObject rootJSONObj = new JSONObject(jsonString);

        JSONArray denominationsArray = rootJSONObj.getJSONArray("denominations");

        for (int i = 0; i < denominationToReturn.length; i++) {
            denominationToReturn[i] = denominationsArray.optInt(i, 0);
        }
        return denominationToReturn;
    }
}
