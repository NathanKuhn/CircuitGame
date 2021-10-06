package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Misc;
import com.github.nathankuhn.circuitgame.utils.Vector3f;
import com.github.nathankuhn.circuitgame.utils.Vector3i;
import com.github.nathankuhn.circuitgame.utils.VectorMath;

public class BoundingBox {

    private Vector3f center;
    private Vector3f dimensions;

    Vector3f min;
    Vector3f max;

    public BoundingBox(Vector3f center, Vector3f dimensions) {
        this.center = center;
        this.dimensions = dimensions;
        updateMax();
        updateMin();
    }

    public void setCenter(Vector3f center) {
        this.center = center;
        updateMax();
        updateMin();
    }

    public void setDimensions(Vector3f dimensions) {
        this.dimensions = dimensions;
        updateMin();
        updateMax();
    }

    private void updateMax() {
        max = VectorMath.Add(center, VectorMath.Scale(dimensions, 0.5f));
    }

    private void updateMin() {
        min = VectorMath.Subtract(center, VectorMath.Scale(dimensions, 0.5f));
    }

    private boolean isColliding(int x, int y, int z) {
        return (min.x <= x + 1 && max.x >= x) &&
                (min.y <= y + 1 && max.y >= y) &&
                (min.z <= z + 1 && max.z >= z);
    }

    public boolean isColliding(Vector3i block) {
        return isColliding(block.x, block.y, block.z);
    }

    public Vector3f worldCollision(World world, Vector3i centerOffset) {

        // find min and max block checks
        Vector3i minCheck = min.floor();
        Vector3i maxCheck = max.ceil();

        // define return value
        Vector3f ret = new Vector3f();

        // check all blocks for collisions
        for (int x = minCheck.x; x <= maxCheck.x; x++) {
            for (int y = minCheck.y; y <= maxCheck.y; y++) {
                for (int z = minCheck.z; z <= maxCheck.z; z++) {
                    Vector3i checkBlock = new Vector3i(x, y, z);//VectorMath.Add(new Vector3i(x, y, z), centerOffset);
                    if (isColliding(x, y, z) && world.getBlock(checkBlock) != 0) {

                        float eastFactor = (x + 1) - (min.x);
                        float westFactor = (max.x) - x;
                        float upFactor = (y + 1) - (min.y);
                        float downFactor = (max.y) - y;
                        float southFactor = (z + 1) - (min.z);
                        float northFactor = (max.z) - z;

                        int minIndex = Misc.minIndex(new float[] {eastFactor, westFactor, upFactor, downFactor, southFactor, northFactor});

                        if (minIndex == 0) {
                            ret.x = eastFactor;
                        } else if (minIndex == 1) {
                            ret.x = -westFactor;
                        } else if (minIndex == 2) {
                            ret.y = upFactor;
                        } else if (minIndex == 3) {
                            ret.y = -downFactor;
                        } else if (minIndex == 4) {
                            ret.z = southFactor;
                        } else{
                            ret.z = -northFactor;
                        }

                    }
                }
            }
        }

        return ret;
    }

    public Vector3f getCenter() {
        return center;
    }
}
