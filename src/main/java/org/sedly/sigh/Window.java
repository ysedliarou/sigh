package org.sedly.sigh;

import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.IntBuffer;
import java.security.PublicKey;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

public class Window {

  public static final int WIDTH = 800;

  public static final int HEIGHT = 600;


  private long window;

  private int width, height;

  public Window(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void create() {

    if (!GLFW.glfwInit()) {
      return;
    }

    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

    window = GLFW.glfwCreateWindow(width, height, "SIGH", 0, 0);
    if (window == 0) {
      return;
    }

    try (MemoryStack stack = MemoryStack.stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      GLFW.glfwGetWindowSize(window, pWidth, pHeight);

      // Get the resolution of the primary monitor
      GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

      // Center the window
      GLFW.glfwSetWindowPos(
          window,
          (videoMode.width() - pWidth.get(0)) / 2,
          (videoMode.height() - pHeight.get(0)) / 2
      );
    }

    GLFW.glfwMakeContextCurrent(window);
    GL.createCapabilities();

    GLFW.glfwShowWindow(window);
  }

  public void stop() {
    GLFW.glfwTerminate();
  }

  public void update() {

    GL11.glClearColor(1, 0, 0, 1);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    GLFW.glfwPollEvents();
  }

  public void swap() {
    GLFW.glfwSwapBuffers(window);
  }

  public boolean isClosed() {
    return GLFW.glfwWindowShouldClose(window);
  }

}
