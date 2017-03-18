package top.flyfire.json.deserialize.component.defaults.format;

import top.flyfire.common.StringUtils;
import top.flyfire.json.Token;
import top.flyfire.json.event.*;

/**
 * Created by devll on 2017/2/17.
 */
public class FormatBuilder implements JsonWorkListener<String> {

    StringBuilder builder = new StringBuilder(128);

    String result;

    @Override
    public void markOpen(JsonStructEvent mark) {
        if(mark.isForObject()){
            builder.append(Token.OBJECT_OPEN);
        }else{
            builder.append(Token.ARRAY_OPEN);
        }
    }

    @Override
    public void markClose(JsonStructEvent mark) {
        newLine(mark.getLevel());
        if(mark.isForObject()){
            builder.append(Token.OBJECT_CLOSE);
        }else{
            builder.append(Token.ARRAY_CLOSE);
        }
    }

    @Override
    public boolean markIndex(JsonIndexEvent mark) {
        newLine(mark.getLevel());
        if(mark.isForObject()){
            builder.append(mark.getIndex());
            builder.append(Token.OBJECT_P2V);
        }
        return false;
    }

    @Override
    public void markValue(JsonValueEvent mark) {
        if(mark.isNull()){
            builder.append("null");
        }else if(mark.isUndefined()){
            builder.append("undefined");
        }else{
            builder.append(mark.getValue());
        }
    }

    @Override
    public void markNext(JsonNextEvent mark) {
        if(mark.hasNext()){
            builder.append(",");
        }
    }

    private void newLine(int count){
        builder.append("\r\n");
        for(int i = 0;i<count;i++){
            builder.append('\t');
        }
    }

    @Override
    public String get() {
        if(StringUtils.isNull(result)){
            result = builder.toString();
            builder= null;
        }
        return result;
    }
}
