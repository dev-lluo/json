package top.flyfire.json.component.defaults;

import top.flyfire.json.component.JsonComponent;

/**
 * Created by devll on 2016/10/18.
 */
public class FormatCpt implements JsonComponent<String> {

    StringBuilder builder = new StringBuilder();

    @Override
    public void newStruct(int type, int level) {
        if(type==0){
            builder.append("[").append("\r\n");
        }else{
            builder.append("{").append("\r\n");
        }
    }

    @Override
    public void endStruct(int type, int level) {
        for(int i = 0;i<level;i++){
            builder.append("\t");
        }
        if(type==0){
            builder.append("]").append("\r\n");
        }else{
            builder.append("}").append("\r\n");
        }
    }

    @Override
    public void indexing(Object index,int level) {
        for(int i = 0;i<level;i++){
            builder.append("\t");
        }
        builder.append(index).append(" : ");
    }

    @Override
    public void value(String value, int level) {
        builder.append(value).append("\r\n");
    }

    @Override
    public String storage() {
        return builder.toString();
    }
}
