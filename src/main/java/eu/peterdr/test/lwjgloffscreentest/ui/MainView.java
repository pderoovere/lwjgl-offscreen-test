package eu.peterdr.test.lwjgloffscreentest.ui;

import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class MainView extends GridPane {

    private MainController controller;

    private Label lblMemoryUsed;
    private Button btnAddExecutor;
    private Button btnClearAllExecutors;

    public MainView() {
    }

    public void setController(final MainController controller) {
        this.controller = controller;
    }

    public void build() {
        this.lblMemoryUsed = new Label("");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Get system memory used
                NumberFormat format = NumberFormat.getInstance();
                Platform.runLater(() -> {
                    MainView.this.lblMemoryUsed.setText("Heap memory used: "
                            + (format
                                    .format((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024)));
                });
            }
        }, 0, 500);
        this.btnAddExecutor = new Button("Add executor");
        this.btnAddExecutor.setOnAction((event) -> {
            this.controller.clickedAddExecutor();
        });
        this.btnClearAllExecutors = new Button("Clear all executors");
        this.btnClearAllExecutors.setOnAction((event) -> {
            this.controller.clickedClearAllExecutors();
        });
        add(this.lblMemoryUsed, 0, 0, 2, 1);
        add(this.btnAddExecutor, 0, 1);
        add(this.btnClearAllExecutors, 1, 1);
        setAlignment(Pos.CENTER);
        setHgap(8);
        setVgap(16);
    }
}
