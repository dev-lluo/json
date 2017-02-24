package top.flyfire.json.serialize.component.defaults.tojson;

import top.flyfire.json.Token;
import top.flyfire.json.mark.*;
import top.flyfire.json.serialize.SerializeConfig;

/**
 * Created by devll on 2017/2/24.
 */
public class ParseJsonBuilder implements JsonMarkBuilder<String> {

    StringBuilder builder = new StringBuilder();

    SerializeConfig config;

    public ParseJsonBuilder() {
        this(SerializeConfig.DEFAULT);
    }

    public ParseJsonBuilder(SerializeConfig config) {
        this.config = config;
    }

    @Override
    public void markOpen(JsonMarkStruct mark) {
        builder.append(mark.isForObject()? Token.OBJECT_OPEN:Token.ARRAY_OPEN);
    }

    @Override
    public void markClose(JsonMarkStruct mark) {
        builder.append(mark.isForObject()?Token.OBJECT_CLOSE:Token.ARRAY_CLOSE);
    }

    @Override
    public boolean markIndex(JsonMarkIndex mark) {
        System.out.println(mark.getPath());
        if(mark.isForObject()){
            builder.append(mark.getIndex());
            builder.append(Token.OBJECT_P2V);
        }
        return false;
    }

    @Override
    public void markValue(JsonMarkValue mark) {
        builder.append(config.value2String(mark.getValue()));
    }

    @Override
    public void markNext(JsonMarkNext mark) {
        if(mark.hasNext()){
            builder.append(Token.NEXT);
        }
    }

    @Override
    public String get() {
        return builder.toString();
    }
}
