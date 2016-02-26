package eu.peterdr.test.lwjgloffscreentest.opengl;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDepthRange;
import static org.lwjgl.opengl.GL11.glEnable;

import java.nio.FloatBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import eu.peterdr.test.lwjgloffscreentest.util.OpenGLUtils;
import eu.peterdr.test.lwjgloffscreentest.util.SingleThreadExecutor;

public class OpenGLExecutor {

    private SingleThreadExecutor singleThreadExecutor;
    private long window;

    private FloatBuffer buffer;

    private static Logger logger = LogManager.getLogger(OpenGLExecutor.class.getName());

    public OpenGLExecutor() {
        this.singleThreadExecutor = new SingleThreadExecutor();
    }

    public void initialize() {
        this.singleThreadExecutor.submitAction(() -> {
            this.window = GLFWWindowManager.getInstance().createWindow(1, 1);
            glfwMakeContextCurrent(this.window);
            GL.createCapabilities();
            setUpOpenGLOptions();
            createBuffer();
        });
    }

    private void setUpOpenGLOptions() {
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthRange(1, 0);
    }

    private void createBuffer() {
        this.buffer = BufferUtils.createFloatBuffer(OpenGLUtils.ARRAY_BUFFER_LENGTH);
        int bufferId = OpenGLUtils.createArrayBuffer(this.buffer);
        logger.debug("Created buffer with id: " + bufferId);
    }

    public void deleteContext() {
        glfwMakeContextCurrent(0);
        GLFWWindowManager.getInstance().destroyWindow(this.window);
        this.buffer = null;
        System.gc();
    }
}
