package org.sedly.sigh.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.sedly.sigh.math.Matrix4f;

public class BufferUtil {

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
      for (int j = 0; j < Matrix4f.SIZE ; j++, k++) {
        data[k] = matrix[i][j];
      }
    }

    return floatBuffer(data);
  }


}
