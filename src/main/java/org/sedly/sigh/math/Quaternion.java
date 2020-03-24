package org.sedly.sigh.math;


import org.sedly.sigh.math.rotation.QuaternionRotation;

import static org.sedly.sigh.util.MathUtil.NUMBER_FORMAT;

import java.text.MessageFormat;

public class Quaternion {

  // --------------- CONSTANTS ---------------

  private static final Quaternion ZERO = new Quaternion(0, 0, 0, 0);

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

  private Quaternion(final float x, final float y, final float z, final float w) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }

  public Quaternion(final Vector3f axis, final float angle) {
    Vector3f normal = axis.normalize();
    float sinHalfAngle = (float) Math.sin(angle / 2);
    float cosHalfAngle = (float) Math.cos(angle / 2);

    this.x = normal.getX() * sinHalfAngle;
    this.y = normal.getY() * sinHalfAngle;
    this.z = normal.getZ() * sinHalfAngle;
    this.w = cosHalfAngle;
  }

  // --------------- MATH ---------------

  private static float dot(final Quaternion q1, final Quaternion q2) {
    return q1.x * q2.x + q1.y * q2.y + q1.z * q2.z + q1.w * q2.w;
  }

  private static Quaternion mult(final Quaternion q1, final Quaternion q2) {
    float x = q1.x * q2.w + q1.y * q2.z - q1.z * q2.y + q1.w * q2.x;
    float y = -q1.x * q2.z + q1.y * q2.w + q1.z * q2.x + q1.w * q2.y;
    float z = q1.x * q2.y - q1.y * q2.x + q1.z * q2.w + q1.w * q2.z;
    float w = -q1.x * q2.x - q1.y * q2.y - q1.z * q2.z + q1.w * q2.w;

    return new Quaternion(x, y, z, w);
  }

  private static Quaternion mult(final Quaternion q, final Vector3f v) {
    float w = -q.x * v.getX() - q.y * v.getY() - q.z * v.getZ();
    float x = q.w * v.getX() + q.y * v.getZ() - q.z * v.getY();
    float y = q.w * v.getY() + q.z * v.getX() - q.x * v.getZ();
    float z = q.w * v.getZ() + q.x * v.getY() - q.y * v.getX();

    return new Quaternion(x, y, z, w);
  }

  private static Quaternion scale(final Quaternion q, final float a) {
    return new Quaternion(q.x * a, q.y * a, q.z * a, q.w * a);
  }

  private static Quaternion sub(final Quaternion q1, final Quaternion q2) {
    return new Quaternion(q1.x - q2.x, q1.y - q2.y, q1.z - q2.z, q1.w - q2.w);
  }

  private static Quaternion add(final Quaternion q1, final Quaternion q2) {
    return new Quaternion(q1.x + q2.x, q1.y + q2.y, q1.z + q2.z, q1.w + q2.w);
  }

  private static Quaternion slerp(final Quaternion q1, final Quaternion q2, final float i) {
    Quaternion n1 = q1.normalize(), n2 = q2.normalize();

    float dot = n1.dot(n2);
    if (dot < 0.0f) {
      n2 = n2.negate();
      dot = -dot;
    }

    final float THRESHOLD = 0.9995f;
    if (dot > THRESHOLD) {
      return n2.sub(n1).scale(i).add(n1).normalize();
    }

    float alpha = (float) Math.acos(dot);
    float beta = i * alpha;

    float sinAlpha = (float) Math.sin(alpha);
    float sinBeta = (float) Math.sin(beta);

    float s1 = (float) Math.cos(beta) - dot * sinBeta / sinAlpha;
    float s2 = sinBeta / sinAlpha;

    return n1.scale(s1).add(n2.scale(s2));
  }

  // --------------- METHODS ---------------

  public float magnitude() {
    return (float) Math.sqrt(x * x + y * y + z * z + w * w);
  }

  public Quaternion normalize() {
    if (ZERO.equals(this)) {
      throw new IllegalStateException("Zero quaternion.");
    }
    float length = magnitude();
    if (length == 1.0f) {
      return copy();
    }
    return new Quaternion(x / length, y / length, z / length, w / length);
  }

  public Quaternion conjugate() {
    return new Quaternion(-x, -y, -z, w);
  }

  public Quaternion negate() {
    return scale(-1);
  }

  public float dot(final Quaternion q) {
    return dot(this, q);
  }

  public Quaternion scale(final float a) {
    return scale(this, a);
  }

  public Quaternion slerp(final Quaternion q, final float i) {
    return slerp(this, q, i);
  }

  public Quaternion mult(final Quaternion q) {
    return mult(this, q);
  }

  public Quaternion mult(final Vector3f v) {
    return mult(this, v);
  }

  public Vector3f rotate(final Vector3f v) {
    return mult(v).mult(conjugate()).getDirection();
  }

  public Quaternion sub(final Quaternion q) {
    return sub(this, q);
  }

  public Quaternion add(final Quaternion q) {
    return add(this, q);
  }

  public Vector3f forward() {
    return rotate(Vector3f.UNIT_Z);
  }

  public Quaternion copy() {
    return new Quaternion(x, y, z, w);
  }

  // --------------- COMMON ---------------

  @Override
  public boolean equals(final Object o) {
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
