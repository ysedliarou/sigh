package org.sedly.sigh.shader;

import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Vector3f;
import org.sedly.sigh.model.Light;

public class StaticShader extends ShaderProgram {

  private static final String VS = "vertex.glsl";
  private static final String FS = "fragment.glsl";

  private int locationTransformationMatrix;
  private int locationProjectionMatrix;
  private int locationViewMatrix;

  private int locationLightPosition;
  private int locationLightColour;
  private int locationShineDamper;
  private int locationReflectivity;

  public StaticShader() {
    super(VS, FS);
  }

  @Override
  protected void bind() {
    bind(0, "position");
    bind(1, "texCoords");
    bind(2, "normal");
  }

  @Override
  protected void getAllUniformLocations() {
    locationTransformationMatrix = getUniformLocation("transformationMatrix");
    locationProjectionMatrix = getUniformLocation("projectionMatrix");
    locationViewMatrix = getUniformLocation("viewMatrix");

    locationLightPosition = getUniformLocation("lightPosition");
    locationLightColour = getUniformLocation("lightColour");
    locationShineDamper = getUniformLocation("shineDamper");
    locationReflectivity = getUniformLocation("reflectivity");
  }

  public void loadTransformationMatrix(Matrix4f matrix4f) {
    loadMatrix4f(locationTransformationMatrix, matrix4f);
  }

  public void loadProjectionMatrix(Matrix4f matrix4f) {
    loadMatrix4f(locationProjectionMatrix, matrix4f);
  }

  public void loadViewMatrix(Matrix4f matrix4f) {
    loadMatrix4f(locationViewMatrix, matrix4f);
  }

  public void loadLight(Light light) {
    loadVector3f(locationLightPosition, light.getPosition());
    loadVector3f(locationLightColour, light.getColor());
  }

  public void loadShine(float damper, float reflectivity) {
    loadFloat(locationShineDamper, damper);
    loadFloat(locationReflectivity, reflectivity);
  }

}
