package com.dev.illiaka;

/**
 * Created by sonicmaster on 09.09.16.
 */
public class Wallet {

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public static void setInstance(Wallet instance) {
        Wallet.instance = instance;
    }

    private Double money;

    private static volatile Wallet instance;

    private Wallet() {}

    public static synchronized Wallet getInstance(){
        if (instance == null) instance = new Wallet();
        return instance;
    }


}
