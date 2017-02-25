package top.flyfire.json.deserialize.component.defaults.highparse;

import top.flyfire.common.ObjectUtils;
import top.flyfire.common.reflect.MetaInfo;
import top.flyfire.common.reflect.wrapper.*;
import top.flyfire.json.JsonException;
import top.flyfire.json.mark.*;

import java.util.Collection;
import java.util.Map;

/**
 * Created by shyy_work on 2017/2/24.
 */
public class HighParseJavaObjectBuilder implements JsonMarkBuilder<Object> {


    private MetaInfo metaInfo;

    private HighValueData data;

    private Object result;


    public HighParseJavaObjectBuilder(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    @Override
    public void markOpen(JsonMarkStruct mark) {
        InstanceWrapper instanceWrapper = (InstanceWrapper) (metaInfo.getWrapper());
        data = buildStructValueData(instanceWrapper, instanceWrapper.instance(), (HighStructValueData) data);
    }

    @Override
    public void markClose(JsonMarkStruct mark) {
        dataRender();
    }

    @Override
    public boolean markIndex(JsonMarkIndex mark) {
        return  null == (metaInfo = ((HighStructValueData) data).indexing(mark.getIndex()));
    }

    @Override
    public void markValue(JsonMarkValue mark) {
        data = new HighRawValueData((ValueWrapper) (metaInfo.getWrapper()), mark.getValue(), (HighStructValueData) data);
        dataRender();
    }

    @Override
    public void markNext(JsonMarkNext mark) {

    }

    @Override
    public Object get() {
        if(ObjectUtils.isNull(result)){
            result = data.getValue();
            data= null;
            metaInfo = null;
        }
        return result;
    }

    private void dataRender(){
        HighStructValueData temp = data.getParent();
        if(temp!=null){
            data = temp;
        }
    }

    private final static HighStructValueData buildStructValueData(InstanceWrapper wrapper, Object value, HighStructValueData owner){
        if(wrapper instanceof BuildInWrapper){
            return new BuildInValueData((BuildInWrapper) wrapper,value,owner);
        }if(wrapper instanceof CollectionWrapper){
            return new CollectionValueData((CollectionWrapper) wrapper, (Collection) value,owner);
        }else if(wrapper instanceof MapWrapper){
            return new MapValueData((MapWrapper) wrapper,(Map) value,owner);
        }else if(wrapper instanceof ArrayWrapper){
            return new ArrayValueData((ArrayWrapper) wrapper, (Collection) value,owner);
        }else{
            throw new JsonException("unknow wrapper ["+wrapper+"]");
        }
    }
}
