package com.github.nathankuhn.circuitgame.hud;

import com.github.nathankuhn.circuitgame.rendering.RenderObject;
import com.github.nathankuhn.circuitgame.utils.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Root extends HudElement{

    public Root() {
        super(null, new Vector2f(0.0f, 0.0f), new Vector2f(1.0f, 1.0f), true);
    }

    public List<HudElement> getAllChildren() {

        Stack<HudElement> stack = new Stack<>();
        stack.addAll(getChildren());

        List<HudElement> ret = new ArrayList<>();

        while(stack.size() > 0) {

            ret.add(stack.lastElement());
            if (stack.lastElement().getChildren() != null) {
                stack.addAll(stack.pop().getChildren());
            } else {
                stack.pop();
            }
        }

        return ret;

    }

    public List<RenderObject> getRenderObjects() {

        Stack<HudElement> stack = new Stack<>();
        stack.addAll(getChildren());

        List<RenderObject> ret = new ArrayList<>();

        while(stack.size() > 0) {

            ret.add(stack.lastElement().getRenderObject());

            if (stack.lastElement().getChildren() != null) {
                stack.addAll(stack.pop().getChildren());
            } else {
                stack.pop();
            }
        }

        return ret;
    }

}
