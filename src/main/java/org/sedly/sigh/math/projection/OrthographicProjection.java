package org.sedly.sigh.math.projection;

import org.sedly.sigh.math.Matrix4f;

// TODO: fix me
public class OrthographicProjection implements Projection {

    private float near;

    private float far;

    private int width;

    private int height;

    private OrthographicProjection() {
        super();
    }

    private OrthographicProjection(Builder builder) {
        this.near = builder.near;
        this.far = builder.far;
        this.width = builder.width;
        this.height = builder.height;
    }

    public float getNear() {
        return near;
    }

    public float getFar() {
        return far;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public static class Builder {

        private float near = -100f;

        private float far = 100f;

        private int width;

        private int height;

        private Builder() {
            super();
        }

        public Builder z(float near, float far) {
            this.near = near;
            this.far = far;
            return this;
        }

        public Builder xy(int width, int height) {
            this.width = width;
            this.height = height;
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
        return new Builder().xy(width, height).z(near, far);
    }

    public Matrix4f projection() {
        return projection(width, height, near, far);
    }


    //https://solarianprogrammer.com/2013/05/22/opengl-101-matrices-projection-view-model/
    private Matrix4f projection(int width, int height, float near, float far) {

        float[][] m = new float[4][4];
        float depth = far - near;

        m[0][0] = -near / width;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = 0;
        m[1][0] = 0;
        m[1][1] = -near / height;
        m[1][2] = 0;
        m[1][3] = 0;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = 2 / depth;
        m[2][3] = 0;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return new Matrix4f(m);
    }

}