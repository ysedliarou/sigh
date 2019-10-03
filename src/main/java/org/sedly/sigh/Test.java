package org.sedly.sigh;

import org.sedly.sigh.math.Vector3f;

public class Test {

  public static void main(String[] args) {
    Vector3f cross = Vector3f.UNIT_X.cross(Vector3f.UNIT_Y);
    System.out.println(cross);
  }

}
