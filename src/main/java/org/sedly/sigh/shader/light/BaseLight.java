package org.sedly.sigh.shader.light;

import org.sedly.sigh.math.Vector3f;

public class BaseLight {

  private Vector3f color;

  private float intensity;

  public BaseLight(Vector3f color, float intensity) {
    this.color = color;
    this.intensity = intensity;
  }

  public Vector3f getColor() {
    return color;
  }

  public float getIntensity() {
    return intensity;
  }

}
