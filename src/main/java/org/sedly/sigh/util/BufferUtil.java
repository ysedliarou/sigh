package org.sedly.sigh.util;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.sedly.sigh.math.Matrix4f;

public class BufferUtil {

    // TODO: verify
    private static final int BYTES = Integer.SIZE;

    private static ByteBuffer byteBuffer(int length) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(length * BYTES);
        byteBuffer.order(ByteOrder.nativeOrder());
        return byteBuffer;
    }

    public static IntBuffer intBuffer(int[] data) {
        ByteBuffer byteBuffer = byteBuffer(data.length);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(data);
        intBuffer.flip();
        return intBuffer;
    }

    public static FloatBuffer floatBuffer(float[] data) {
        ByteBuffer byteBuffer = byteBuffer(data.length);
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.flip();
        return floatBuffer;
    }

    public static ByteBuffer byteBuffer(byte[] data) {
        ByteBuffer byteBuffer = byteBuffer(data.length);
        byteBuffer.put(data);
        byteBuffer.flip();
        return byteBuffer;
    }

    public static FloatBuffer floatBuffer(Matrix4f value) {
        float[] data = new float[Matrix4f.SIZE * Matrix4f.SIZE];

        float[][] matrix = value.getMatrix();
        for (int i = 0, k = 0; i < Matrix4f.SIZE; i++) {
            for (int j = 0; j < Matrix4f.SIZE; j++, k++) {
                data[k] = matrix[i][j];
            }
        }

        return floatBuffer(data);
    }

    public static ByteBuffer byteBuffer(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        boolean alpha = image.getColorModel().hasAlpha();
        byte[] data = new byte[image.getHeight() * image.getWidth() * 4];

        for (int y = 0, i = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++, i += 4) {
                int pixel = pixels[y * image.getWidth() + x];

                data[i] = (byte) ((pixel >> 16) & 0xFF);
                data[i + 1] = (byte) ((pixel >> 8) & 0xFF);
                data[i + 2] = (byte) ((pixel) & 0xFF);
                data[i + 3] = alpha ? (byte) ((pixel >> 24) & 0xFF) : (byte) 0xFF;
            }
        }

        return BufferUtil.byteBuffer(data);
    }


}
