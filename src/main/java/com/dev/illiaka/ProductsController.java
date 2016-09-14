package com.dev.illiaka;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Created by sonicmaster on 11.09.16.
 * class represents product with its fields and methods
 * It is also extends HBox for be able to fill listView
 *
 * @see javafx.scene.layout.HBox
 * @see javafx.scene.control.ListView
 */

public class ProductsController extends HBox {

    private Label productTypeLabel = new Label();
    private Label productPriceLabel = new Label();


    private String productType;
    private Double productPrice;
    private int productAmount;

    public ProductsController(String productType, Double productPrice, int productAmount) {
        super();

        productTypeLabel.setText(productType);
        productTypeLabel.setMaxWidth(Double.MAX_VALUE);

        productPriceLabel.setText("" + productPrice);
        productPriceLabel.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(productTypeLabel, Priority.ALWAYS);

        this.productType = productType;
        this.productPrice = productPrice;
        this.productAmount = productAmount;


        this.getChildren().addAll(productTypeLabel, productPriceLabel);
    }

    public int getProductAmount() {
        return this.productAmount;
    }

    public void setProductAmount(int amount) {
        this.productAmount = amount;
    }

    public String getProductType() {
        return productType;
    }

    public Double getProductPrice() {
        return productPrice;
    }

}
