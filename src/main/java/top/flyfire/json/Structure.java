package top.flyfire.json;

/**
 * Created by shyy_work on 2016/6/21.
 */
public interface Structure {

    boolean validateAndStart();

    boolean hasNext();

    void validateAndEnd();

}
