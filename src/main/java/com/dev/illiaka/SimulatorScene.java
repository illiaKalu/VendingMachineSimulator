package com.dev.illiaka;

import com.dev.illiaka.Tests.FakeData;
import com.dev.illiaka.Tests.Product;
import com.dev.illiaka.Utils.JSONParser;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by sonicmaster on 09.09.16.\
 * The class represents behavior of Vending Machine.
 * Data like products and denominations comes from GAE server
 * Internet connection needed
 *
 */
public class SimulatorScene extends Application {

    private static final String DOMINATION_IS_NOT_CORRECT = "inserted value is not correct!";
    private static final String PICK_ITEM_FIRST = "pick item first!";
    private static final String SUCCESS_NO_CHANGE = "Thank you, take your product";
    private static final String PRIMARY_STAGE_TITLE = "Vending Machine Simulator";
    private static final String NO_MORE_PRODUCTS = "Sorry, product is gone";

    private ListView<ProductsController> listView;
    private Button cancelButton;
    private Button insertButton;
    private TextField insertedMoneyTextField;
    private Label messageLabel;
    private Label pickedItemTypeLabel;
    private Label pickedItemPriceLabel;
    private Label insertedMoneyLabel;
    private Label neededMoneyLabel;
    private Label changeLabel;

    // each cell represent available denomination
    // eg. [0] - 5 denomination
    // [1] - 2 denomination
    // [5] - 0.1 denomination
    private int[] tempUserMoneyInsertion = new int[6];

    private Double insertedMoneyValue;
    private Double neededMoneyValue;

    public static void main(String[] args) {

        launch(args);

    }

    public void start(Stage primaryStage) throws Exception {

         // primary Scene implementation
        // markup file location
        String fxmlSceneMarkupFile = "/scenes/primaryScene.fxml";

        // loads scene from fxml file in resources
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream(fxmlSceneMarkupFile));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle(PRIMARY_STAGE_TITLE);

        primaryStage.show();

        findControls(scene);

        populateProductsIntoListView();


        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProductsController>() {
            public void changed(ObservableValue<? extends ProductsController> observable, ProductsController oldValue, ProductsController newValue) {

                System.out.println("amount of chosen product - " + newValue.getProductAmount());

                // check product availability
                if (newValue.getProductAmount() < 1){
                    messageLabel.setText(NO_MORE_PRODUCTS);
                    neededMoneyLabel.setText("");

                    insertButton.setDisable(true);
                    cancelButton.setDisable(true);
                }else {

                    newValue.setProductAmount(newValue.getProductAmount() - 1);

                    neededMoneyLabel.setText("" + newValue.getProductPrice());
                    changeLabel.setText("");
                    pickedItemTypeLabel.setText(newValue.getProductType());
                    pickedItemPriceLabel.setText("" + newValue.getProductPrice());

                    // activate buttons
                    insertButton.setDisable(false);
                    cancelButton.setDisable(false);

                    // clear message label
                    messageLabel.setText("");
                }
            }
        });

        // button actions
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                // return inserted money
                pickedItemTypeLabel.setText("");
                pickedItemPriceLabel.setText("");

                listView.setDisable(false);


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
                        listView.setDisable(true);


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
                        setNeededMoneyLabel(insertedMoney);


                        // check is inserted money enough
                        // 0 - no change, give product
                        // 1 - user inserted not enough money
                        // -1 - with change
                        switch (isMoneyEnough()) {

                            case 1:
                                System.out.println("not enough");
                                break;
                            case 0:

                                // TODO: Decrease product from products list


                                // reset fields and labels for next user
                                insertedMoneyValue = 0d;
                                neededMoneyValue = 0d;

                                insertedMoneyLabel.setText("0");
                                neededMoneyLabel.setText("");

                                listView.setDisable(false);

                                // no change, give product, make buttons states react properly
                                messageLabel.setText(SUCCESS_NO_CHANGE);

                                cancelButton.setDisable(true);
                                insertButton.setDisable(true);
                                break;
                            case -1:

                                insertedMoneyLabel.setText("0");
                                neededMoneyLabel.setText("");
                                listView.setDisable(false);


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

    private void setNeededMoneyLabel(String insertedMoneyString) {

        // if user inserted first denomination
        // get product price and sub inserted money
//        if ("".equals(neededMoneyLabel.getText())) {
//
//            neededMoneyValue = Double.parseDouble(pickedItemPriceLabel.getText().replace('$', Character.MIN_VALUE))
//                    - Double.parseDouble(insertedMoneyString);

            //neededMoneyLabel.setText(String.format("%.2f", neededMoneyValue));
       // } else {

            //get previous value and sub inserted money from it
            neededMoneyValue = Double.parseDouble(neededMoneyLabel.getText()) - Double.parseDouble(insertedMoneyString);

       // }

        // if there is enough money - set responsible label
        if (neededMoneyValue < 0) {
            changeLabel.setText(String.format("%.2f", Math.abs(neededMoneyValue)) );
            neededMoneyLabel.setText("0");
            insertButton.setDisable(true);
        } else {
            neededMoneyLabel.setText(String.format("%.2f", neededMoneyValue));
        }


    }

    private boolean checkIsDenominationCorrect(String text) {

        return !("".equals(text) || !text.matches("(5|2|1|0.5|0.2|0.1)"));
    }

    private void findControls(Scene scene) {

        listView = (ListView<ProductsController>) scene.lookup("#products_listview");

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

        listView.setItems(FXCollections.observableList(JSONParser.parseJson("")));

    }

    private boolean isProductChosen() {
        return !"".equals(pickedItemPriceLabel.getText());
    }

    private int isMoneyEnough() {

        if (neededMoneyValue < 0) {
            return -1;
        }
        if (neededMoneyValue == 0) {
            return 0;
        }

        return 1;
    }

}
