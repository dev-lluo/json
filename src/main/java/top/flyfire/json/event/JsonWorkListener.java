package top.flyfire.json.event;

/**
 * Created by devll on 2017/2/17.
 */
public interface JsonWorkListener<Result> {

    void markOpen(JsonStructEvent mark);

    void markClose(JsonStructEvent mark);

    boolean markIndex(JsonIndexEvent mark);

    void markValue(JsonValueEvent mark);

    void markNext(JsonNextEvent mark);

    Result get();

}
