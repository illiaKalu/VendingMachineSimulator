package com.dev.illiaka;

import com.dev.illiaka.Tests.FakeData;
import com.dev.illiaka.Tests.Product;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonicmaster on 09.09.16.
 */
public class SimulatorScene extends Application {

    private static final String DOMINATION_IS_NOT_CORRECT = "inserted value is not correct!";
    private static final String PICK_ITEM_FIRST = "pick item first!";
    private static final String SUCCESS_NO_CHANGE = "Thank you, take your product";
    private static final String PRIMARY_STAGE_TITLE = "Vending Machine Simulator";
    ListView<HBoxCell> listView;
    Button cancelButton;
    Button insertButton;
    TextField insertedMoneyTextField;
    Label messageLabel;
    Label pickedItemTypeLabel;
    Label pickedItemPriceLabel;
    Label insertedMoneyLabel;
    Label neededMoneyLabel;
    Label changeLabel;
    // each cell represent available denomination
    // eg [0] - 5 denomination
    // [1] - 2 denomination
    // [5] - 0.1 denomination
    int[] tempUserMoneyInsertion = new int[6];

    Double insertedMoneyValue;
    Double neededMoneyValue;

    public static void main(String[] args) {

        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        // primary Scene implementation
        // markup file location
        String fxmlSceneMarkupFile = "/scenes/primaryScene.fxml";

        // loads scene from fxml file in resources
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlSceneMarkupFile));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle(PRIMARY_STAGE_TITLE);


        primaryStage.show();

        findControls(scene);

        populateProductsIntoListView();


        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBoxCell>() {
            public void changed(ObservableValue<? extends HBoxCell> observable, HBoxCell oldValue, HBoxCell newValue) {

                pickedItemTypeLabel.setText(newValue.productType.getText());
                pickedItemPriceLabel.setText(newValue.productPrice.getText());

                // activate buttons
                insertButton.setDisable(false);
                cancelButton.setDisable(false);

                // clear message label
                messageLabel.setText("");
            }
        });

        // button actions
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                // return inserted money
                pickedItemTypeLabel.setText("");
                pickedItemPriceLabel.setText("");

            }
        });

        insertButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {

                // clear error message
                messageLabel.setText("");

                String insertedMoney = insertedMoneyTextField.getText();

                // check is any product chosen
                if (isProductChosen()) {

                    // check if inserted value is correct
                    if (checkIsDenominationCorrect(insertedMoney)) {

                        setInsertedMoneyLabel(insertedMoney);


                        switch (insertedMoney) {

                            case "5":
                                System.out.println("5");
                                tempUserMoneyInsertion[0] += 1;
                                break;
                            case "2":
                                System.out.println("2");
                                tempUserMoneyInsertion[1] += 1;
                                break;
                            case "1":
                                System.out.println("1");
                                tempUserMoneyInsertion[2] += 1;
                                break;
                            case "0.5":
                                System.out.println("0.5");
                                tempUserMoneyInsertion[3] += 1;
                                break;
                            case "0.2":
                                System.out.println("0.2");
                                tempUserMoneyInsertion[4] += 1;
                                break;
                            case "0.1":
                                System.out.println("0.1");
                                tempUserMoneyInsertion[5] += 1;
                                break;
                            default:
                                messageLabel.setText(DOMINATION_IS_NOT_CORRECT);
                        }

                        // calculate how much money user need to add and set responsible Label
                        setneededMoneyLabel(insertedMoney);


                        // check is inserted money enough
                        // 0 - no change, give product
                        // 1 - user inserted not enough money
                        // -1 - with change
                        switch (isMoneyEnough()) {

                            case 1:
                                System.out.println("not enough");
                                break;
                            case 0:

                                // reset fields and labels for next user
                                insertedMoneyValue = 0d;
                                neededMoneyValue = 0d;

                                insertedMoneyLabel.setText("0");
                                neededMoneyLabel.setText("");


                                // no change, give product, make buttons states react properly
                                messageLabel.setText(SUCCESS_NO_CHANGE);

                                cancelButton.setDisable(true);
                                insertButton.setDisable(true);
                                break;
                            case -1:
                                System.out.println("give change");
                        }


                    } else {
                        messageLabel.setText(DOMINATION_IS_NOT_CORRECT);
                    }

                } else {
                    messageLabel.setText(PICK_ITEM_FIRST);
                }

            }
        });

    }

    // get previous value of inserted money label and add to it new value
    private void setInsertedMoneyLabel(String insertedMoneyString) {
        insertedMoneyValue = Double.parseDouble(insertedMoneyLabel.getText()) +
                Double.parseDouble(insertedMoneyString);

        insertedMoneyLabel.setText(String.format("%.2f", insertedMoneyValue));

    }

    private void setneededMoneyLabel(String insertedMoneyString) {

        // if user inserted first denomination
        // get product price and sub inserted money
        if ("".equals(neededMoneyLabel.getText())) {

            neededMoneyValue = Double.parseDouble(pickedItemPriceLabel.getText().replace('$', Character.MIN_VALUE))
                    - Double.parseDouble(insertedMoneyString);

            neededMoneyLabel.setText(String.format("%.2f", neededMoneyValue));
        } else {

            //else - get previous value and sub inserted money from it
            neededMoneyValue = Double.parseDouble(neededMoneyLabel.getText()) - Double.parseDouble(insertedMoneyString);

            // if there is enough money - set responsible label
            if (neededMoneyValue < 0) {
                changeLabel.setText(String.format("%.2f", Math.abs(neededMoneyValue)) );
                neededMoneyLabel.setText("0");
                insertButton.setDisable(true);
            } else {
                neededMoneyLabel.setText(String.format("%.2f", neededMoneyValue));
            }

        }


    }

    private boolean checkIsDenominationCorrect(String text) {

        if (!("".equals(text) || !text.matches("(5|2|1|0.5|0.2|0.1)"))) {
            return true;
        }
        return false;
    }

    private void findControls(Scene scene) {

        listView = (ListView<HBoxCell>) scene.lookup("#products_listview");

        cancelButton = (Button) scene.lookup("#cancel_button");
        insertButton = (Button) scene.lookup("#insert_money_button");
        insertedMoneyTextField = (TextField) scene.lookup("#inserted_money_textField");
        messageLabel = (Label) scene.lookup("#message_label");
        pickedItemTypeLabel = (Label) scene.lookup("#picked_item");
        insertedMoneyLabel = (Label) scene.lookup("#inserted_money_label");
        neededMoneyLabel = (Label) scene.lookup("#needed_money_label");
        pickedItemPriceLabel = (Label) scene.lookup("#picked_item_price");
        changeLabel = (Label) scene.lookup("#change_label");

    }

    private void populateProductsIntoListView() {


        ArrayList<Product> p = FakeData.fakeProducts();


        List<HBoxCell> list = new ArrayList<HBoxCell>();

        for (int i = 0; i < p.size(); i++) {
            list.add(new HBoxCell(p.get(i).getType(), new Double(10) + "$"));
        }

        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
        listView.setItems(myObservableList);

    }

    public boolean isProductChosen() {
        return !"".equals(pickedItemPriceLabel.getText());
    }

    public int isMoneyEnough() {

        if (neededMoneyValue < 0) {
            return -1;
        }
        if (neededMoneyValue == 0) {
            return 0;
        }

        return 1;
    }

    private class HBoxCell extends HBox {

        Label productType = new Label();
        Label productPrice = new Label();

        HBoxCell(String productTypeText, String productPriceText) {
            super();

            productType.setText(productTypeText);
            productType.setMaxWidth(Double.MAX_VALUE);

            productPrice.setText(productPriceText);
            productPrice.setMaxWidth(Double.MAX_VALUE);

            HBox.setHgrow(productType, Priority.ALWAYS);


            this.getChildren().addAll(productType, productPrice);
        }

    }
}
