package eu.peterdr.test.lwjgloffscreentest.ui;

import java.util.ArrayList;
import java.util.List;

import eu.peterdr.test.lwjgloffscreentest.opengl.GLFWWindowManager;
import eu.peterdr.test.lwjgloffscreentest.opengl.OpenGLExecutor;

public class MainController {

    private List<OpenGLExecutor> executors;
    private MainView view;

    public MainController() {
        this.executors = new ArrayList<OpenGLExecutor>();
    }

    public void setView(final MainView view) {
        this.view = view;
        view.setController(this);
        view.build();
    }

    public void clickedAddExecutor() {
        OpenGLExecutor executor = new OpenGLExecutor();
        executor.initialize();
        this.executors.add(executor);
        this.view.setBtnClearAllExecutorsDisabled(false);
    }

    public void clickedClearAllExecutors() {
        for (OpenGLExecutor executor : this.executors) {
            executor.deleteContext();
        }
        this.executors.clear();
        this.view.setBtnClearAllExecutorsDisabled(true);
        this.view.setBtnDestroyGLFWDisabled(false);
    }

    public void clickedDestroyGLFW() {
        GLFWWindowManager.getInstance().destroyGLFW();
        this.view.setBtnDestroyGLFWDisabled(true);
    }
}
