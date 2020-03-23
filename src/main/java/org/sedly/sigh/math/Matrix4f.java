package org.sedly.sigh.math;


import static org.sedly.sigh.util.MathUtil.NUMBER_FORMAT;

import java.util.Arrays;

public class Matrix4f {

  private abstract static class Operation {

    float[][] source, target;

    Operation(final float[][] source, final float[][] target) {
      this.source = source;
      this.target = target;
    }

    abstract float operate(int i, int j);
  }

  // --------------- CONSTANTS ---------------

  public static final int SIZE = 4;

  public static final Matrix4f IDENTITY = new Matrix4f(new float[][] {
      {1,         0,          0,          0},
      {0,         1,          0,          0},
      {0,         0,          1,          0},
      {0,         0,          0,          1}
  });

  // --------------- PROPERTIES ---------------

  private final float[][] matrix;

  // --------------- GETTERS ---------------

  public float[][] getMatrix() {
    return fill(createGetOperation(matrix));
  }

  // --------------- CONSTRUCTORS ---------------

  public Matrix4f(final float[][] matrix) {
    this(createGetOperation(matrix));
  }

  private Matrix4f(final Operation operation) {
    this.matrix = fill(operation);
  }

  // --------------- MATH ---------------

  private static Matrix4f div(final Matrix4f m, final float a) {
    if (a == 0.0f) {
      throw new IllegalStateException("Zero division.");
    }
    return new Matrix4f(new Operation(m.matrix, null) {
      @Override
      float operate(final int i, final int j) {
        return this.source[i][j] / a;
      }
    });
  }

  private static Matrix4f mult(final Matrix4f m, final float a) {
    return new Matrix4f(new Operation(m.matrix, null) {
      @Override
      float operate(final int i, final int j) {
        return this.source[i][j] * a;
      }
    });
  }

  private static Matrix4f sub(final Matrix4f m1, final Matrix4f m2) {
    return new Matrix4f(new Operation(m1.matrix, m2.matrix) {
      @Override
      float operate(final int i, final int j) {
        return this.source[i][j] - this.target[i][j];
      }
    });
  }

  private static Matrix4f add(final Matrix4f m1, final Matrix4f m2) {
    return new Matrix4f(new Operation(m1.matrix, m2.matrix) {
      @Override
      float operate(final int i, final int j) {
        return this.source[i][j] + this.target[i][j];
      }
    });
  }

  private static Vector3f mult(float[][] matrix, Vector3f v) {
    float x = matrix[0][0] * v.getX() + matrix[0][1] * v.getY() + matrix[0][2] * v.getZ() + matrix[0][3];
    float y = matrix[1][0] * v.getX() + matrix[1][1] * v.getY() + matrix[1][2] * v.getZ() + matrix[1][3];
    float z = matrix[2][0] * v.getX() + matrix[2][1] * v.getY() + matrix[2][2] * v.getZ() + matrix[2][3];
    return new Vector3f(x, y, z);
  }


  // --------------- METHODS ---------------

  private static Operation createGetOperation(final float[][] matrix) {
    return new Operation(matrix, null) {
      @Override
      float operate(final int i, final int j) {
        return this.source[i][j];
      }
    };
  }

  public float get(final int i, final int j) {
    return matrix[i][j];
  }

  private static float[][] fill(final Operation operation) {
    float[][] target = new float[SIZE][SIZE];
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        target[i][j] = operation.operate(i, j);
      }
    }
    return target;
  }

  public Matrix4f copy() {
    return new Matrix4f(matrix);
  }

  public Matrix4f mult(final float a) {
    return mult(this, a);
  }

  public Matrix4f div(final float a) {
    return div(this, a);
  }

  public Matrix4f add(final Matrix4f m) {
    return add(this, m);
  }

  public Matrix4f sub(final Matrix4f m) {
    return sub(this, m);
  }

  public Matrix4f mult(final Matrix4f m) {
    return new Matrix4f(new Operation(matrix, m.matrix) {
      @Override
      float operate(final int i, final int j) {
        return this.source[i][0] * this.target[0][j]
            + this.source[i][1] * this.target[1][j]
            + this.source[i][2] * this.target[2][j]
            + this.source[i][3] * this.target[3][j];
      }
    });
  }

  public Vector3f mult(final Vector3f v) {
    return mult(matrix, v);
  }

  public Matrix4f negate() {
    return mult(this, -1);
  }

  public static Matrix4f trans(final Matrix4f m) {
    return new Matrix4f(new Operation(m.matrix, null) {
      @Override
      float operate(final int i, final int j) {
        return this.source[j][i];
      }
    });
  }

  public Matrix4f trans() {
    return trans(this);
  }

  // --------------- COMMON ---------------

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Matrix4f)) {
      return false;
    }
    if (this == o) {
      return true;
    }
    Matrix4f other = (Matrix4f) o;
    return Arrays.deepEquals(matrix, other.matrix);
  }

  @Override
  public int hashCode() {
    final int PRIME = 37;
    int hash = PRIME;
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        hash += PRIME * hash + matrix[i][j];
      }
    }
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("Matrix4f[ \n");
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        builder.append(" ").append(NUMBER_FORMAT.get().format(matrix[i][j])).append(" ");
      }
      builder.append("\n");
    }
    return builder.append("]").toString();
  }

}

