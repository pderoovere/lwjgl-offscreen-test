package eu.peterdr.test.lwjgloffscreentest.opengl;

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
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryUtil;

import eu.peterdr.test.lwjgloffscreentest.util.SingleThreadExecutor;

public class GLFWWindowManager {

    private static GLFWWindowManager instance;
    private SingleThreadExecutor singleThreadExecutor;
    private GLFWErrorCallback errorCallback;

    private static Logger logger = LogManager.getLogger(GLFWWindowManager.class.getName());

    public static synchronized GLFWWindowManager getInstance() {
        if (instance == null) {
            instance = new GLFWWindowManager();
        }
        return instance;
    }

    private GLFWWindowManager() {
        logger.debug("Creating GLFWWindowManager");
        this.singleThreadExecutor = new SingleThreadExecutor();
        initialize();
    }

    private void initialize() {
        this.singleThreadExecutor.submitAction(() -> {
            logger.debug("Initializing LWJGL");
            glfwSetErrorCallback(this.errorCallback = GLFWErrorCallback.createPrint(System.err));
            if (glfwInit() != GL_TRUE) {
                throw new IllegalStateException("Unable to initialize GLFW");
            }
            setWindowHints();
        });
    }

    private void setWindowHints() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    }

    public void destroyGLFW() {
        this.singleThreadExecutor.submitActionAndWait(() -> {
            glfwTerminate();
            this.errorCallback.release();
            instance = null;
        });
    }

    public long createWindow(final int width, final int height) {
        return this.singleThreadExecutor.submitActionAndGetResult(() -> {
            long window = glfwCreateWindow(width, height, "", MemoryUtil.NULL, MemoryUtil.NULL);
            if (window == MemoryUtil.NULL) {
                throw new RuntimeException("Failed to create the GLFW window");
            }
            return window;
        });
    }

    public void setWindowSize(final long window, final int width, final int height) {
        this.singleThreadExecutor.submitAction(() -> {
            glfwSetWindowSize(window, width, height);
        });
    }

    public void destroyWindow(final long window) {
        this.singleThreadExecutor.submitActionAndWait(() -> {
            glfwDestroyWindow(window);
            System.out.println("Window destroyed: " + window);
        });
    }

}
