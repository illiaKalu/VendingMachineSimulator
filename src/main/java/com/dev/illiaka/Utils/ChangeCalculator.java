package com.dev.illiaka.Utils;

import com.dev.illiaka.Wallet;

/**
 * Created by sonicmaster on 12.09.16.
 */
public class ChangeCalculator {


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

        for (int i = 0; i < denominations.length; i++) {
            int count = Math.min(changeInPennies / denominations[i], denominationsAmount[i]);
            changeInPennies -= denominations[i] * count;
            givenDenominations[i] = count;
        }

        if (changeInPennies == 0) {
            Wallet.getInstance().subDenominations(givenDenominations);
            return true;
        } else {
            return false;
        }

    }
}
