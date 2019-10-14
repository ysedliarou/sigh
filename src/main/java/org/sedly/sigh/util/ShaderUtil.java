package org.sedly.sigh.util;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.sedly.sigh.light.Attenuation;
import org.sedly.sigh.light.BaseLight;
import org.sedly.sigh.light.DirectionalLight;
import org.sedly.sigh.light.PointLight;
import org.sedly.sigh.light.SpecularReflection;
import org.sedly.sigh.light.SpotLight;

public class ShaderUtil {

  public static final String DELIMITER = ".";

  public static final String COLOR = "color";
  public static final String INTENSITY = "intensity";
  public static final String DIRECTION = "direction";
  public static final String POSITION = "position";
  public static final String RANGE = "range";
  public static final String CUTOFF = "cutoff";
  public static final String POWER = "power";


  public static final String CONSTANT = "constant";
  public static final String LINEAR = "linear";
  public static final String EXPONENT = "exponent";

  public static final String SPECULAR_REFLECTION = "specularReflection";
  public static final String BASE_LIGHT = "baseLight";
  public static final String DIRECTIONAL_LIGHT = "directionalLight";
  public static final String POINT_LIGHT = "pointLight";
  public static final String POINT_LIGHT_I = POINT_LIGHT + "s[%d]";
  public static final String ATTENUATION = "attenuation";
  public static final String SPOT_LIGHT = "spotLight";
  public static final String SPOT_LIGHT_I = SPOT_LIGHT + "s[%d]";


  public static Map<String, Object> toMap(BaseLight baseLight) {
    return ImmutableMap.<String, Object>builder()
        .put(String.join(DELIMITER, BASE_LIGHT, COLOR), baseLight.getColor())
        .put(String.join(DELIMITER, BASE_LIGHT, INTENSITY), baseLight.getIntensity())
        .build();
  }

  public static Map<String, Object> toMap(SpecularReflection specularReflection) {
    return ImmutableMap.<String, Object>builder()
        .put(String.join(DELIMITER, SPECULAR_REFLECTION, POWER), specularReflection.getPower())
        .put(String.join(DELIMITER, SPECULAR_REFLECTION, INTENSITY), specularReflection.getIntensity())
        .build();
  }

  public static Map<String, Object> toMap(DirectionalLight directionalLight) {

    Map<String, Object> baseLightMap = toMap(directionalLight.getBaseLight()).entrySet().stream()
        .collect(Collectors
            .toMap(entry -> String.join(DELIMITER, DIRECTIONAL_LIGHT, entry.getKey()),
                Map.Entry::getValue));

    return ImmutableMap.<String, Object>builder()
        .put(String.join(DELIMITER, DIRECTIONAL_LIGHT, DIRECTION), directionalLight.getDirection())
        .putAll(baseLightMap).build();
  }

  public static Map<String, Object> toMap(Attenuation attenuation) {
    return ImmutableMap.<String, Object>builder()
        .put(String.join(DELIMITER, ATTENUATION, CONSTANT), attenuation.getConstant())
        .put(String.join(DELIMITER, ATTENUATION, LINEAR), attenuation.getLinear())
        .put(String.join(DELIMITER, ATTENUATION, EXPONENT), attenuation.getExponent())
        .build();
  }

  public static Map<String, Object> toMap(PointLight pointLight) {

    Map<String, Object> baseLightMap = toMap(pointLight.getBaseLight()).entrySet().stream()
        .collect(Collectors.toMap(entry -> String.join(DELIMITER, POINT_LIGHT, entry.getKey()),
            Map.Entry::getValue));

    Map<String, Object> attenuationMap = toMap(pointLight.getAttenuation()).entrySet().stream()
        .collect(Collectors.toMap(entry -> String.join(DELIMITER, POINT_LIGHT, entry.getKey()),
            Map.Entry::getValue));

    return ImmutableMap.<String, Object>builder()
        .put(String.join(DELIMITER, POINT_LIGHT, POSITION), pointLight.getPosition())
        .put(String.join(DELIMITER, POINT_LIGHT, RANGE), pointLight.getRange())
        .putAll(baseLightMap)
        .putAll(attenuationMap)
        .build();
  }

  public static Map<String, Object> toMap(PointLight pointLight, int i) {

    String s = String.format(POINT_LIGHT_I, i);

    Map<String, Object> pointLightMap = toMap(pointLight).entrySet().stream()
        .collect(Collectors.toMap(entry -> entry.getKey().replace(POINT_LIGHT, s),
            Map.Entry::getValue));

    return pointLightMap;
  }

  public static Map<String, Object> toMap(SpotLight spotLight) {

    Map<String, Object> pointLightMap = toMap(spotLight.getPointLight()).entrySet().stream()
        .collect(Collectors.toMap(entry -> String.join(DELIMITER, SPOT_LIGHT, entry.getKey()),
            Map.Entry::getValue));

    return ImmutableMap.<String, Object>builder()
        .put(String.join(DELIMITER, SPOT_LIGHT, DIRECTION), spotLight.getDirection())
        .put(String.join(DELIMITER, SPOT_LIGHT, CUTOFF), spotLight.getCutoff())
        .putAll(pointLightMap)
        .build();
  }

  public static Map<String, Object> toMap(SpotLight spotLight, int i) {

    String s = String.format(SPOT_LIGHT_I, i);

    Map<String, Object> pointLightMap = toMap(spotLight).entrySet().stream()
        .collect(Collectors.toMap(entry -> entry.getKey().replace(SPOT_LIGHT, s),
            Map.Entry::getValue));

    return pointLightMap;
  }

}
