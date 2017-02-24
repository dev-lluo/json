package top.flyfire.json;

import top.flyfire.common.ArrayUtils;
import top.flyfire.common.StringUtils;

/**
 * Created by devll on 2017/2/22.
 */
public class JsonRoute {

    private String[] caches;

    private int cursor;

    public JsonRoute(){
        caches = new String[16];
        push("$");
    }

    public void push(String nodeName){
        if(cursor==caches.length) {
            resize();
        }
        caches[cursor++] = nodeName;
    }

    public void pop(){
        cursor--;
    }

    public String get(){
        return StringUtils.megre(caches,cursor);
    }

    private void resize(){
        String[] oldCaches = caches;
        this.caches = new String[oldCaches.length<<1];
        ArrayUtils.arrayCopy(oldCaches,0,this.caches,0,oldCaches.length);
    }

}
