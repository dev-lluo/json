package top.flyfire.json.component.defaults.parse;

import top.flyfire.json.component.JsonComponent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by devll on 2016/10/19.
 */
public class ParseJavaObjectCpt implements JsonComponent {

    private ValueData data;

    @Override
    public void openArray(int level) {
        data = new ArrayData(new ArrayList(), (StructValueData) data);
    }

    @Override
    public void closeArray(int level) {
        dataRender();
    }

    @Override
    public void openObject(int level) {
        data = new ObjectData(new HashMap(), (StructValueData) data);
    }

    @Override
    public void closeObject(int level) {
        dataRender();
    }

    @Override
    public void indexing(Object index, int level) {
        ((StructValueData)data).indexing(index);
    }

    @Override
    public void value(String value, int level) {
        data = new ValueData(value, (StructValueData) data);
        dataRender();
    }

    @Override
    public void toNext(int level) {

    }

    private void dataRender(){
        ValueData temp = data.render();
        if(temp!=null){
            data = temp;
        }
    }

    public Object result(){
        return data.getValue();
    }
}
