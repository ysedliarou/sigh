package org.sedly.sigh.shader;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Vector3f;
import org.sedly.sigh.shader.light.DirectionalLight;
import org.sedly.sigh.shader.light.SpecularReflection;

public class PhongShader extends ShaderProgram {

  private static final String VS = "phongVertex.glsl";
  private static final String FS = "phongFragment.glsl";

  public static final String TRANSFORMATION_MATRIX = "transformationMatrix";
  public static final String PROJECTION_MATRIX = "projectionMatrix";
  public static final String VIEW_MATRIX = "viewMatrix";

  public static final String BASE_COLOR = "baseColor";
  public static final String AMBIENT_LIGHT = "ambientLight";

  public static final String DIRECTIONAL_BASE_COLOR = "directionalLight.baseLight.color";
  public static final String DIRECTIONAL_BASE_INTENSITY = "directionalLight.baseLight.intensity";
  public static final String DIRECTIONAL_DIRECTION = "directionalLight.direction";

  public static final String SPECULAR_INTENSITY = "specularReflection.intensity";
  public static final String SPECULAR_POWER = "specularReflection.power";

  public static final String EYE_POS = "eyePos";

  private Map<String, Integer> locations = new HashMap<>();

  public PhongShader() {
    super(VS, FS);
  }

  private List<String> uniforms = Lists.newArrayList(
      TRANSFORMATION_MATRIX,
      PROJECTION_MATRIX,
      VIEW_MATRIX,
      BASE_COLOR,
      AMBIENT_LIGHT,
      DIRECTIONAL_BASE_COLOR,
      DIRECTIONAL_BASE_INTENSITY,
      DIRECTIONAL_DIRECTION,
      SPECULAR_INTENSITY,
      SPECULAR_POWER,
      EYE_POS);

  @Override
  protected void bind() {
    bind(0, POSITION);
    bind(1, TEX_COORD);
    bind(2, NORMAL);
  }

  @Override
  protected void initUniformLocations() {
    Map<String, Integer> locations = uniforms.stream()
        .collect(Collectors.toMap(Function.identity(), this::getUniformLocation));
    this.locations.putAll(locations);
  }

  public void loadTransformationMatrix(Matrix4f matrix4f) {
    loadMatrix4f(locations.get(TRANSFORMATION_MATRIX), matrix4f);
  }

  public void loadProjectionMatrix(Matrix4f matrix4f) {
    loadMatrix4f(locations.get(PROJECTION_MATRIX), matrix4f);
  }

  public void loadViewMatrix(Matrix4f matrix4f) {
    loadMatrix4f(locations.get(VIEW_MATRIX), matrix4f);
  }

  public void loadBaseColor(Vector3f vector3f) {
    loadVector3f(locations.get(BASE_COLOR), vector3f);
  }

  public void loadAmbientLight(Vector3f vector3f) {
    loadVector3f(locations.get(AMBIENT_LIGHT), vector3f);
  }

  public void loadDirectionalLight(DirectionalLight directionalLight) {
    loadVector3f(locations.get(DIRECTIONAL_BASE_COLOR), directionalLight.getBaseLight().getColor());
    loadFloat(locations.get(DIRECTIONAL_BASE_INTENSITY), directionalLight.getBaseLight().getIntensity());
    loadVector3f(locations.get(DIRECTIONAL_DIRECTION), directionalLight.getDirection());
  }

  public void loadSpecularReflection(SpecularReflection specularReflection) {
    loadFloat(locations.get(SPECULAR_INTENSITY), specularReflection.getIntensity());
    loadFloat(locations.get(SPECULAR_POWER), specularReflection.getPower());
  }

  public void loadEyePos(Vector3f vector3f) {
    loadVector3f(locations.get(EYE_POS), vector3f);
  }

}
