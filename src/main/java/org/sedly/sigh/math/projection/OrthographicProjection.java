package org.sedly.sigh.math.projection;

import org.sedly.sigh.math.Matrix4f;

public class OrthographicProjection implements Projection {

    public static final OrthographicProjection DEFAULT = new Builder().build();

    public static final float DEFAULT_HALF = 0.5f;

    private float near, far;

    private float left, right;

    private float bottom, top;

    private OrthographicProjection() {
        super();
    }

    private OrthographicProjection(Builder builder) {
        this.near = builder.near;
        this.far = builder.far;
        this.left = builder.left;
        this.right = builder.right;
        this.bottom = builder.bottom;
        this.top = builder.top;
    }

    public float getNear() {
        return near;
    }

    public float getFar() {
        return far;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    public float getTop() {
        return top;
    }

    public static class Builder {

        private float near = DEFAULT_NEAR;

        private float far = DEFAULT_FAR;

        private float left = - DEFAULT_HALF, right = DEFAULT_HALF;

        private float bottom = - DEFAULT_HALF, top = DEFAULT_HALF;

        private Builder() {
            super();
        }

        public Builder setDepth(float near, float far) {
            this.near = near;
            this.far = far;
            return this;
        }

        public Builder setWidth(float left, float right) {
            this.left = left;
            this.right = right;
            return this;
        }

        public Builder setHeight(float bottom, float top) {
            this.bottom = bottom;
            this.top = top;
            return this;
        }

        public OrthographicProjection build() {
            return new OrthographicProjection(this);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder().setWidth(left, right).setHeight(bottom, top).setDepth(near, far);
    }

    @Override
    public Matrix4f projection() {
        return projection(left, right, bottom, top, near, far);
    }

    private Matrix4f projection(float left, float right, float bottom, float top, float near, float far) {

        return new Matrix4f(new float[][] {
                {2.0f / (right - left),     0,                      0,                      -(right + left) / (right - left)   },
                {0,                         2.0f / (top - bottom),  0,                      -(top + bottom) / (top - bottom)   },
                {0,                         0,                      -2.0f / (far - near),   -(far + near) / (far - near)       },
                {0,                         0,                      0,                      1.0f                               }
        });
    }

}