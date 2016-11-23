package top.flyfire.json.serialize.component.defaults;

import top.flyfire.common.StringUtils;
import top.flyfire.json.serialize.component.Valued;
import top.flyfire.json.serialize.component.Render;

/**
 * Created by devll on 2016/11/22.
 */
public class JsonValued extends JsonRender implements Valued {

    private final static int NULL = 1,PRIMITIVE = 1<<1;

    protected int feature;


    public JsonValued( Object value, Render parent) {
        super(value, parent);
        if(this.value==null){
            feature&=NULL;
        }else if(this.value.getClass().isPrimitive()){
            feature&=PRIMITIVE;
        }
    }

    @Override
    public String toJson() {
        if((feature&NULL) == NULL){
            return "null";
        }else if((feature&PRIMITIVE) == PRIMITIVE){
            return value.toString();
        }else{
            return StringUtils.merge("\"",value.toString(),"\"");
        }
    }
}
