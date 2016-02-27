package eu.peterdr.test.lwjgloffscreentest.ui;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryUtil;

import eu.peterdr.test.lwjgloffscreentest.opengl.OpenGLExecutor;

public class Application2 {

    private static boolean glfwInitialized = false;
    private static boolean alive;
    private static List<OpenGLExecutor> executors = new ArrayList<OpenGLExecutor>();
    private static GLFWErrorCallback errorCallback;

    public static void main(final String[] args) {
        System.out.println(System.getProperty("org.lwjgl.system.allocator"));
        System.out.println(System.getProperty("org.lwjgl.util.DebugAllocator"));
        initializeGLFW();
        loop();
    }

    private static void loop() {
        alive = true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 'add' to add executor.");
        System.out.println("Enter 'clear' to clear executors.");
        System.out.println("Enter 'destroy' to distroy GLFW.");
        System.out.println("Enter 'exit' to distroy GLFW.");
        while (alive) {
            String input = scanner.next();
            parseInput(input);
        }
        scanner.close();
    }

    private static void parseInput(final String input) {
        switch (input) {
        case "add":
            System.out.println("About to add executor");
            addExecutor();
            System.out.println("Added executor");
            break;
        case "clear":
            System.out.println("About to clear executors");
            clearExecutors();
            System.out.println("Executors cleared");
            break;
        case "destroy":
            System.out.println("About to destroy GLFW");
            destroyGLFW();
            System.out.println("GLFW destroyed");
            break;
        case "exit":
            System.out.println("About to exit");
            exit();
            break;
        default:
            System.out.println("Unknown input, please try again");
        }
    }

    private static void addExecutor() {
        if (!glfwInitialized) {
            initializeGLFW();
        }
        OpenGLExecutor executor = new OpenGLExecutor();
        executor.initialize(createWindow());
        executors.add(executor);
    }

    private static long createWindow() {
        long window = glfwCreateWindow(1, 1, "", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        return window;
    }

    private static void clearExecutors() {
        for (OpenGLExecutor executor : executors) {
            executor.clearBuffer();
            glfwDestroyWindow(executor.getWindow());
            executor.shutDownThreadExecutor();
        }
        executors.clear();
        System.gc();
    }

    private static void destroyGLFW() {
        clearExecutors();
        if (glfwInitialized) {
            glfwTerminate();
            errorCallback.release();
            glfwInitialized = false;
        }
    }

    private static void exit() {
        alive = false;
    }

    private static void initializeGLFW() {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        setWindowHints();
        glfwInitialized = true;
    }

    private static void setWindowHints() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    }

}
