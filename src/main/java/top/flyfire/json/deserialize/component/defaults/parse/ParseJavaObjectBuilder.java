package top.flyfire.json.deserialize.component.defaults.parse;

import top.flyfire.json.mark.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shyy_work on 2017/2/24.
 */
public class ParseJavaObjectBuilder implements JsonMarkBuilder<Object> {


    private ValueData data;

    @Override
    public void markOpen(JsonMarkStruct mark) {
        data = mark.isForObject()?new ObjectData(new HashMap(), (StructValueData) data): new ArrayData(new ArrayList(), (StructValueData) data);
    }

    @Override
    public void markClose(JsonMarkStruct mark) {
        dataRender();
    }

    @Override
    public boolean markIndex(JsonMarkIndex mark) {
        ((StructValueData)data).indexing(mark.getIndex());
        return false;
    }

    @Override
    public void markValue(JsonMarkValue mark) {
        data = new ValueData(mark.getValue(), (StructValueData) data);
        dataRender();
    }

    @Override
    public void markNext(JsonMarkNext mark) {

    }

    @Override
    public Object get() {
        try {
            return data.getValue();
        }finally {
            data = null;
        }
    }

    private void dataRender(){
        ValueData temp = data.getParent();
        if(temp!=null){
            data = temp;
        }
    }
}
