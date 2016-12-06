package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.reflect.MetaInfo;
import top.flyfire.common.reflect.wrapper.BuildInWrapper;
import top.flyfire.common.reflect.wrapper.BuildOutWrapper;
import top.flyfire.common.reflect.wrapper.InstanceWrapper;
import top.flyfire.common.reflect.wrapper.Wrapper;
import top.flyfire.json.JsonException;
import top.flyfire.json.deserialize.component.StructComponent;

/**
 * Created by devll on 2016/11/1.
 */
public abstract class HighStructValueData<I,W extends InstanceWrapper,O> extends HighValueData<W,O> implements StructComponent<I,MetaInfo> {

    protected I index;

    public HighStructValueData(W wrapper, O value, HighStructValueData owner) {
        super(wrapper,value, owner);
    }

    @Override
    public abstract MetaInfo indexing(I index);

    @Override
    public abstract HighStructValueData push(Object value);

    @Override
    public void destroy() {
        super.destroy();
        this.index = null;
    }

//    public final static HighStructValueData buildStructValueData(InstanceWrapper wrapper, Object value, HighStructValueData owner){
//        if(wrapper instanceof BuildInWrapper){
//            return new BuildInValueData((BuildInWrapper) wrapper,value,owner);
//        }else if(wrapper instanceof BuildOutWrapper){
//            return new BuildOutValueData((BuildOutWrapper) wrapper,value,owner);
//        }else{
//            throw new JsonException("unknow wrapper "+wrapper);
//        }
//    }
}
