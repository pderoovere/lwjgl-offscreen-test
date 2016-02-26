package eu.peterdr.test.lwjgloffscreentest.ui;

import java.util.ArrayList;
import java.util.List;

import eu.peterdr.test.lwjgloffscreentest.opengl.OpenGLExecutor;

public class MainController {

    private List<OpenGLExecutor> executors;

    public MainController() {
        this.executors = new ArrayList<OpenGLExecutor>();
    }

    public void setView(final MainView view) {
        view.setController(this);
        view.build();
    }

    public void clickedAddExecutor() {
        OpenGLExecutor executor = new OpenGLExecutor();
        executor.initialize();
        this.executors.add(executor);
    }

    public void clickedClearAllExecutors() {
        for (OpenGLExecutor executor : this.executors) {
            executor.deleteContext();
        }
        this.executors.clear();
    }
}
