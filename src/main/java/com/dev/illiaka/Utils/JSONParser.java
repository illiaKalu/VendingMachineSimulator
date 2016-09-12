package com.dev.illiaka.Utils;

import com.dev.illiaka.ProductsController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonicmaster on 11.09.16.
 */
public class JSONParser {


    /**
     * @param jsonString
     * @return ArrayList with products from JSON
     */
    public static ArrayList<ProductsController> getProductsArrayList(String jsonString) {

        ArrayList arr = new ArrayList<ProductsController>(5);

        arr.add(new ProductsController("cola 3", 1.2d, 3));
        arr.add(new ProductsController("fanta 1", 1.1d, 1));
        arr.add(new ProductsController("pepsi 2", 5d, 2));
        arr.add(new ProductsController("sprite0", 4.3d, 0));
        arr.add(new ProductsController("mirinda5", 3.8d, 5));

        return arr;
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
        return new int[]{0, 0, 0, 0, 1, 1};
    }
}
