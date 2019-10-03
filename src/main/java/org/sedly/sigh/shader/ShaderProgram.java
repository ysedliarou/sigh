package org.sedly.sigh.shader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Vector3f;

public abstract class ShaderProgram {

  public static final String POSITION = "position";
  public static final String NORMAL = "normal";
  public static final String TEX_COORD = "texCoord";

  private int programId;

  private int vsId;

  private int fsId;

  public ShaderProgram(String vs, String fs) {
    vsId = load(vs, GL20.GL_VERTEX_SHADER);
    fsId = load(fs, GL20.GL_FRAGMENT_SHADER);
    programId = GL20.glCreateProgram();
    GL20.glAttachShader(programId, vsId);
    GL20.glAttachShader(programId, fsId);
    bind();
    GL20.glLinkProgram(programId);
  }

  public void start() {
    GL20.glUseProgram(programId);
  }

  public void stop() {
    GL20.glUseProgram(0);
  }

  public void clean() {
    stop();
    GL20.glDetachShader(programId, vsId);
    GL20.glDetachShader(programId, fsId);
    GL20.glDeleteShader(vsId);
    GL20.glDeleteShader(fsId);
    GL20.glDeleteProgram(programId);
  }

  protected abstract void bind();

  public void init() {
    initUniformLocations();
  }

  protected abstract void initUniformLocations();

  public void loadFloat(int location, float value) {
    GL20.glUniform1f(location, value);
  }

  public void loadBool(int location, boolean value) {
    GL20.glUniform1f(location, value ? 1 : 0);
  }

  public void loadVector3f(int location, Vector3f value) {
    GL20.glUniform3f(location, value.getX(), value.getY(), value.getZ());
  }

  public void loadMatrix4f(int location, Matrix4f value) {
    FloatBuffer floatBuffer = (FloatBuffer) BufferUtils.createFloatBuffer(16);
    float[][] matrix = value.getMatrix();
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4 ; j++) {
        floatBuffer.put(matrix[i][j]);
      }
    }
    floatBuffer.flip();
    GL20.glUniformMatrix4fv(location, true, floatBuffer);
  }

  public void loadInt(int location, int value) {
    GL20.glUniform1i(location, value);
  }

  protected int getUniformLocation(String name) {
    return GL20.glGetUniformLocation(programId, name);
  }

  protected void bind(int attribute, String name) {
    GL20.glBindAttribLocation(programId, attribute, name);
  }

  private static int load(String file, int type) {
    StringBuilder sb = new StringBuilder();

    try {
      BufferedReader bf = new BufferedReader(new FileReader(new File(ShaderProgram.class.getResource(file).getFile())));
      String line;
      while ((line = bf.readLine()) != null) {
        sb.append(line).append("\n");
      }
      bf.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    int shaderId = GL20.glCreateShader(type);
    GL20.glShaderSource(shaderId, sb);
    GL20.glCompileShader(shaderId);

    if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0) {
      System.out.println(GL20.glGetShaderInfoLog(shaderId, 1024));
    }

    return shaderId;
  }

}

