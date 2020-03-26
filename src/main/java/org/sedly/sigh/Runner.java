package org.sedly.sigh;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.projection.FrustumProjection;
import org.sedly.sigh.math.projection.OrthographicProjection;
import org.sedly.sigh.math.projection.PerspectiveProjection;
import org.sedly.sigh.math.Transformation;
import org.sedly.sigh.math.Vector3f;
import org.sedly.sigh.math.rotation.AxisAngleRotation;
import org.sedly.sigh.math.rotation.LookAtRotation;
import org.sedly.sigh.renderer.Camera;
import org.sedly.sigh.renderer.DummyCamera;
import org.sedly.sigh.renderer.LookAtCamera;
import org.sedly.sigh.shader.PhongShader;
import org.sedly.sigh.model.Loader;
import org.sedly.sigh.model.Mesh;
import org.sedly.sigh.model.Model;
import org.sedly.sigh.model.ObjLoader;
import org.sedly.sigh.model.Renderer;
import org.sedly.sigh.model.Texture;
import org.sedly.sigh.model.TexturedMesh;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.sedly.sigh.Const.AMBIENT_LIGHT;
import static org.sedly.sigh.Const.BASE_COLOR;
import static org.sedly.sigh.Const.DIRECTIONAL_LIGHT_0;
import static org.sedly.sigh.Const.POINT_LIGHT_0;
import static org.sedly.sigh.Const.POINT_LIGHT_1;
import static org.sedly.sigh.Const.POINT_LIGHT_2;
import static org.sedly.sigh.Const.SPECULAR_REFLECTION;
import static org.sedly.sigh.Const.SPOT_LIGHT_0;

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

//  private static DummyCamera camera = new DummyCamera(
//          Vector3f.ZERO,
//          Vector3f.UNIT_Z.negate(),
//          Vector3f.UNIT_Y);

  private static Camera camera = LookAtCamera.builder()
          .setPosition(new Vector3f(0, 0, 0))
          .setTarget(new Vector3f(0, 0, -15))
          .build();

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

//      if (key == GLFW.GLFW_KEY_W && action == GLFW.GLFW_PRESS) {
//        camera.move(camera.getForward(), 2);
//        System.out.println("W" + camera.getPosition());
//      }
//      if (key == GLFW.GLFW_KEY_S && action == GLFW.GLFW_PRESS) {
//        camera.move(camera.getForward(), -2);
//        System.out.println("S");
//      }
//      if (key == GLFW.GLFW_KEY_A && action == GLFW.GLFW_PRESS) {
//        camera.move(camera.getForward().cross(camera.getUp()).normalize(), -2);
//        System.out.println("A");
//      }
//      if (key == GLFW.GLFW_KEY_D && action == GLFW.GLFW_PRESS) {
//        camera.move(camera.getForward().cross(camera.getUp()).normalize(), 2);
//        System.out.println("D");
//      }
//
//      if (key == GLFW.GLFW_KEY_Q && action == GLFW.GLFW_PRESS) {
//        camera.rotateY(0.2f);
//        System.out.println("q");
//      }
//
//      if (key == GLFW.GLFW_KEY_E && action == GLFW.GLFW_PRESS) {
//        camera.rotateY(-0.2f);
//        System.out.println("e");
//      }

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

    TexturedMesh dragon = model(loader, "dragon.obj", "stall.png");
    TexturedMesh bunny = model(loader, "bunny.obj", "stall.png");


    float angleModel = 0.0f;

    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window)) {
      glfwSwapBuffers(window); // swap the color buffers

      renderer.prepare();

      shader.start();
      updateShader(shader, new Vector3f(-7, -5, -15), 1, angleModel);
      renderer.render(dragon);
      shader.stop();

      shader.start();
      updateShader(shader, new Vector3f(7, -5, -15), 6, -angleModel);
      renderer.render(bunny);
      shader.stop();

      angleModel += 0.01;

//
//      float radius = 10.0f;
//      float camX = (float) Math.sin(glfwGetTime()) * radius;
//      float camZ = (float) Math.cos(glfwGetTime()) * radius;
//      camera =  LookAtCamera.builder()
//              .setPosition(new Vector3f(camX, 0, camZ))
//              .setTarget(new Vector3f(-7, -5, -15))
//              .build();

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents();
    }

    shader.clean();

    loader.clean();
  }

  private static Matrix4f modelMatrix(Vector3f translation, float scale, float angle) {
    return Transformation.builder()
        .setTranslation(translation)
        .setScaling(new Vector3f(1,1,1).scale(scale))
//        .setRotation(new QuaternionRotation(new Quaternion(Vector3f.UNIT_Y, angle)))
        .setRotation(new AxisAngleRotation(Vector3f.UNIT_Y, angle))
        .build().transform();
  }

  private static void updateShader(PhongShader shader, Vector3f translation, float scale, float angleModel) {
    shader.loadTransformationMatrix(modelMatrix(translation, scale, angleModel));
    shader.loadProjectionMatrix(camera.getProjection());
    shader.loadViewMatrix(camera.getView());

    shader.loadBaseColor(BASE_COLOR);
    shader.loadAmbientLight(AMBIENT_LIGHT);
    shader.loadDirectionalLight(DIRECTIONAL_LIGHT_0);
    shader.loadPointLight(POINT_LIGHT_0, 0);
    shader.loadPointLight(POINT_LIGHT_1, 1);
    shader.loadPointLight(POINT_LIGHT_2, 2);
    shader.loadSpotLight(SPOT_LIGHT_0, 0);

    shader.loadSpecularReflection(SPECULAR_REFLECTION);

    shader.loadEyePos(camera.getPosition());
  }

  public static TexturedMesh model(Loader loader, String modelName, String textureName) {
    Model model = ObjLoader.loadObjModel(modelName);
    Texture texture = loader.texture(textureName);
    Mesh mesh = loader.create(model);
    return new TexturedMesh(mesh, texture);
  }

  public static void main(String[] args) {
    new Runner().run();
  }

}