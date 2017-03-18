package top.flyfire.json.serialize.component.defaults.tojson;

import top.flyfire.common.StringUtils;
import top.flyfire.json.JsonMark;
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
    public void onOpen(JsonStructEvent mark) {
        builder.append(mark.isForObject()? JsonMark.OBJECT_OPEN: JsonMark.ARRAY_OPEN);
    }

    @Override
    public void onClose(JsonStructEvent mark) {
        builder.append(mark.isForObject()? JsonMark.OBJECT_CLOSE: JsonMark.ARRAY_CLOSE);
    }

    @Override
    public boolean onIndex(JsonIndexEvent mark) {
        if(mark.isForObject()){
            builder.append(mark.getIndex());
            builder.append(JsonMark.OBJECT_P2V);
        }
        return false;
    }

    @Override
    public void onValue(JsonValueEvent mark) {
        if(mark.hasWrapper()){
            builder.append(JsonMark.DOUBLE_QUOTE).append(config.value2String(mark.getValue())).append(JsonMark.DOUBLE_QUOTE);
        }else{
            builder.append(config.value2String(mark.getValue()));
        }

    }

    @Override
    public void onNext(JsonNextEvent mark) {
        if(mark.hasNext()){
            builder.append(JsonMark.NEXT);
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
