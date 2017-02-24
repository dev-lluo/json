package top.flyfire.json;

import top.flyfire.common.ArrayUtils;
import top.flyfire.common.StringUtils;

/**
 * Created by devll on 2017/2/22.
 */
public class JsonRoute {

    private String[] caches;

    private int level;

    public JsonRoute(){
        level = 0;
        caches = new String[16];
        push("$");
    }

    public int pushArrayIndex(int index){
        return push(StringUtils.merge("[",index,"]"));
    }

    public int pushObjectKey(String key){
        return push(StringUtils.merge(".",key));
    }

    private int push(String nodeName){
        if(level ==caches.length) {
            resize();
        }
        caches[level] = nodeName;
        return level++;
    }

    public int pop(){
        return --level;
    }

    public String get(){
        return StringUtils.megre(caches, level);
    }

    public int getLevel() {
        return level;
    }

    private void resize(){
        String[] oldCaches = caches;
        this.caches = new String[oldCaches.length<<1];
        ArrayUtils.arrayCopy(oldCaches,0,this.caches,0,oldCaches.length);
    }

}
