package eu.peterdr.test.lwjgloffscreentest.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        MainController mainController = new MainController();
        MainView mainView = new MainView();
        mainController.setView(mainView);
        Scene scene = new Scene(mainView, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
