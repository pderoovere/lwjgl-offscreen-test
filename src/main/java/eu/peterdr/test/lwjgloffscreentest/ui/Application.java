package eu.peterdr.test.lwjgloffscreentest.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    public static void main(final String[] args) {
        System.out.println(System.getProperty("org.lwjgl.system.allocator"));
        System.out.println(System.getProperty("org.lwjgl.util.DebugAllocator"));
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

    @Override
    public void stop() {
        System.out.println("About to exit");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        });
    }

}
