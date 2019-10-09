package org.sedly.sigh.shader;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.sedly.sigh.math.Color;
import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Vector3f;
import org.sedly.sigh.light.DirectionalLight;
import org.sedly.sigh.light.PointLight;
import org.sedly.sigh.light.SpecularReflection;
import org.sedly.sigh.light.SpotLight;

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

  public static final int MAX_POINT_LIGHTS = 4;

  public static final int MAX_SPOT_LIGHTS = 4;

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

    Map<String, Integer> pointLights = IntStream.range(0, MAX_POINT_LIGHTS)
        .boxed()
        .flatMap(i -> getPointLightName(i).stream())
        .collect(Collectors.toMap(Function.identity(), this::getUniformLocation));

    Map<String, Integer> spotLights = IntStream.range(0, MAX_SPOT_LIGHTS)
        .boxed()
        .flatMap(i -> getSpotLightName(i).stream())
        .collect(Collectors.toMap(Function.identity(), this::getUniformLocation));

    this.locations.putAll(locations);
    this.locations.putAll(pointLights);
    this.locations.putAll(spotLights);
  }

  private List<String> getPointLightName(int i) {
    List<String> names = new ArrayList<>();
    names.add("pointLights[" + i + "].baseLight.color");
    names.add("pointLights[" + i + "].baseLight.intensity");
    names.add("pointLights[" + i + "].attenuation.constant");
    names.add("pointLights[" + i + "].attenuation.linear");
    names.add("pointLights[" + i + "].attenuation.exponent");
    names.add("pointLights[" + i + "].position");
    names.add("pointLights[" + i + "].range");
    return names;
  }

  private List<String> getSpotLightName(int i) {
    List<String> names = new ArrayList<>();
    names.add("spotLights[" + i + "].pointLight.baseLight.color");
    names.add("spotLights[" + i + "].pointLight.baseLight.intensity");
    names.add("spotLights[" + i + "].pointLight.attenuation.constant");
    names.add("spotLights[" + i + "].pointLight.attenuation.linear");
    names.add("spotLights[" + i + "].pointLight.attenuation.exponent");
    names.add("spotLights[" + i + "].pointLight.position");
    names.add("spotLights[" + i + "].pointLight.range");
    names.add("spotLights[" + i + "].direction");
    names.add("spotLights[" + i + "].cutoff");
    return names;
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

  public void loadBaseColor(Color color) {
    loadColor(locations.get(BASE_COLOR), color);
  }

  public void loadAmbientLight(Vector3f vector3f) {
    loadVector3f(locations.get(AMBIENT_LIGHT), vector3f);
  }

  public void loadDirectionalLight(DirectionalLight directionalLight) {
    loadColor(locations.get(DIRECTIONAL_BASE_COLOR), directionalLight.getBaseLight().getColor());
    loadFloat(locations.get(DIRECTIONAL_BASE_INTENSITY), directionalLight.getBaseLight().getIntensity());
    loadVector3f(locations.get(DIRECTIONAL_DIRECTION), directionalLight.getDirection());
  }

  public void loadPointLights(List<PointLight> pointLights) {
    int i = 0;
    for (PointLight pointLight : pointLights) {
      loadPointLight(pointLight, i);
      i++;
    }
  }

  public void loadPointLight(PointLight pointLight, int i) {
    loadColor(locations.get("pointLights[" + i + "].baseLight.color"), pointLight.getBaseLight().getColor());
    loadFloat(locations.get("pointLights[" + i + "].baseLight.intensity"), pointLight.getBaseLight().getIntensity());
    loadFloat(locations.get("pointLights[" + i + "].attenuation.constant"), pointLight.getAttenuation().getConstant());
    loadFloat(locations.get("pointLights[" + i + "].attenuation.linear"), pointLight.getAttenuation().getLinear());
    loadFloat(locations.get("pointLights[" + i + "].attenuation.exponent"), pointLight.getAttenuation().getExponent());
    loadVector3f(locations.get("pointLights[" + i + "].position"), pointLight.getPosition());
    loadFloat(locations.get("pointLights[" + i + "].range"), pointLight.getRange());
  }

  public void loadSpotLight(SpotLight spotLight, int i) {
    loadColor(locations.get("spotLights[" + i + "].pointLight.baseLight.color"), spotLight.getPointLight().getBaseLight().getColor());
    loadFloat(locations.get("spotLights[" + i + "].pointLight.baseLight.intensity"), spotLight.getPointLight().getBaseLight().getIntensity());
    loadFloat(locations.get("spotLights[" + i + "].pointLight.attenuation.constant"), spotLight.getPointLight().getAttenuation().getConstant());
    loadFloat(locations.get("spotLights[" + i + "].pointLight.attenuation.linear"), spotLight.getPointLight().getAttenuation().getLinear());
    loadFloat(locations.get("spotLights[" + i + "].pointLight.attenuation.exponent"), spotLight.getPointLight().getAttenuation().getExponent());
    loadVector3f(locations.get("spotLights[" + i + "].pointLight.position"), spotLight.getPointLight().getPosition());
    loadFloat(locations.get("spotLights[" + i + "].pointLight.range"), spotLight.getPointLight().getRange());
    loadVector3f(locations.get("spotLights[" + i + "].direction"), spotLight.getDirection());
    loadFloat(locations.get("spotLights[" + i + "].cutoff"), spotLight.getCutoff());
  }

  public void loadSpecularReflection(SpecularReflection specularReflection) {
    loadFloat(locations.get(SPECULAR_INTENSITY), specularReflection.getIntensity());
    loadFloat(locations.get(SPECULAR_POWER), specularReflection.getPower());
  }

  public void loadEyePos(Vector3f vector3f) {
    loadVector3f(locations.get(EYE_POS), vector3f);
  }

}
