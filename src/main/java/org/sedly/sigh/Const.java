package org.sedly.sigh;

import org.sedly.sigh.light.Attenuation;
import org.sedly.sigh.light.BaseLight;
import org.sedly.sigh.light.DirectionalLight;
import org.sedly.sigh.light.PointLight;
import org.sedly.sigh.light.SpecularReflection;
import org.sedly.sigh.light.SpotLight;
import org.sedly.sigh.math.Color;
import org.sedly.sigh.math.Vector3f;

public class Const {

  public static Color BASE_COLOR = Color.WHITE;

  public static Vector3f AMBIENT_LIGHT = Vector3f.ZERO;

  public static SpecularReflection SPECULAR_REFLECTION = new SpecularReflection(1f, 150f);

  public static DirectionalLight DIRECTIONAL_LIGHT_0 = new DirectionalLight(
      new BaseLight(Color.WHITE, 0.4f),
      new Vector3f(0, 0,1)
  );

  public static PointLight POINT_LIGHT_0 = new PointLight(
      new BaseLight(Color.RED, 1f),
      new Attenuation(0.021f, 0.01f, 0.01f),
      new Vector3f(-15, 5, -15),
      30
  );

  public static PointLight POINT_LIGHT_1 = new PointLight(
      new BaseLight(Color.BLUE, 1f),
      new Attenuation(0.021f, 0.01f, 0.01f),
      new Vector3f(0, 5, -15),
      30
  );

  public static PointLight POINT_LIGHT_2 = new PointLight(
      new BaseLight(new Color(1, 1, 0), 1f),
      new Attenuation(0.021f, 0.01f, 0.01f),
      new Vector3f(15, -5, -15),
      30
  );

  public static SpotLight SPOT_LIGHT_0 = new SpotLight(
      new PointLight(
            new BaseLight(Color.GREEN, 1f),
            new Attenuation(0.001f, 0.01f, 0.01f),
            new Vector3f(-7, -2, -5),
            30
      ),
      new Vector3f(0, 0, -1),
      0.98f
  );

}
