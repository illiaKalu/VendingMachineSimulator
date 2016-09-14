package com.dev.illiaka.Utils;

import com.dev.illiaka.Wallet;

/**
 * Created by sonicmaster on 12.09.16.
 * class calculating change for customer
 */
public class ChangeCalculator {


    /**
     * @param change
     * @param denominationsAmount
     * @return boolean
     */
    public boolean canGiveChange(double change, int[] denominationsAmount) {

        // each cell represent available denomination
        // eg. [0] - 5 denomination
        // [1] - 2 denomination
        // [5] - 0.1 denomination
        int[] givenDenominations = new int[6];

        int[] denominations = new int[]{500, 200, 100, 50, 20, 10};

        // 4.3$ = 43
        // dollars to cents
        change *= 100;

        int changeInPennies = (int) change;

        // check every denomination machine have to count how much bills of every denominations needed
        for (int i = 0; i < denominations.length; i++) {
            int count = Math.min(changeInPennies / denominations[i], denominationsAmount[i]);
            changeInPennies -= denominations[i] * count;
            givenDenominations[i] = count;
        }

        // take denominations that were given to customer from machine wallet
        if (changeInPennies == 0) {
            Wallet.getInstance().subDenominations(givenDenominations);
            return true;
        } else {
            return false;
        }

    }
}
