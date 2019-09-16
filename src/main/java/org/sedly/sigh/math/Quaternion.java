package org.sedly.sigh.math;


import static org.sedly.sigh.math.MathUtil.NUMBER_FORMAT;

import java.text.MessageFormat;

public class Quaternion {

  // --------------- CONSTANTS ---------------

  public static final Quaternion ZERO = new Quaternion(0, 0, 0, 0);

  public static final Quaternion UNIT = new Quaternion(0, 0, 0, 1);

  // --------------- PROPERTIES ---------------

  private final float x, y, z, w;

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

  public float getW() {
    return w;
  }

  public Vector3f getDirection() {
    return new Vector3f(x, y, z);
  }

  // --------------- CONSTRUCTORS ---------------

  private Quaternion(float x, float y, float z, float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public Quaternion(Vector3f axis, float angle) {
    Vector3f normal = axis.normalize();
    float sinHalfAngle = (float) Math.sin(angle / 2);
    float cosHalfAngle = (float) Math.cos(angle / 2);

    this.x = normal.getX() * sinHalfAngle;
    this.y = normal.getY() * sinHalfAngle;
    this.z = normal.getZ() * sinHalfAngle;
    this.w = cosHalfAngle;
  }

  // --------------- MATH ---------------

  private static float dot(Quaternion q1, Quaternion q2) {
    return q1.x * q2.x + q1.y * q2.y + q1.z * q2.z + q1.w * q2.w;
  }

  private static Quaternion mult(Quaternion q1, Quaternion q2) {
    float x = q1.x * q2.w + q1.y * q2.z - q1.z * q2.y + q1.w * q2.x;
    float y = -q1.x * q2.z + q1.y * q2.w + q1.z * q2.x + q1.w * q2.y;
    float z = q1.x * q2.y - q1.y * q2.x + q1.z * q2.w + q1.w * q2.z;
    float w = -q1.x * q2.x - q1.y * q2.y - q1.z * q2.z + q1.w * q2.w;

    return new Quaternion(x, y, z, w);
  }

  private static Quaternion mult(Quaternion q, Vector3f v) {
    float w = -q.x * v.getX() - q.y * v.getY() - q.z * v.getZ();
    float x = q.w * v.getX() + q.y * v.getZ() - q.z * v.getY();
    float y = q.w * v.getY() + q.z * v.getX() - q.x * v.getZ();
    float z = q.w * v.getZ() + q.x * v.getY() - q.y * v.getX();

    return new Quaternion(x, y, z, w);
  }

  private static Quaternion scale(Quaternion q, float a) {
    return new Quaternion(q.x * a, q.y * a, q.z * a, q.w * a);
  }

  private static Quaternion sub(Quaternion q1, Quaternion q2) {
    return new Quaternion(q1.x - q2.x, q1.y - q2.y, q1.z - q2.z, q1.w - q2.w);
  }

  private static Quaternion add(Quaternion q1, Quaternion q2) {
    return new Quaternion(q1.x + q2.x, q1.y + q2.y, q1.z + q2.z, q1.w + q2.w);
  }

  // --------------- METHODS ---------------

  public float length() {
    return (float) Math.sqrt(x * x + y * y + z * z + w * w);
  }

  public Quaternion normalize() {
    if (ZERO.equals(this)) {
      throw new IllegalStateException("Zero quaternion.");
    }
    float length = length();
    if (length == 1.0f) {
      return copy();
    }
    return new Quaternion(x / length, y / length, z / length, w / length);
  }

  public Quaternion conjugate() {
    return new Quaternion(-x, -y, -z, w);
  }

  public float dot(Quaternion q) {
    return dot(this, q);
  }

  public Quaternion scale(float a) {
    return scale(this, a);
  }

  public Quaternion mult(Quaternion q) {
    return mult(this, q);
  }

  public Quaternion mult(Vector3f v) {
    return mult(this, v);
  }

  public Vector3f rotate(Vector3f v) {
    return mult(v).mult(conjugate()).getDirection();
  }

  public Quaternion sub(Quaternion q) {
    return sub(this, q);
  }

  public Quaternion add(Quaternion q) {
    return add(this, q);
  }

  public Vector3f forward() {
    return rotate(Vector3f.UNIT_Z);
  }

  public Vector3f backward() {
    return rotate(Vector3f.UNIT_Z.negate());
  }

  public Vector3f up() {
    return rotate(Vector3f.UNIT_Y);
  }

  public Vector3f down() {
    return rotate(Vector3f.UNIT_Y.negate());
  }

  public Vector3f right() {
    return rotate(Vector3f.UNIT_X);
  }

  public Vector3f left() {
    return rotate(Vector3f.UNIT_X.negate());
  }

  public Quaternion copy() {
    return new Quaternion(x, y, z, w);
  }

  public Matrix4f rotation() {
    return Transformation.builder().setRotation(this).build().transform();
  }

  // --------------- COMMON ---------------

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Quaternion)) {
      return false;
    }
    if (this == o) {
      return true;
    }

    Quaternion other = (Quaternion) o;
    if (Float.compare(x, other.x) != 0) {
      return false;
    }
    if (Float.compare(y, other.y) != 0) {
      return false;
    }
    if (Float.compare(z, other.z) != 0) {
      return false;
    }
    if (Float.compare(w, other.w) != 0) {
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
    hash += PRIME * hash + Float.floatToIntBits(w);
    return hash;
  }

  @Override
  public String toString() {
    String fx = NUMBER_FORMAT.get().format(x);
    String fy = NUMBER_FORMAT.get().format(y);
    String fz = NUMBER_FORMAT.get().format(z);
    String fw = NUMBER_FORMAT.get().format(z);
    return MessageFormat.format("Quaternion[x={0},y={1},z={2},w={3}]", fx, fy, fz, fw);
  }

}