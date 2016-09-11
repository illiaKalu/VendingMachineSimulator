package com.dev.illiaka.Utils;

import com.dev.illiaka.ProductsController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonicmaster on 11.09.16.
 */
public class JSONParser {

    public static ArrayList<ProductsController> parseJson(String jsonString){

        ArrayList arr = new ArrayList<ProductsController>(5);

        arr.add(new ProductsController("cola 3", 1.25d, 3));
        arr.add(new ProductsController("fanta 1", 1.01d, 1));
        arr.add(new ProductsController("pepsi 2", 5d, 2));
        arr.add(new ProductsController("sprite0", 4.26d, 0));
        arr.add(new ProductsController("mirinda5", 3.45d, 5));

        return arr;
    }
}
