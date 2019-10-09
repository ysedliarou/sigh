package org.sedly.sigh.shader;

import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.light.Light;

public class StaticShader extends ShaderProgram {

  private static final String VS = "vertex.glsl";
  private static final String FS = "fragment.glsl";

  private int locationTransformationMatrix;
  private int locationProjectionMatrix;
  private int locationViewMatrix;

  private int locationLightDirection;
  private int locationLightColour;
  private int locationLightIntensity;
  private int locationShineDamper;
  private int locationReflectivity;

  public StaticShader() {
    super(VS, FS);
  }

  @Override
  protected void bind() {
    bind(0, POSITION);
    bind(1, TEX_COORD);
    bind(2, NORMAL);
  }

  @Override
  protected void initUniformLocations() {
    locationTransformationMatrix = getUniformLocation("transformationMatrix");
    locationProjectionMatrix = getUniformLocation("projectionMatrix");
    locationViewMatrix = getUniformLocation("viewMatrix");

    locationLightDirection = getUniformLocation("lightDirection");
    locationLightColour = getUniformLocation("lightColour");
    locationLightIntensity = getUniformLocation("lightIntensity");
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
    loadVector3f(locationLightDirection, light.getDirection());
    loadVector3f(locationLightColour, light.getColor());
    loadFloat(locationLightIntensity, light.getIntensity());
  }

  public void loadShine(float damper, float reflectivity) {
    loadFloat(locationShineDamper, damper);
    loadFloat(locationReflectivity, reflectivity);
  }

}
