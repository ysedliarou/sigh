package org.sedly.sigh;

import org.sedly.sigh.math.Matrix4f;
import org.sedly.sigh.math.Vector3f;

public class Test {

  public static void main(String[] args) {
    float[][] asd = new float[][] {
            {1,2,3,4},
            {1,2,3,4},
            {1,2,3,4},
            {1,2,3,4}
    };

    Matrix4f m = new Matrix4f(asd);

    System.out.println(m);
    System.out.println(m.trans());
  }

}
