package eu.peterdr.test.lwjgloffscreentest.util;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

public final class OpenGLUtils {

    public static final int ARRAY_BUFFER_LENGTH = 10000000;

    private OpenGLUtils() {
    }

    public static int createArrayBuffer(final FloatBuffer buffer) {
        int bufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_DYNAMIC_DRAW);
        return bufferId;
    }

    public static void bufferData(final int attributeId, final int dataId, final FloatBuffer data, final int dataSize) {
        glEnableVertexAttribArray(attributeId);
        glBindBuffer(GL_ARRAY_BUFFER, dataId);
        glVertexAttribPointer(attributeId, dataSize, GL_FLOAT, false, 0, 0);
        glBufferSubData(GL_ARRAY_BUFFER, 0, data);
    }

    public static void deleteBuffer(final int bufferId) {
        if (bufferId > 0) {
            glDeleteBuffers(bufferId);
        }
    }
}
