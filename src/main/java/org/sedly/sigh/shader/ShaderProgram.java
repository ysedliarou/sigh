package org.sedly.sigh.shader;

import org.lwjgl.opengl.GL20;
import org.sedly.sigh.math.Color;
import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Vector3f;
import org.sedly.sigh.util.BufferUtil;
import org.sedly.sigh.util.ResourceUtil;

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

  public void loadFloat(int location, float value) {
    GL20.glUniform1f(location, value);
  }

  public void loadBool(int location, boolean value) {
    GL20.glUniform1f(location, value ? 1 : 0);
  }

  public void loadVector3f(int location, Vector3f value) {
    GL20.glUniform3f(location, value.getX(), value.getY(), value.getZ());
  }

  public void loadColor(int location, Color value) {
    GL20.glUniform4f(location, value.getR(), value.getG(), value.getB(), value.getA());
  }

  public void loadMatrix4f(int location, Matrix4f value) {
    GL20.glUniformMatrix4fv(location, true, BufferUtil.floatBuffer(value));
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

    String shaderSrc = ResourceUtil.loadString(file);

    int shaderId = GL20.glCreateShader(type);
    GL20.glShaderSource(shaderId, shaderSrc);
    GL20.glCompileShader(shaderId);

    if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0) {
      System.out.println(GL20.glGetShaderInfoLog(shaderId, 1024));
    }

    return shaderId;
  }

}

