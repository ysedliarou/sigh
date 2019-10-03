package org.sedly.sigh.math;

import org.lwjgl.system.CallbackI.B;

public class View {

  private Vector3f position;

  private Vector3f forward;

  private Vector3f up;

  private View() {
    super();
  }

  private View(Builder builder) {
    this.position = builder.position;
    this.forward = builder.forward;
    this.up = builder.up;
  }

  public Vector3f getPosition() {
    return position;
  }

  public Vector3f getForward() {
    return forward;
  }

  public Vector3f getUp() {
    return up;
  }

  public static class Builder {

    private Vector3f position;

    private Vector3f forward;

    private Vector3f up;

    public Builder position(Vector3f position) {
      this.position = position;
      return this;
    }

    public Builder forward(Vector3f forward) {
      this.forward = forward;
      return this;
    }

    public Builder up(Vector3f up) {
      this.up = up;
      return this;
    }

    public View build() {
      return new View(this);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .position(position)
        .forward(forward)
        .up(up);
  }

  private Matrix4f translation() {
    return Transformation.builder()
        .setTranslation(position.negate()) // TODO check
        .build()
        .transformation();
  }

  public Matrix4f view() {
    Matrix4f translation = translation();

    Vector3f right = up.cross(forward).normalize(); // TODO check

    Matrix4f rotation = new Matrix4f(new float[][] {
        {right.getX(),     right.getY(),     right.getZ(),     0},
        {up.getX(),        up.getY(),        up.getZ(),        0},
        {forward.getX(),   forward.getY(),   forward.getZ(),   0},
        {0,                0,                0,                1}
    });

    return rotation.mult(translation);
  }

}
