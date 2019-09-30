package org.sedly.sigh.math;

public class Projection {

  private float near;

  private float far;

  private float fov;

  private int width;

  private int height;

  private Projection() {
    super();
  }

  private Projection(Builder builder) {
    this.near = builder.near;
    this.far = builder.far;
    this.fov = builder.fov;
    this.width = builder.width;
    this.height = builder.height;
  }

  public float getNear() {
    return near;
  }

  public float getFar() {
    return far;
  }

  public float getFov() {
    return fov;
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public static class Builder {

    private float near = 0.1f;

    private float far = 1000f;

    private float fov = 75f;

    private int width;

    private int height;

    private Builder() {
      super();
    }

    public Builder z(float near, float far) {
      this.near = near;
      this.far = far;
      return this;
    }

    public Builder xy(int width, int height) {
      this.width = width;
      this.height = height;
      return this;
    }

    public Builder setFov(float fov) {
      this.fov = fov;
      return this;
    }

    public Projection build() {
      return new Projection(this);
    }

  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder().xy(width, height).z(near, far).setFov(fov);
  }

  public Matrix4f projection() {
    return projection(width, height, near, far, fov);
  }

  private static Matrix4f projection(int width, int height, float near, float far, float fov) {
    float aspectRatio = width / height;
    float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
    float x_scale = y_scale / aspectRatio;
    float frustum_length = far - near;

    float m00 = x_scale;
    float m11 = y_scale;
    float m22 = -((far + near) / frustum_length);
    float m23 = -((2 * near * far) / frustum_length);
    float m32 = -1;

    return new Matrix4f(new float[][] {
        {m00, 0, 0, 0},
        {0, m11, 0, 0},
        {0, 0, m22, m23},
        {0, 0, m32, 0}
    });
  }

  public Matrix4f projection1() {
    float tanHalfFOV = (float) Math.tan(Math.toRadians(fov / 2));
    float zRange = near - far;
    float aspectRatio = width/height;

    float[][] arr = new float[][] {
      {1.0f / (tanHalfFOV * aspectRatio), 0, 0, 0 },
      { 0,  1.0f / tanHalfFOV, 0, 0},
      {0,  0,  (-near - far) / zRange,  2 * far * near / zRange},
      {0, 0, 1, 0}
    };
    return new Matrix4f(arr);
  }

}
