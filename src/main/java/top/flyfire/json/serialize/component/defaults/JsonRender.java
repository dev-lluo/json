package top.flyfire.json.serialize.component.defaults;

import top.flyfire.common.Destroy;
import top.flyfire.json.serialize.component.Render;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by devll on 2016/11/22.
 */
public class JsonRender<T> implements Render,Destroy {

    protected Render parent;

    protected T value;

    public JsonRender(T value, Render parent) {
        this.value = value;
        this.parent = parent;
    }

    @Override
    public Render render() {
        try {
            return parent;
        }finally {
            destroy();
        }
    }

    @Override
    public void destroy() {
        parent = null;
        value = null;
    }

    public static Render buildRender(Object value, Render parent){
        if(value == null){
            return new JsonValued(value,parent);
        }else if(value instanceof String){
            return new JsonValued(value,parent);
        }else if(value.getClass().isPrimitive()){
            return new JsonValued(value,parent);
        }else if(value.getClass().isArray()){
            return new JsonArray(value,parent);
        }else if(value instanceof Date){
            return new JsonValued(value,parent);
        }else if(value instanceof Map){
            return new JsonMap((Map) value,parent);
        }else if(value instanceof List){
            return new JsonList((List) value,parent);
        }else if(value instanceof Set){
          return new JsonSet((Set) value,parent);
        }else{
            return new JsonObject(value,parent);
        }
    }

}
