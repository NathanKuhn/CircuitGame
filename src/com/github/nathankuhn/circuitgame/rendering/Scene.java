package com.github.nathankuhn.circuitgame.rendering;

import com.github.nathankuhn.circuitgame.utils.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private Camera mainCamera;
    private List<RenderObject> renderObjects; // legacy render objects
    private World world;
    // TODO lights

    public Scene(Camera camera, World world) {
        mainCamera = camera;
        this.world = world;
        renderObjects = new ArrayList<>();
    }

    public List<RenderObject> getRenderObjects() {
        List<RenderObject> ret = new ArrayList<>();
        List<RenderObject> a = world.getRenderList();
        ret.addAll(renderObjects);
        ret.addAll(a);
        return ret;
    }
    public Camera getMainCamera() {
        return mainCamera;
    }

    public void addRenderObject(RenderObject renderObject) {
        renderObjects.add(renderObject);
    }
}
