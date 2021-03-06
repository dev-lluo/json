package top.flyfire.json.deserialize;

import top.flyfire.json.JsonMark;

/**
 * Created by shyy_work on 2016/6/21.
 */
public final class JsonMarker implements JsonMark {

    public static boolean isArrayStart(char token){
        return ARRAY_OPEN == token;
    }

    public static boolean isArrayEnd(char token){
        return ARRAY_CLOSE == token;
    }

    public static boolean isObjectStart(char token){
        return OBJECT_OPEN == token;
    }

    public static boolean isObjectEnd(char token){
        return OBJECT_CLOSE == token;
    }

    public static boolean isNext(char token){
        return NEXT == token;
    }

    public static boolean isPrp2Val(char token){
        return OBJECT_P2V == token;
    }

    public static boolean isInvisibleChar(char token){
        return SPACE == token || TAB == token || RLINE == token || NLINE == token;
    }

}
