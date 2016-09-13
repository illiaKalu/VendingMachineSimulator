package com.dev.illiaka.Scenes;

import com.dev.illiaka.ProductsController;
import com.dev.illiaka.Utils.HttpRequestReadJSON;
import com.dev.illiaka.Utils.JSONParser;
import com.dev.illiaka.Wallet;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.concurrent.ExecutionException;

/**
 * Created by sonicmaster on 12.09.16.
 */
public class SplashScene extends Application {

    public static final String SPLASH_IMAGE =
            "https://mir-s3-cdn-cf.behance.net/project_modules/disp/e70f9144231595.56076c81e910f.png";
    private static final int SPLASH_WIDTH = 676;
    private static final int SPLASH_HEIGHT = 227;
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        ImageView splash = new ImageView(new Image(
                SPLASH_IMAGE
        ));
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
        progressText = new Label("");
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle(
                "-fx-padding: 5; " +
                        "-fx-background-color: cornsilk; " +
                        "-fx-border-width:5; " +
                        "-fx-border-color: " +
                        "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                        ");"
        );
        splashLayout.setEffect(new DropShadow());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        final Task<ObservableList<ProductsController>> loadAndParseProductsTask = new Task<ObservableList<ProductsController>>() {
            @Override
            protected ObservableList<ProductsController> call() throws InterruptedException {

                // get json string with products and denominations
                String jsonData = HttpRequestReadJSON.getJSON();

                // parse json and fill product list
                ObservableList<ProductsController> products = FXCollections.observableArrayList(JSONParser.getProductsArrayList(jsonData));

                // fill machine wallet with denominations from JSON
                Wallet.getInstance().init(JSONParser.getDenominationsArray(jsonData));

                updateMessage("Loading products . . .");
                //updateProgress(i + 1, availableFriends.size());

                updateMessage("All products loaded.");

                return products;
            }
        };

        showSplash(primaryStage, loadAndParseProductsTask, () -> {
            try {
                showMainStage(loadAndParseProductsTask.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        new Thread(loadAndParseProductsTask).start();

    }

    private void showSplash(final Stage initStage, Task<?> task, InitCompletionHandler initCompletionHandler) {

        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());

        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {

                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                try {
                    initCompletionHandler.complete();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    private void showMainStage(ObservableList<ProductsController> products) throws Exception {

        SimulatorScene mainScene = new SimulatorScene(products);
        mainScene.start(new Stage());


    }


    public interface InitCompletionHandler {
        void complete() throws ExecutionException, InterruptedException;
    }
}
