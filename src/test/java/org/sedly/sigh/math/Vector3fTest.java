package org.sedly.sigh.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Vector3fTest {

    private static final Vector3f V1 = new Vector3f(3, 7, 9);

    private static final Vector3f V2 = new Vector3f(4, -6, 1);

    @Test
    public void testGet() {
        assertEquals(3, V1.getX(), 0);
        assertEquals(7, V1.getY(), 0);
        assertEquals(9, V1.getZ(), 0);
    }

    @Test
    public void testEquals() {
        assertEquals(V1, V1);
        assertEquals(new Vector3f(3, 7, 9), V1);
        assertNotEquals(V2, V1);
        assertNotEquals(new Object(), V1);
    }

    @Test
    public void testAdd() {
        Vector3f expected = new Vector3f(7, 1, 10);
        assertEquals(expected, V1.add(V2));
    }

    @Test
    public void testSub() {
        assertEquals(new Vector3f(-1, 13, 8), V1.sub(V2));
        assertEquals(new Vector3f(1, -13, -8), V2.sub(V1));
    }

    @Test
    public void testScale() {
        assertEquals(V1, V1.scale(1));
        assertEquals(new Vector3f(6, 14, 18), V1.scale(2));
        assertEquals(new Vector3f(-6, -14, -18), V1.scale(-2));

        assertEquals(V1, V1.scale(2).scale(0.5f));
        assertEquals(V1, V1.scale(2).div(2));
    }

    @Test
    public void testNegate() {
        assertEquals(new Vector3f(-3, -7, -9), V1.negate());
        assertEquals(new Vector3f(-4, 6, -1), V2.negate());
    }

}
