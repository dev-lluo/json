package top.flyfire.json.component.defaults;

import top.flyfire.json.component.JsonComponent;

/**
 * Created by devll on 2016/10/18.
 */
public class FormatCpt implements JsonComponent {

    StringBuilder builder = new StringBuilder();

    @Override
    public void openArray(int level) {
        builder.append("[").append("\r\n");
    }

    @Override
    public void closeArray(int level) {
        for(int i = 0;i<level;i++){
            builder.append("\t");
        }
        builder.append("]").append("\r\n");
    }

    @Override
    public void openObject(int level) {
        builder.append("{").append("\r\n");
    }

    @Override
    public void closeObject(int level) {
        for(int i = 0;i<level;i++){
            builder.append("\t");
        }
        builder.append("}").append("\r\n");
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

    public String result() {
        return builder.toString();
    }
}
