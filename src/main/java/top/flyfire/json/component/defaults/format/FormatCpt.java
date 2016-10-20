package top.flyfire.json.component.defaults.format;

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
        builder.append("\r\n");
        for(int i = 0;i<level;i++){
            builder.append("\t");
        }
        builder.append("]");
    }

    @Override
    public void openObject(int level) {
        builder.append("{").append("\r\n");
    }

    @Override
    public void closeObject(int level) {
        builder.append("\r\n");
        for(int i = 0;i<level;i++){
            builder.append("\t");
        }
        builder.append("}");
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
        builder.append(value);
    }

    @Override
    public void toNext(int level) {
        builder.append(" , ").append("\r\n");
    }

    public String result() {
        return builder.toString();
    }
}
