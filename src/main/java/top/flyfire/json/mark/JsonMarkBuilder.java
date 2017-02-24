package top.flyfire.json.mark;

/**
 * Created by devll on 2017/2/17.
 */
public interface JsonMarkBuilder<Result> {

    void markOpen(JsonMarkStruct mark);

    void markClose(JsonMarkStruct mark);

    boolean markIndex(JsonMarkIndex mark);

    void markValue(JsonMarkValue mark);

    void markNext(JsonMarkNext mark);

    Result get();

}
