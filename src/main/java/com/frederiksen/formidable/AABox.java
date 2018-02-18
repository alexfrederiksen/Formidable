package com.frederiksen.formidable;

import com.frederiksen.formidable.utils.Utils;
import org.joml.Vector3f;


/**
 * Axis-Aligned box
 */
public class AABox {
    private Vector3f position;
    private Vector3f size;

    public static AABox createFromCorners(Vector3f p1, Vector3f p2) {
        return new AABox(p1.x, p1.y, p1.z,
                p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
    }

    public AABox(float x, float y, float z, float width, float height, float depth) {
        position = new Vector3f(x, y, z);
        size = new Vector3f(width, height, depth);
    }

    public AABox(Vector3f position, Vector3f size) {
        this.position = new Vector3f(position);
        this.size = new Vector3f(size);
    }

    public void set(Vector3f position, Vector3f size) {
        position.set(position);
        size.set(size);
    }

    public void set(float x, float y, float z, float width, float height, float depth) {
        position.set(x, y, z);
        size.set(width, height, depth);
    }

    public void setCorners(Vector3f p1, Vector3f p2) {
        set(p1.x, p1.y, p1.z,
                p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
    }

    public void setCorners(float x1, float y1, float z1, float x2, float y2, float z2) {
        set(x1, y1, z1, x2 - x1, y2 - y1, z2 - z1);
    }

    public float getMinX() {
        return Math.min(position.x, position.x + size.x);
    }

    public float getMaxX() {
        return Math.max(position.x, position.x + size.x);
    }

    public float getMinY() {
        return Math.min(position.y, position.y + size.y);
    }

    public float getMaxY() {
        return Math.max(position.y, position.y + size.y);
    }

    public float getMinZ() {
        return Math.min(position.z, position.z + size.z);
    }

    public float getMaxZ() {
        return Math.max(position.z, position.z + size.z);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getSize() {
        return size;
    }

    public boolean intersect(Vector3f point) {
        return (point.x >= getMinX() && point.x <= getMaxX()) &&
                (point.y >= getMinY() && point.y <= getMaxY()) &&
                (point.z >= getMinZ() && point.z <= getMaxY());
    }

    public boolean intersect(AABox box) {
        return (getMinX() <= box.getMaxX() && getMaxX() >= box.getMinX()) &&
                (getMinY() <= box.getMaxY() && getMaxY() >= box.getMinY()) &&
                (getMinZ() <= box.getMaxZ() && getMaxZ() >= box.getMinZ());
    }

    /**
     * Checks intersection and computes amount of intersect on each axis
     * @param box
     * @param collision intersection of each axis
     * @return true if there is intersection
     */
    public boolean intersect(AABox box, Vector3f collision) {
        collision.set(
                Utils.rangeIntersect(getMinX(), getMaxX(), box.getMinX(), box.getMaxX()),
                Utils.rangeIntersect(getMinY(), getMaxY(), box.getMinY(), box.getMaxY()),
                Utils.rangeIntersect(getMinZ(), getMaxZ(), box.getMinZ(), box.getMaxZ()));
        return collision.x > 0 && collision.y > 0 && collision.x > 0;
    }

}
