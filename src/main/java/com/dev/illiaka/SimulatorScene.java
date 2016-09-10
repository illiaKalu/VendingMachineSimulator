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
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonicmaster on 09.09.16.
 */
public class SimulatorScene extends Application{

    private static final String DOMINATION_IS_NOT_CORRECT = "inserted value is not correct!";
    private static final String PICK_ITEM_FIRST = "pick item first!";
    ListView<HBoxCell> listView;

    Button cancelButton;
    Button insertButton;

    TextField insertedMoneyTextField;

    Label errorLabel;
    Label pickedItemTypeLabel;
    Label pickedItemPriceLabel;
    Label insertedMoneyLabel;
    Label neededMoneyLabel;
    Label changeLabel;

    private static final String PRIMARY_STAGE_TITLE = "Vending Machine Simulator";

    // each cell represent available denomination
    // eg [0] - 5 denomination
    // [1] - 2 denomination
    // [5] - 0.1 denomination
    int[] tempUserMoneyInsertion = new int[6];

    Double insertedMoney;
    Double neededMoney;

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
                errorLabel.setText("");

                String insertedMoney = insertedMoneyTextField.getText();

                // check is any product chosen
                if (isProductChosen()){

                    // check if inserted value is correct
                    if (checkIsDenominationCorrect(insertedMoney)){

                        setInsertedMoneyLabel(insertedMoney);


                        switch (insertedMoney){

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
                                errorLabel.setText(DOMINATION_IS_NOT_CORRECT);
                        }

                        // calculate how much money user need to add and set responsible Label
                        setNeededMoneyLabel(insertedMoney);


                        // check is inserted money enough
                        // 0 - no change, give product
                        // 1 - user inserted not enough money
                        // -1 - with change
                        switch (isMoneyEnough()){

                            case 1:
                                System.out.println("not enough");
                                break;
                            case 0:
                                System.out.println("no change");
                                break;
                            case -1:
                                System.out.println("give change");
                        }


                    }else{
                        errorLabel.setText(DOMINATION_IS_NOT_CORRECT);
                    }

                }else {
                    errorLabel.setText(PICK_ITEM_FIRST);
                }

                // add inserted denomination to machine wallet
                // add value to inserted money


            }
        });

    }

    // get previous value of inserted money label and add to it new value
    private void setInsertedMoneyLabel(String insertedMoneyString) {
        insertedMoney = Double.parseDouble(insertedMoneyLabel.getText()) +
                Double.parseDouble(insertedMoneyString);

        insertedMoneyLabel.setText(String.format("%.2f", insertedMoney));

    }

    private void setNeededMoneyLabel(String insertedMoneyString) {

        // if user inserted first denomination
        // get product price and sub inserted money
            if ("".equals(neededMoneyLabel.getText())){

                neededMoney = Double.parseDouble(pickedItemPriceLabel.getText().replace('$', Character.MIN_VALUE))
                        - Double.parseDouble(insertedMoneyString);

                neededMoneyLabel.setText(String.format("%.2f", neededMoney));
            }else {

         //else - get previous value and sub inserted money from it
                neededMoney = Double.parseDouble(neededMoneyLabel.getText()) - Double.parseDouble(insertedMoneyString);

                // if there is enough money - set responsible label
                if (neededMoney < 0){
                    changeLabel.setText("" + Math.abs(neededMoney));
                    neededMoneyLabel.setText("0");
                    insertButton.setDisable(true);
                }else{
                    neededMoneyLabel.setText(String.format("%.2f", neededMoney));
                }

            }



    }

    private boolean checkIsDenominationCorrect(String text) {

        if ( !( "".equals(text) || !text.matches("(5|2|1|0.5|0.2|0.1)")) ){
            return true;
        }
        return false;
    }

    private void findControls(Scene scene) {

        listView = (ListView<HBoxCell>) scene.lookup("#products_listview");

        cancelButton = (Button) scene.lookup("#cancel_button");
        insertButton = (Button) scene.lookup("#insert_money_button");
        insertedMoneyTextField = (TextField) scene.lookup("#inserted_money_textField");
        errorLabel = (Label) scene.lookup("#error_label");
        pickedItemTypeLabel = (Label) scene.lookup("#picked_item");
        insertedMoneyLabel = (Label) scene.lookup("#inserted_money_label");
        neededMoneyLabel = (Label) scene.lookup("#needed_money_label");
        pickedItemPriceLabel = (Label) scene.lookup("#picked_item_price");
        changeLabel = (Label) scene.lookup("#change_label");

    }

    private void populateProductsIntoListView(){


        ArrayList<Product> p = FakeData.fakeProducts();


        List<HBoxCell> list = new ArrayList<HBoxCell>();

        for (int i = 0; i < p.size(); i++) {
            list.add(new HBoxCell (p.get(i).getType(), new Double(10) + "$"));
        }

        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
        listView.setItems(myObservableList);

    }

    public boolean isProductChosen() {
        return !"".equals(pickedItemPriceLabel.getText());
    }

    public int isMoneyEnough() {

        if ( neededMoney < 0) {
            return -1;
        }
        if ( neededMoney == 0) {
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
