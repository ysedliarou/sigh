package org.sedly.sigh;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import org.sedly.sigh.math.MathUtil;
import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Projection;
import org.sedly.sigh.math.Quaternion;
import org.sedly.sigh.math.Transformation;
import org.sedly.sigh.math.Vector3f;
import org.sedly.sigh.model.Light;
import org.sedly.sigh.model.Loader;
import org.sedly.sigh.model.Mesh;
import org.sedly.sigh.model.Model;
import org.sedly.sigh.model.ObjLoader;
import org.sedly.sigh.model.Renderer;
import org.sedly.sigh.model.Texture;
import org.sedly.sigh.model.TexturedMesh;
import org.sedly.sigh.shader.StaticShader;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Runner {

  private static final int WIDTH = 800;

  private static final int HEIGHT = 600;
  // The window handle
  private long window;

  public void run() {

    init();
    loop();

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window);
    glfwDestroyWindow(window);

    // Terminate GLFW and free the error callback
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  private Vector3f position = new Vector3f(0, 4, 20);

  public Matrix4f view() {
    return Transformation.builder().setTranslation(position.negate()).build().transformation();
  }

  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }
    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

    // Create the window
    window = glfwCreateWindow(WIDTH, HEIGHT, "Sigh!", NULL, NULL);
    if (window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
      }

      if (key == GLFW.GLFW_KEY_W && action == GLFW.GLFW_PRESS) {
        position = position.add(new Vector3f(0.0f, 0.0f, -0.2f));
        System.out.println("W");
      }
      if (key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_PRESS) {
        position = position.add(new Vector3f(0.0f, 0.0f, 0.2f));
        System.out.println("S");
      }
      if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_PRESS) {
        position = position.add(new Vector3f(-0.2f, 0.0f, 0.0f));
        System.out.println("A");
      }
      if (key == GLFW.GLFW_KEY_D && action == GLFW.GLFW_PRESS) {
        position = position.add(new Vector3f(0.2f, 0.0f, 0.0f));
        System.out.println("D");
      }

      if (key == GLFW.GLFW_KEY_Q && action == GLFW.GLFW_PRESS) {
        position = position.rotate(new Quaternion(Vector3f.UNIT_Y, -0.2f));
        System.out.println("q");
      }

      if (key == GLFW.GLFW_KEY_E && action == GLFW.GLFW_PRESS) {
        position = position.rotate(new Quaternion(Vector3f.UNIT_Y, 0.2f));
        System.out.println("q");
      }

      System.out.println(position);

    });

    // Get the thread stack and push a new frame
    try (MemoryStack stack = stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);

      // Get the resolution of the primary monitor
      GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center the window
      glfwSetWindowPos(
          window,
          (videoMode.width() - pWidth.get(0)) / 2,
          (videoMode.height() - pHeight.get(0)) / 2
      );
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
  }

  private void loop() {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities();

    Renderer renderer = new Renderer();
    Loader loader = new Loader();

    StaticShader shader = new StaticShader();

    Model model = ObjLoader.loadObjModel("stall.obj");

    Texture texture = loader.texture("stall.png");
    texture.setShineDamper(10);
    texture.setReflectivity(1);
    Mesh mesh = loader.create(model);

    TexturedMesh texturedMesh = new TexturedMesh(mesh, texture);

    Vector3f t = new Vector3f(0, 0, -1.5f);

    float angleLight = 0.009f;
    float angleModel = 0.001f;

    Vector3f lightPosition = position; // new Vector3f(-15, 0, 0);

    float r = 0.01f, g = 0.01f, b = 0.01f;

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window)) {
      glfwSwapBuffers(window); // swap the color buffers

      Matrix4f tr = Transformation.builder()
          .setTranslation(t)
          .setRotation(new Quaternion(Vector3f.UNIT_Y, angleModel))
          .build().transformation();

      Matrix4f pr = Projection.builder().xy(WIDTH, HEIGHT).build().projection();

      Light light = new Light(lightPosition, new Vector3f(1,1,1));
      r = g = b += 0.001;

      renderer.prepare();
      shader.start();
      shader.loadTransformationMatrix(tr);
      shader.loadProjectionMatrix(pr);
      shader.loadViewMatrix(view());
      shader.loadLight(light);
      shader.loadShine(texture.getShineDamper(), texture.getReflectivity());
      renderer.render(texturedMesh);
      shader.stop();

      // lightPosition = lightPosition.rotate(new Quaternion(Vector3f.UNIT_Y, angleLight));

      // t = t.sub(new Vector3f(0.000f, 0.0f, 0.001f));
      angleModel += 0.004;


      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents();
    }

    shader.clean();
    loader.clean();
  }

  public static void main(String[] args) {
    new Runner().run();
  }

}