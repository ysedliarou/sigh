package org.sedly.sigh.math;

import org.sedly.sigh.math.rotation.QuaternionRotation;
import org.sedly.sigh.math.rotation.Rotation;

/**
 * The direction of vector rotation is counterclockwise if angle (in radians) is positive (e.g. 90°),
 * and clockwise if angle is negative (e.g. −90°).
 */
public final class Transformation {

  public enum Order {

    TRS {
      @Override
      protected Matrix4f execute(Matrix4f translation, Matrix4f scale, Matrix4f rotation) {
        return translation.mult(rotation.mult(scale));
      }
    },

    RT {
      @Override
      protected Matrix4f execute(Matrix4f translation, Matrix4f scale, Matrix4f rotation) {
        return rotation.mult(translation);
      }
    },

    NONE {
      @Override
      protected Matrix4f execute(Matrix4f translation, Matrix4f scale, Matrix4f rotation) {
        return Matrix4f.IDENTITY;
      }
    };

    protected abstract Matrix4f execute(Matrix4f translation, Matrix4f scale, Matrix4f rotation);
  }

  // --------------- BUILDER ---------------

  public static final class Builder {

    private Vector3f translation = Vector3f.ZERO;
    private Vector3f scaling = Vector3f.XYZ;
    private Rotation rotation = QuaternionRotation.DEFAULT;
    private Order order = Order.TRS;

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

    public Builder setOrder(final Order order) {
      if (order != null) {
        this.order = order;
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
        .setScaling(scaling)
        .setOrder(order);
  }

  public static Builder builder() {
    return new Builder();
  }

  // --------------- PROPERTIES ---------------

  private Vector3f translation;

  private Vector3f scaling;

  private Rotation rotation;

  private Order order;

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

  public Order getOrder() {
    return order;
  }

  // --------------- CONSTRUCTORS ---------------

  private Transformation() {
    super();
  }

  private Transformation(final Builder builder) {
    this.translation = builder.translation;
    this.scaling = builder.scaling;
    this.rotation = builder.rotation;
    this.order = builder.order;
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

  // --------------- METHODS ---------------

  public Matrix4f transform() {
    return transform(translate(translation), scale(scaling), rotation.rotate());
  }

  private Matrix4f transform(Matrix4f translation, Matrix4f scale, Matrix4f rotation) {
    return order.execute(translation, scale, rotation);
  }

}
