package com.dev.illiaka;

import com.sun.deploy.util.ArrayUtil;

/**
 * Created by sonicmaster on 09.09.16.
 */
public class Wallet {

    private static volatile Wallet instance;
    private int[] denominations = new int[6];
    private Double money;

    private Wallet() {
    }

    public static synchronized Wallet getInstance() {
        if (instance == null) instance = new Wallet();
        return instance;
    }

    public int[] getDenominations() {
        return denominations;
    }

    public void setDenominations(int[] denominations) {
        this.denominations = denominations;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void init(int[] denominations) {
        this.denominations = denominations;
    }

    public void addDenominations(int[] tempUserMoneyInsertion) {
        for (int i = 0; i < tempUserMoneyInsertion.length; i++) {
            denominations[i] += tempUserMoneyInsertion[i];
        }
    }

    public void subDenominations(int[] tempUserMoneyInsertion) {
        for (int i = 0; i < tempUserMoneyInsertion.length; i++) {
            denominations[i] -= tempUserMoneyInsertion[i];
        }
    }
}
