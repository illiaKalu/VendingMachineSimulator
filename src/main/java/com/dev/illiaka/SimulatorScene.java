package com.dev.illiaka;

import com.dev.illiaka.Tests.FakeData;
import com.dev.illiaka.Tests.Product;
import javafx.application.Application;
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

    ListView<HBoxCell> listView;

    Button cancelButton;
    Button insertButton;

    TextField insertedMoneyTextField;

    Label errorLabel;
    Label youPickedLabel;
    Label itCostsLabel;
    Label insertedMoneylabel;

    private static final String PRIMARY_STAGE_TITLE = "Vending Machine Simulator";

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

        /**
         * set styles to scene
         * @see /resources/style.css
         */
        scene.getStylesheets().add("style.css");


        primaryStage.show();

        findControls(scene);

        populateProductsIntoListView();

        // button actions
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                // return inserted money
                // clear you picked field
                // clear it costs field

            }
        });

        insertButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                // check if inputed value is correct
                // set error label if not
                // add inserted denomination to machine wallet
                // add value to inserted money
                //

            }
        });


    }

    private void findControls(Scene scene) {

        listView = (ListView<HBoxCell>) scene.lookup("#products_listview");

        cancelButton = (Button) scene.lookup("#cancel_button") ;
        insertButton = (Button) scene.lookup("#insert_money_button");
        insertedMoneyTextField = (TextField) scene.lookup("#inserted_money_textField");
        errorLabel = (Label) scene.lookup("#error_label");
        youPickedLabel = (Label) scene.lookup("#picked_item");
        insertedMoneylabel = (Label) scene.lookup("#inserted_money_label");
        itCostsLabel = (Label) scene.lookup("#picked_item_price");


    }

    private void populateProductsIntoListView(){


        ArrayList<Product> p = FakeData.fakeProducts();


        List<HBoxCell> list = new ArrayList<HBoxCell>();

        for (int i = 0; i < p.size(); i++) {
            list.add(new HBoxCell(p.get(i).getType(), p.get(i).getPrice() + "$"));
        }

        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
        listView.setItems(myObservableList);

    }

    private class HBoxCell extends HBox {

        Label label = new Label();
        Button button = new Button();

        HBoxCell(String labelText, String buttonText) {
            super();

            label.setText(labelText);
            label.setMaxWidth(Double.MAX_VALUE);

            HBox.setHgrow(label, Priority.ALWAYS);

            button.setText(buttonText);

            this.getChildren().addAll(label, button);
        }

    }
}
