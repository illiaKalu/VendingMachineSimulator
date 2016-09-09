package com.dev.illiaka.Tests;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sonicmaster on 09.09.16.
 */
public class FakeData {

    static Random r = new Random(20);

    public static int [] fakeDenominations(){

        int[] fakeDenominationsArray = new int[6];

        for (int i = 0; i < fakeDenominationsArray.length; i++) {
            fakeDenominationsArray[i] = r.nextInt(20);
        }
        return fakeDenominationsArray;

    }

    public static ArrayList<Product> fakeProducts(){

        ArrayList<Product> products = new ArrayList<Product>(45);

        for (int i = 0; i < 45; i ++){

            products.add(new Product("Cola dring", "" + r.nextDouble(), "" + r.nextInt()));

        }

        return products;
    }
}
