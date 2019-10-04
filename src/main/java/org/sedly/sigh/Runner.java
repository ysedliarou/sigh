package org.sedly.sigh;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.PerspectiveProjection;
import org.sedly.sigh.math.Quaternion;
import org.sedly.sigh.math.Transformation;
import org.sedly.sigh.math.Vector3f;
import org.sedly.sigh.math.View;
import org.sedly.sigh.model.Camera;
import org.sedly.sigh.shader.PhongShader;
import org.sedly.sigh.shader.StaticShader;
import org.sedly.sigh.shader.light.BaseLight;
import org.sedly.sigh.shader.light.DirectionalLight;
import org.sedly.sigh.shader.light.Light;
import org.sedly.sigh.model.Loader;
import org.sedly.sigh.model.Mesh;
import org.sedly.sigh.model.Model;
import org.sedly.sigh.model.ObjLoader;
import org.sedly.sigh.model.Renderer;
import org.sedly.sigh.model.Texture;
import org.sedly.sigh.model.TexturedMesh;
import org.sedly.sigh.shader.light.SpecularReflection;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Runner {

  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;

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

  private Camera camera = new Camera();

  public Matrix4f view() {
    return View.builder()
        .position(camera.getPosition())
        .forward(camera.getForward())
        .up(camera.getUp())
        .build()
        .view();
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
        camera.move(Vector3f.UNIT_Z, 2);
        System.out.println("W");
      }
      if (key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_PRESS) {
        camera.move(Vector3f.UNIT_Z, -2);
        System.out.println("S");
      }
      if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_PRESS) {
        camera.move(Vector3f.UNIT_X, 2);
        System.out.println("A");
      }
      if (key == GLFW.GLFW_KEY_D && action == GLFW.GLFW_PRESS) {
        camera.move(Vector3f.UNIT_X, -2);
        System.out.println("D");
      }

      if (key == GLFW.GLFW_KEY_Q && action == GLFW.GLFW_PRESS) {
        camera.rotateY(0.2f);
        System.out.println("q");
      }

      if (key == GLFW.GLFW_KEY_E && action == GLFW.GLFW_PRESS) {
        camera.rotateY(-0.2f);
        System.out.println("e");
      }

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

    PhongShader shader = new PhongShader();
    // StaticShader shader = new StaticShader();

    Model model = ObjLoader.loadObjModel("bunny.obj");

    Texture texture = loader.texture("white.png");

    Mesh mesh = loader.create(model);

    TexturedMesh texturedMesh = new TexturedMesh(mesh, texture);

    float angleModel = 0.04f;
    float angleLight = 0.04f;


    float t = 0;
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window)) {
      glfwSwapBuffers(window); // swap the color buffers

      Matrix4f tr = Transformation.builder()
          .setTranslation(new Vector3f(0, t, 0))
          .setScaling(new Vector3f(8f,8f,8f))
          .setRotation(new Quaternion(Vector3f.UNIT_Y, angleModel))
          .build().transformation();


      Matrix4f pr = PerspectiveProjection.builder().xy(WIDTH, HEIGHT).build().projection();

      Vector3f baseColor = new Vector3f(1, 1, 1);
      Vector3f ambientLight = Vector3f.ZERO;
      BaseLight baseLight = new BaseLight(new Vector3f(1f,1f,1f), 1f);
      DirectionalLight directionalLight = new DirectionalLight(baseLight, new Vector3f(1, 0,0));

      SpecularReflection specularReflection = new SpecularReflection(1f, 150f);

      renderer.prepare();

      shader.init();
      shader.start();

      shader.loadTransformationMatrix(tr);
      shader.loadProjectionMatrix(pr);
      shader.loadViewMatrix(view());

      shader.loadBaseColor(baseColor);
      shader.loadAmbientLight(ambientLight);
      shader.loadDirectionalLight(directionalLight);
      shader.loadSpecularReflection(specularReflection);

      shader.loadEyePos(camera.getPosition());

      // shader.loadShine(150, 1);
      // shader.loadLight(new Light(new Vector3f(0,0,-1), new Vector3f(1,1,1), 1));

      renderer.render(texturedMesh);
      shader.stop();

      angleModel += 0.004;

      t-=0.002;

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