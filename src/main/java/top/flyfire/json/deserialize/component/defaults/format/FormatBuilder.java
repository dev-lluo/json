package top.flyfire.json.deserialize.component.defaults.format;

import top.flyfire.json.Token;
import top.flyfire.json.mark.*;

/**
 * Created by devll on 2017/2/17.
 */
public class FormatBuilder implements JsonMarkBuilder<String> {

    StringBuilder builder = new StringBuilder();

    @Override
    public void markOpen(JsonMarkStruct mark) {
        if(mark.isForObject()){
            builder.append(Token.STC_START);
        }else{
            builder.append(Token.INX_START);
        }
    }

    @Override
    public void markClose(JsonMarkStruct mark) {
        if(mark.isForObject()){
            builder.append(Token.STC_END);
        }else{
            builder.append(Token.INX_END);
        }
    }

    @Override
    public void markIndex(JsonMarkIndex mark) {
        if(mark.isForObject()){
            builder.append(mark.getIndex());
            builder.append(Token.STC_K2V);
        }
    }

    @Override
    public void markValue(JsonMarkValue mark) {
        if(mark.isNull()){
            builder.append("null");
        }else if(mark.isUndefined()){
            builder.append("undefined");
        }else{
            builder.append(mark.getValue());
        }
    }

    @Override
    public void markNext(JsonMarkNext mark) {
        if(mark.hasNext()){
            builder.append("\r\n");
            appendTab(mark.getLevel());
            builder.append(",");
        }
    }

    private void appendTab(int count){
        for(int i = 0;i<count;i++){
            builder.append('\t');
        }
    }

    @Override
    public String get() {
        return builder.toString();
    }
}
