package org.sedly.sigh.math;


import static org.sedly.sigh.math.MathUtil.NUMBER_FORMAT;

import java.text.MessageFormat;

public class Vector3f {

  // --------------- CONSTANTS ---------------

  public static final Vector3f ZERO = new Vector3f(0, 0, 0);

  public static final Vector3f XYZ = new Vector3f(1, 1, 1);

  public static final Vector3f UNIT_X = new Vector3f(1, 0, 0);
  public static final Vector3f UNIT_Y = new Vector3f(0, 1, 0);
  public static final Vector3f UNIT_Z = new Vector3f(0, 0, 1);

  // --------------- PROPERTIES ---------------

  private final float x, y, z;

  // --------------- GETTERS ---------------

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getZ() {
    return z;
  }

  // --------------- CONSTRUCTORS ---------------

  public Vector3f(final float x, final float y, final float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  // --------------- MATH ---------------

  private static Vector3f add(final Vector3f v1, final Vector3f v2) {
    return new Vector3f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
  }

  private static Vector3f sub(final Vector3f v1, final Vector3f v2) {
    return new Vector3f(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
  }

  private static Vector3f scale(final Vector3f v, final float a) {
    return new Vector3f(v.x * a, v.y * a, v.z * a);
  }

  private static Vector3f div(final Vector3f v, final float a) {
    if (a == 0.0f) {
      throw new IllegalStateException("Zero division.");
    }
    return new Vector3f(v.x / a, v.y / a, v.z / a);
  }

  private static float dot(final Vector3f v1, final Vector3f v2) {
    return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
  }

  private static Vector3f cross(final Vector3f v1, final Vector3f v2) {
    float x = v1.y * v2.z - v1.z * v2.y;
    float y = v1.z * v2.x - v1.x * v2.z;
    float z = v1.x * v2.y - v1.y * v2.x;
    return new Vector3f(x, y, z);
  }

  private static float angle(final Vector3f a, final Vector3f b) {
    if (ZERO.equals(a) || ZERO.equals(b)) {
      throw new IllegalStateException("Zero vector.");
    }
    float dls = dot(a, b) / (a.magnitude() * b.magnitude());
    return (float) Math.acos(MathUtil.clamp(dls, -1.0f, 1.0f));
  }

  // --------------- METHODS ---------------


  public Vector3f add(final Vector3f v) {
    return add(this, v);
  }

  public Vector3f sub(final Vector3f v) {
    return sub(this, v);
  }

  public Vector3f scale(final float a) {
    return scale(this, a);
  }

  public Vector3f div(final float a) {
    return div(this, a);
  }

  public float magnitude() {
    return (float) Math.sqrt(x * x + y * y + z * z);
  }

  public float dot(final Vector3f v) {
    return dot(this, v);
  }

  public Vector3f cross(final Vector3f v) {
    return cross(this, v);
  }

  public Vector3f normalize() {
    if (ZERO.equals(this)) {
      throw new IllegalStateException("Zero vector.");
    }
    float length = magnitude();
    if (length == 1.0f) {
      return copy();
    }
    return div(this, length);
  }

  public float angle(final Vector3f v) {
    return angle(this, v);
  }

  public Vector3f negate() {
    return scale(this, -1.0f);
  }

  public Vector3f rotate(final Vector3f axis, final float angle) {
    return rotate(new Quaternion(axis, angle));
  }

  public Vector3f rotate(final Quaternion rotation) {
    return rotation.rotate(this);
  }

  public Vector3f copy() {
    return new Vector3f(x, y, z);
  }

  public Matrix4f translation() {
    return Transformation.builder().setTranslation(this).build().transform();
  }

  public Matrix4f scale() {
    return Transformation.builder().setScaling(this).build().transform();
  }

  // --------------- COMMON ---------------

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Vector3f)) {
      return false;
    }
    if (this == o) {
      return true;
    }

    Vector3f other = (Vector3f) o;
    if (Float.compare(x, other.x) != 0) {
      return false;
    }
    if (Float.compare(y, other.y) != 0) {
      return false;
    }
    if (Float.compare(z, other.z) != 0) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int PRIME = 37;
    int hash = 1;
    hash += PRIME * hash + Float.floatToIntBits(x);
    hash += PRIME * hash + Float.floatToIntBits(y);
    hash += PRIME * hash + Float.floatToIntBits(z);
    return hash;
  }

  @Override
  public String toString() {
    String fx = NUMBER_FORMAT.get().format(x);
    String fy = NUMBER_FORMAT.get().format(y);
    String fz = NUMBER_FORMAT.get().format(z);
    return MessageFormat.format("Vector3f[x={0},y={1},z={2}]", fx, fy, fz);
  }

}
