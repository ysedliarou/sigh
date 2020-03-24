package org.sedly.sigh.light;

import org.sedly.sigh.math.Vector3f;

public class DirectionalLight {

  public static final DirectionalLight DEFAULT = new DirectionalLight(BaseLight.DEFAULT, Vector3f.ZERO);

  private BaseLight baseLight;

  // TODO: rename to position
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
