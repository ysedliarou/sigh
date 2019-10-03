package org.sedly.sigh.shader.light;

import org.sedly.sigh.math.Vector3f;

public class DirectionalLight {

  private BaseLight baseLight;

  private Vector3f direction;

  public DirectionalLight(BaseLight baseLight, Vector3f direction) {
    this.baseLight = baseLight;
    this.direction = direction;
  }

  public BaseLight getBaseLight() {
    return baseLight;
  }

  public Vector3f getDirection() {
    return direction;
  }
}
