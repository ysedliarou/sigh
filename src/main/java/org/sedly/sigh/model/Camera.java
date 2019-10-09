package org.sedly.sigh.model;

import org.sedly.sigh.math.Vector3f;

public class Camera {

  private Vector3f position;

  private Vector3f forward;

  private Vector3f up;

  public Camera() {
    this(new Vector3f(-2, 4, 20),
        new Vector3f(0.4f, 0, -1),
        Vector3f.UNIT_Y);
  }

  public Camera(Vector3f position, Vector3f forward, Vector3f up) {
    this.position = position;
    this.forward = forward.normalize();
    this.up = up.normalize();
  }

  public void move(Vector3f direction, float amount) {
    position = position.add(direction.scale(amount));
  }

  public Vector3f getForward() {
    return forward;
  }

  public void setForward(Vector3f forward) {
    this.forward = forward;
  }

  public Vector3f getUp() {
    return up;
  }

  public void setUp(Vector3f up) {
    this.up = up;
  }

  public void rotateY(float angle) {
    Vector3f haxis = Vector3f.UNIT_Y.cross(forward).normalize();
    forward = forward.rotate(Vector3f.UNIT_Y, angle).normalize();
    up = forward.cross(haxis).normalize();
  }

  public Vector3f getPosition() {
    return position;
  }
}
