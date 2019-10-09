package org.sedly.sigh.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Loader {

  private List<Integer> vaos = new ArrayList<>();
  private List<Integer> vbos = new ArrayList<>();
  private List<Integer> textures = new ArrayList<>();

  public Mesh create(Model model) {
    int vaoId = vao();
    indices(model.getIndices());
    store(0, 3, model.getPositions());
    store(1, 2, model.getTexCoords());
    store(2,3, model.getNormals());
    unbind();
    return new Mesh(vaoId, model.getIndices().length);
  }

  public Texture texture(String file) {

    BufferedImage image = null;
    try {
      image = ImageIO.read(new File(Loader.class.getResource(file).getFile()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    ByteBuffer byteBuffer = byteBuffer(image);

    int texId  = GL11.glGenTextures();

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

    GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);

    textures.add(texId);

    return new Texture(texId);
  }

  private int vao() {
    int vaoId = GL30.glGenVertexArrays();
    vaos.add(vaoId);
    GL30.glBindVertexArray(vaoId);
    return vaoId;
  }

  private void store(int attributeNumber, int coordSize, float[] data) {
    int vboId = GL15.glGenBuffers();
    vbos.add(vboId);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer(data), GL15.GL_STATIC_DRAW);
    GL20.glVertexAttribPointer(attributeNumber, coordSize , GL11.GL_FLOAT, false, 0, 0);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
  }

  private void indices(int[] indices) {
    int vboId = GL15.glGenBuffers();
    vbos.add(vboId);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
    GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intBuffer(indices), GL15.GL_STATIC_DRAW);
  }

  private IntBuffer intBuffer(int[] data) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length * 4);
    byteBuffer.order(ByteOrder.nativeOrder());
    IntBuffer intBuffer = byteBuffer.asIntBuffer();
    intBuffer.put(data);
    intBuffer.flip();
    return intBuffer;
  }

  private FloatBuffer floatBuffer(float[] data) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length * 4);
    byteBuffer.order(ByteOrder.nativeOrder());
    FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
    floatBuffer.put(data);
    floatBuffer.flip();
    return floatBuffer;
  }

  private ByteBuffer byteBuffer(byte[] data) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length * 4);
    byteBuffer.order(ByteOrder.nativeOrder());
    byteBuffer.put(data);
    byteBuffer.flip();
    return byteBuffer;
  }

  private ByteBuffer byteBuffer(BufferedImage image) {
    int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
    boolean alpha = image.getColorModel().hasAlpha();
    byte[] data = new byte[image.getHeight() * image.getWidth() * 4];

    for (int y = 0, i = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++, i+=4) {
        int pixel = pixels[y * image.getWidth() + x];

        data[i] = (byte) ((pixel >> 16) & 0xFF);
        data[i + 1] = (byte) ((pixel >> 8) & 0xFF);
        data[i + 2] = (byte) ((pixel) & 0xFF);
        data[i + 3] = alpha ? (byte) ((pixel >> 24) & 0xFF) : (byte) 0xFF;
      }
    }
    return byteBuffer(data);
  }

  private void unbind() {
    GL30.glBindVertexArray(0);
  }

  public void clean() {
    vaos.stream().forEach(GL30::glDeleteVertexArrays);
    vbos.stream().forEach(GL15::glDeleteBuffers);
    textures.stream().forEach(GL11::glDeleteTextures);
  }

}
