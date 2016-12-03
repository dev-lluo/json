package top.flyfire.json.serialize.component.defaults.tojson;

import top.flyfire.json.JsonComponent;

/**
 * Created by devll on 2016/11/25.
 */
public class JsonBuilder implements JsonComponent<String> {

    StringBuilder jsonBuffer = new StringBuilder();

    @Override
    public void openArray(int level) {
        jsonBuffer.append("[");
    }

    @Override
    public void closeArray(int level) {
        jsonBuffer.append("]");
    }

    @Override
    public void openObject(int level) {
        jsonBuffer.append("{");
    }

    @Override
    public void closeObject(int level) {
        jsonBuffer.append("}");
    }

    @Override
    public void indexing(Object index, int level) {
        jsonBuffer.append(index);
        jsonBuffer.append(":");
    }

    @Override
    public void value(Object value, int level) {
        jsonBuffer.append(value);
    }

    @Override
    public void toNext(int level) {
        jsonBuffer.append(",");
    }

    @Override
    public String result() {
        try {
            return jsonBuffer.toString();
        }finally {
            jsonBuffer = null;
        }
    }
}
