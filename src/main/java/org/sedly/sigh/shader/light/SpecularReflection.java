package org.sedly.sigh.shader.light;

public class SpecularReflection {

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
