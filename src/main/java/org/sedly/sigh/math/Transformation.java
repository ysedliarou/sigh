package org.sedly.sigh.math;

import org.sedly.sigh.math.rotation.QuaternionRotation;
import org.sedly.sigh.math.rotation.Rotation;

/**
 * The direction of vector rotation is counterclockwise if angle (in radians) is positive (e.g. 90°),
 * and clockwise if angle is negative (e.g. −90°).
 */
public final class Transformation {

  // --------------- BUILDER ---------------

  public static final class Builder {

    private Vector3f translation = Vector3f.ZERO;
    private Vector3f scaling = Vector3f.XYZ;
    private Rotation rotation = QuaternionRotation.DEFAULT;

    public Builder setTranslation(final Vector3f translation) {
      if (translation != null) {
        this.translation = translation;
      }
      return this;
    }

    public Builder setScaling(final Vector3f scaling) {
      if (scaling != null) {
        this.scaling = scaling;
      }
      return this;
    }

    public Builder setRotation(final Rotation rotation) {
      if (rotation != null) {
        this.rotation = rotation;
      }
      return this;
    }

    private Builder() {
      super();
    }

    public Transformation build() {
      return new Transformation(this);
    }

  }

  public Builder toBuilder() {
    return new Builder()
        .setTranslation(translation)
        .setRotation(rotation)
        .setScaling(scaling);
  }

  public static Builder builder() {
    return new Builder();
  }

  // --------------- PROPERTIES ---------------

  private Vector3f translation;

  private Vector3f scaling;

  private Rotation rotation;

  // --------------- GETTERS ---------------

  public Vector3f getTranslation() {
    return translation;
  }

  public Vector3f getScaling() {
    return scaling;
  }

  public Rotation getRotation() {
    return rotation;
  }

  // --------------- CONSTRUCTORS ---------------

  private Transformation() {
    super();
  }

  private Transformation(final Builder builder) {
    this.translation = builder.translation;
    this.scaling = builder.scaling;
    this.rotation = builder.rotation;
  }

  // --------------- MATH ---------------

  private static Matrix4f translate(final Vector3f v) {
    return new Matrix4f(new float[][] {
        {1,         0,          0,          v.getX()},
        {0,         1,          0,          v.getY()},
        {0,         0,          1,          v.getZ()},
        {0,         0,          0,          1}
    });
  }

  private static Matrix4f scale(final Vector3f v) {
    return new Matrix4f(new float[][] {
        {v.getX(),  0,          0,          0},
        {0,         v.getY(),   0,          0},
        {0,         0,          v.getZ(),   0},
        {0,         0,          0,          1}
    });
  }

  private static Matrix4f transform(Matrix4f translation, Matrix4f scale, Matrix4f rotation) {
    return translation.mult(rotation.mult(scale));
  }

  // --------------- METHODS ---------------

  public Matrix4f transform() {
    return transform(translate(translation), scale(scaling), rotation.rotate());
  }

}
