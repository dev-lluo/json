package top.flyfire.json.serialize.component.defaults.tojson;

import top.flyfire.common.StringUtils;
import top.flyfire.json.Token;
import top.flyfire.json.event.*;
import top.flyfire.json.serialize.SerializeConfig;

/**
 * Created by devll on 2017/2/24.
 */
public class ParseJsonBuilder implements JsonWorkListener<String> {

    StringBuilder builder = new StringBuilder(128);

    String result;

    SerializeConfig config;

    public ParseJsonBuilder() {
        this(SerializeConfig.DEFAULT);
    }

    public ParseJsonBuilder(SerializeConfig config) {
        this.config = config;
    }

    @Override
    public void markOpen(JsonStructEvent mark) {
        builder.append(mark.isForObject()? Token.OBJECT_OPEN:Token.ARRAY_OPEN);
    }

    @Override
    public void markClose(JsonStructEvent mark) {
        builder.append(mark.isForObject()?Token.OBJECT_CLOSE:Token.ARRAY_CLOSE);
    }

    @Override
    public boolean markIndex(JsonIndexEvent mark) {
        if(mark.isForObject()){
            builder.append(mark.getIndex());
            builder.append(Token.OBJECT_P2V);
        }
        return false;
    }

    @Override
    public void markValue(JsonValueEvent mark) {
        if(mark.hasWrapper()){
            builder.append(Token.DOUBLE_QUOTE).append(config.value2String(mark.getValue())).append(Token.DOUBLE_QUOTE);
        }else{
            builder.append(config.value2String(mark.getValue()));
        }

    }

    @Override
    public void markNext(JsonNextEvent mark) {
        if(mark.hasNext()){
            builder.append(Token.NEXT);
        }
    }

    @Override
    public String get() {
        if(StringUtils.isNull(result)){
            result = builder.toString();
            builder= null;
            config = null;
        }
        return result;
    }
}
