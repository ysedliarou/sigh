package org.sedly.sigh.shader;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.function.BiConsumer;
import org.sedly.sigh.light.DirectionalLight;
import org.sedly.sigh.light.PointLight;
import org.sedly.sigh.light.SpecularReflection;
import org.sedly.sigh.light.SpotLight;
import org.sedly.sigh.math.Color;
import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Vector3f;
import org.sedly.sigh.util.ShaderUtil;

public class PhongShader extends ShaderProgram {

  private static final String VS = "phongVertex.glsl";
  private static final String FS = "phongFragment.glsl";

  public static final String TRANSFORMATION_MATRIX = "transformationMatrix";
  public static final String PROJECTION_MATRIX = "projectionMatrix";
  public static final String VIEW_MATRIX = "viewMatrix";

  public static final String EYE_POS = "eyePos";

  public static final String BASE_COLOR = "baseColor";
  public static final String AMBIENT_LIGHT = "ambientLight";

  public PhongShader() {
    super(VS, FS);
  }

  public static final int MAX_POINT_LIGHTS = 4;

  public static final int MAX_SPOT_LIGHTS = 4;

  private Map<Class<?>, BiConsumer<Integer, Object>> loaders = ImmutableMap
      .<Class<?>, BiConsumer<Integer, Object>>builder()
      .put(Float.class, (s, o) -> loadFloat(s, Float.class.cast(o)))
      .put(Matrix4f.class, (s, o) -> loadMatrix4f(s, Matrix4f.class.cast(o)))
      .put(Vector3f.class, (s, o) -> loadVector3f(s, Vector3f.class.cast(o)))
      .put(Color.class, (s, o) -> loadColor(s, Color.class.cast(o)))
      .build();

  @Override
  protected void bind() {
    bind(0, POSITION);
    bind(1, TEX_COORD);
    bind(2, NORMAL);
  }

  public void loadTransformationMatrix(Matrix4f matrix4f) {
    loadMatrix4f(getUniformLocation(TRANSFORMATION_MATRIX), matrix4f);
  }

  public void loadProjectionMatrix(Matrix4f matrix4f) {
    loadMatrix4f(getUniformLocation(PROJECTION_MATRIX), matrix4f);
  }

  public void loadViewMatrix(Matrix4f matrix4f) {
    loadMatrix4f(getUniformLocation(VIEW_MATRIX), matrix4f);
  }

  public void loadEyePos(Vector3f vector3f) {
    loadVector3f(getUniformLocation(EYE_POS), vector3f);
  }

  public void loadBaseColor(Color color) {
    loadColor(getUniformLocation(BASE_COLOR), color);
  }

  public void loadAmbientLight(Vector3f vector3f) {
    loadVector3f(getUniformLocation(AMBIENT_LIGHT), vector3f);
  }

  public void loadDirectionalLight(DirectionalLight directionalLight) {
    load(ShaderUtil.toMap(directionalLight));
  }

  public void loadPointLight(PointLight pointLight, int i) {
    load(ShaderUtil.toMap(pointLight, i));
  }

  public void loadSpotLight(SpotLight spotLight, int i) {
    load(ShaderUtil.toMap(spotLight, i));
  }

  public void loadSpecularReflection(SpecularReflection specularReflection) {
    load(ShaderUtil.toMap(specularReflection));
  }

  private void load(Map<String, Object> map) {
    for (Map.Entry<String, Object> e : map.entrySet()) {
      BiConsumer<Integer, Object> loader = loaders.get(e.getValue().getClass());
      loader.accept(getUniformLocation(e.getKey()), e.getValue());
    }
  }

}
