package top.flyfire.json;

import top.flyfire.common.ArrayUtils;
import top.flyfire.common.StringUtils;

/**
 * Created by devll on 2017/2/22.
 */
public class JsonRoute {

    private Object[] caches;

    private Integer[] tokenCaches;

    private int level;

    private int size = 16;

    public JsonRoute(){
        level = 0;
        caches = new Object[size];
        tokenCaches = new Integer[size];
        push("$",0);
    }

    public int pushArrayIndex(int index){
        return push(index,1);
    }

    public int pushObjectKey(String key){
        return push(key,0);
    }

    private int push(Object nodeName,int token){
        if(level ==caches.length) {
            resize();
        }
        caches[level] = nodeName;
        tokenCaches[level] = token;
        return level++;
    }

    public int pop(){
        return --level;
    }

    public <T> T get(){
        return (T)caches[level-1];
    }

    public int getLevel() {
        return level;
    }

    private void resize(){
        Object[] oldCaches = caches;
        size <<= 1;
        this.caches = new Object[size];
        ArrayUtils.arrayCopy(oldCaches,0,this.caches,0,oldCaches.length);
        Integer[] oldTokenCaches = tokenCaches;
        this.tokenCaches = new Integer[size];
        ArrayUtils.arrayCopy(oldTokenCaches,0,this.tokenCaches,0,oldTokenCaches.length);
    }

}
