package org.sedly.sigh.math;

import org.sedly.sigh.math.rotation.LookAtRotation;

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

  private static Matrix4f translation(Vector3f position) {
    return Transformation.builder()
        .setTranslation(position.negate())
        .build()
        .transform();
  }



  public Matrix4f view() {
    return new LookAtRotation(forward, up).rotate().mult(translation(position));
  }

}
