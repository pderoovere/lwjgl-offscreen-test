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

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import eu.peterdr.test.lwjgloffscreentest.util.OpenGLUtils;
import eu.peterdr.test.lwjgloffscreentest.util.SingleThreadExecutor;

public class OpenGLExecutor {

    private SingleThreadExecutor singleThreadExecutor;
    private long window;

    private int bufferId;
    private FloatBuffer buffer;

    public OpenGLExecutor() {
        this.singleThreadExecutor = new SingleThreadExecutor();
    }

    public void initialize() {
        this.singleThreadExecutor.submitActionAndWait(() -> {
            this.window = GLFWWindowManager.getInstance().createWindow(1, 1);
            setUpContext();
        });
    }

    public void initialize(final long window) {
        this.window = window;
        this.singleThreadExecutor.submitActionAndWait(() -> {
            setUpContext();
        });
    }

    private void setUpContext() {
        glfwMakeContextCurrent(this.window);
        GL.createCapabilities();
        setUpOpenGLOptions();
        createBuffer();
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
        this.bufferId = OpenGLUtils.createArrayBuffer(this.buffer);
    }

    public void deleteContext() {
        this.singleThreadExecutor.submitActionAndWait(() -> {
            this.buffer = null;
            GLFWWindowManager.getInstance().destroyWindow(this.window);
        });
        shutDownThreadExecutor();
    }

    public void shutDownThreadExecutor() {
        this.singleThreadExecutor.shutdown();
    }

    public long getWindow() {
        return this.window;
    }

    public void clearBuffer() {
        this.singleThreadExecutor.submitActionAndWait(() -> {
            OpenGLUtils.deleteBuffer(this.bufferId);
            this.buffer = null;
        });
    }
}
