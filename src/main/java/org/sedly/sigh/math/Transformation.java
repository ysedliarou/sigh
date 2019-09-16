package org.sedly.sigh.math;

/**
 * The direction of vector rotation is counterclockwise if angle (in radians) is positive (e.g. 90°),
 * and clockwise if angle is negative (e.g. −90°).
 */
public class Transformation {

  public static class Builder {

    private Vector3f translation = Vector3f.ZERO;
    private Vector3f scale = Vector3f.UNIT;
    private Quaternion rotation = Quaternion.UNIT;

    public Builder setTranslation(Vector3f translation) {
      if (translation != null) {
        this.translation = translation;
      }
      return this;
    }

    public Builder setScale(Vector3f scale) {
      if (scale != null) {
        this.scale =  scale;
      }
      return this;
    }

    public Builder setRotation(Quaternion rotation) {
      if (rotation != null) {
        this.rotation = rotation;
      }
      return this;
    }

    private Builder() {
    }

    public Transformation build() {
      return new Transformation(this);
    }

  }

  public Builder toBuilder() {
    return new Builder()
        .setTranslation(translation)
        .setRotation(rotation)
        .setScale(scale);
  }

  // --------------- PROPERTIES ---------------

  private Vector3f translation;

  private Vector3f scale;

  private Quaternion rotation;

  // --------------- GETTERS ---------------

  public Vector3f getTranslation() {
    return translation;
  }

  public Vector3f getScale() {
    return scale;
  }

  public Quaternion getRotation() {
    return rotation;
  }

  // --------------- CONSTRUCTORS ---------------

  private Transformation() {
    super();
  }

  private Transformation(Builder builder) {
    this.translation = builder.translation;
    this.scale = builder.scale;
    this.rotation = builder.rotation;
  }

  // --------------- MATH ---------------

  private static Matrix4f translation(Vector3f v) {
    return new Matrix4f(new float[][] {
        {1,         0,          0,          v.getX()},
        {0,         1,          0,          v.getY()},
        {0,         0,          1,          v.getZ()},
        {0,         0,          0,          1}
    });
  }

  private static Matrix4f scale(Vector3f v) {
    return new Matrix4f(new float[][] {
        {v.getX(),  0,          0,          0},
        {0,         v.getY(),   0,          0},
        {0,         0,          v.getZ(),   0},
        {0,         0,          0,          1}
    });
  }

  private static Matrix4f rotation(Quaternion q) {
    return new Matrix4f(new float[][] {
        {1 - 2 * q.getY() * q.getY() - 2 * q.getZ() * q.getZ(),   2 * q.getX() * q.getY() - 2 * q.getZ() * q.getW(),          2 * q.getX() * q.getZ() + 2 * q.getY() * q.getW(),          0},
        {2 * q.getX() * q.getY() + 2 * q.getZ() * q.getW(),       1 - 2 * q.getX() * q.getX() - 2 * q.getZ() * q.getZ(),      2 * q.getY() * q.getZ() - 2 * q.getX() * q.getW(),          0},
        {2 * q.getX() * q.getZ() - 2 * q.getY() * q.getW(),       2 * q.getY() * q.getZ() + 2 * q.getX() * q.getW(),          1 - 2 * q.getX() * q.getX() - 2 * q.getY() * q.getY(),      0},
        {0,                                                       0,                                                          0,                                                          1}
    });
  }

  // --------------- METHODS ---------------

  public static Builder builder() {
    return new Builder();
  }

  public Matrix4f transform() {
    Matrix4f rotation = rotation(this.rotation);
    Matrix4f scale = scale(this.scale);
    Matrix4f translation = translation(this.translation);

    return translation.mult(rotation.mult(scale));
  }

  public Vector3f transform(Vector3f v) {
    return transform().mult(v);
  }

}