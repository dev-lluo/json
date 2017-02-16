package top.flyfire.json.serialize.component.defaults;

/**
 * Created by devll on 2016/11/23.
 */
public abstract class JsonKyStruct<T> extends JsonStruct<T,String> {
    public JsonKyStruct(T cached, JsonValue o) {
        super(cached, o);
    }
}
