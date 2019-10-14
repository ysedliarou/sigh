package org.sedly.sigh.light;

public class SpecularReflection {

  public static final SpecularReflection DEFAULT = new SpecularReflection(0, 0);

  private float intensity;
  private float power;

  public SpecularReflection(float intensity, float power) {
    this.intensity = intensity;
    this.power = power;
  }

  public float getIntensity() {
    return intensity;
  }

  public float getPower() {
    return power;
  }
}
