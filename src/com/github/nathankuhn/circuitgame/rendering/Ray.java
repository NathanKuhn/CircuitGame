package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.engine.World;
import com.github.nathankuhn.circuitgame.utils.Vector3f;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

public class Ray {

    public static RayHit FindVoxelIntersection(Ray ray, World world, float maxDistance) {

        // find voxel ray starts in

        int x = (int)ray.origin.x;
        int y = (int)ray.origin.y;
        int z = (int)ray.origin.z;

        // constants find t required to increment x, y, or z by 1

        float deltaDistX = Math.abs(1.0f / ray.direction.x);
        float deltaDistY = Math.abs(1.0f / ray.direction.y);
        float deltaDistZ = Math.abs(1.0f / ray.direction.z);

        // find direction of ray on each axis and first intersection

        int stepX = ray.direction.x > 0 ? 1 : -1;
        int stepY = ray.direction.y > 0 ? 1 : -1;
        int stepZ = ray.direction.z > 0 ? 1 : -1;
        float tMaxX;
        float tMaxY;
        float tMaxZ;

        if (ray.direction.x > 0) {
            stepX = 1;
            tMaxX = deltaDistX * (1 - (ray.origin.x - x));
        } else {
            stepX = -1;
            tMaxX = deltaDistX * (ray.origin.x - x);
        }
        if (ray.direction.y > 0) {
            stepY = 1;
            tMaxY = deltaDistY * (1 - (ray.origin.y - y));
        } else {
            stepY = -1;
            tMaxY = deltaDistY * (ray.origin.y - y);
        }
        if (ray.direction.z > 0) {
            stepZ = 1;
            tMaxZ = deltaDistZ * (1 - (ray.origin.z - z));
        } else {
            stepZ = -1;
            tMaxZ = deltaDistZ * (ray.origin.z - z);
        }

        // iterate through

        int side = 0;

        for (int i = 0; i < 10; i++) {

            if (tMaxX < tMaxY) {
                if (tMaxX < tMaxZ) {
                    tMaxX += deltaDistX;
                    x += stepX;
                    side = 1;
                } else {
                    tMaxZ += deltaDistZ;
                    z += stepZ;
                    side = 3;
                }

            } else if (tMaxY < tMaxZ) {
                tMaxY += deltaDistY;
                y += stepY;
                side = 2;

            } else {
                tMaxZ += deltaDistZ;
                z += stepZ;
                side = 3;
            }

            float t = 0.0f;

            if (world.getBlock(x, y, z) != 0) {
                if (side == 1) {
                    t = tMaxX - deltaDistX;
                } else if (side == 2) {
                    t = tMaxY - deltaDistY;
                } else {
                    t = tMaxZ - deltaDistZ;
                }
                if (t > maxDistance) {
                    return null;
                }
                return new RayHit(ray, t);
            }

        }

        return null;

    }

    private Vector3f origin;
    private Vector3f direction;

    public Ray(Vector3f origin, Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3f getDistancePosition(float t) {
        return VectorMath.Add(VectorMath.Scale(direction, t), origin);
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3f origin) {
        this.origin = origin;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
}
