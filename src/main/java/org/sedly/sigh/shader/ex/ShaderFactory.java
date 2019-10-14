package org.sedly.sigh.shader.ex;

import java.util.Map;
import org.lwjgl.opengl.GL20;
import org.sedly.sigh.util.ResourceUtil;

public class ShaderFactory {

  private Map<String, Initializer> initializers;

  public Shader create(String name) {

    int vsId = load(vertexFileName(name), GL20.GL_VERTEX_SHADER);
    int fsId = load(fragmentFileName(name), GL20.GL_FRAGMENT_SHADER);
    int programId = GL20.glCreateProgram();

    GL20.glAttachShader(programId, vsId);
    GL20.glAttachShader(programId, fsId);

    Shader shader = Shader.builder()
        .programId(programId)
        .vertexShaderId(vsId)
        .fragmentShaderId(fsId)
        .build();

    // bind
    GL20.glBindAttribLocation(programId, 0, "position");
    GL20.glBindAttribLocation(programId, 1, "texCoord");
    GL20.glBindAttribLocation(programId, 0, "normal");


    GL20.glLinkProgram(programId);

    initializers.get(name).initialize();

    return shader;
  }

  private static String vertexFileName(String name) {
    return name + "Vertex.glsl";
  }

  private static String fragmentFileName(String name) {
    return name + "Fragment.glsl";
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
