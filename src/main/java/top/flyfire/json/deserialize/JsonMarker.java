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

    public static boolean isQuote(char token){
        return DOUBLE_QUOTE==token||SINGLE_QUOTE==token;
    }

    public static char escape(char token){
        if (token == 't') {
            return '\t';
        } else if (token == 'r') {
            return '\r';
        } else if (token == 'n') {
            return '\n';
        } else if (token == 'f') {
            return '\f';
        } else if (token == 'b') {
            return '\b';
        } else if (token == '/') {
            return '/';
        } else if (token == '\\') {
            return '\\';
        } else if (token == '"') {
            return '"';
        } else if (token == '\'') {
            return '\'';
        } else {
            return token;
        }
    }

}
