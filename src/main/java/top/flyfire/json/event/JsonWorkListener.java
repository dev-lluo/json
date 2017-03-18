package top.flyfire.json.event;

/**
 * Created by devll on 2017/2/17.
 */
public interface JsonWorkListener<Result> {

    void onOpen(JsonStructEvent mark);

    void onClose(JsonStructEvent mark);

    boolean onIndex(JsonIndexEvent mark);

    void onValue(JsonValueEvent mark);

    void onNext(JsonNextEvent mark);

    Result get();

}
