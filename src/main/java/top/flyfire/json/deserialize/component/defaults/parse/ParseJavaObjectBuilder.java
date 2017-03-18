package top.flyfire.json.deserialize.component.defaults.parse;

import top.flyfire.common.ObjectUtils;
import top.flyfire.json.event.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shyy_work on 2017/2/24.
 */
public class ParseJavaObjectBuilder implements JsonWorkListener<Object> {


    private ValueData data;

    private Object result;

    @Override
    public void markOpen(JsonStructEvent mark) {
        data = mark.isForObject()?new ObjectData(new HashMap(), (StructValueData) data): new ArrayData(new ArrayList(), (StructValueData) data);
    }

    @Override
    public void markClose(JsonStructEvent mark) {
        dataRender();
    }

    @Override
    public boolean markIndex(JsonIndexEvent mark) {
        ((StructValueData)data).indexing(mark.getIndex());
        return false;
    }

    @Override
    public void markValue(JsonValueEvent mark) {
        data = new ValueData(mark.getValue(), (StructValueData) data);
        dataRender();
    }

    @Override
    public void markNext(JsonNextEvent mark) {

    }

    @Override
    public Object get() {
        if(ObjectUtils.isNull(result)){
            result = data.getValue();
            data= null;
        }
        return result;
    }

    private void dataRender(){
        ValueData temp = data.getParent();
        if(temp!=null){
            data = temp;
        }
    }
}
