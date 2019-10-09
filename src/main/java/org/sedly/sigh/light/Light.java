package org.sedly.sigh.light;

import org.sedly.sigh.math.Vector3f;

public class Light {

  private Vector3f direction;

  private float intensity;

  private Vector3f color;

  public Light(Vector3f direction, Vector3f color, float intensity) {
    this.direction = direction;
    this.color = color;
    this.intensity = intensity;
  }

  public Vector3f getDirection() {
    return direction;
  }

  public Vector3f getColor() {
    return color;
  }

  public float getIntensity() {
    return intensity;
  }
}
