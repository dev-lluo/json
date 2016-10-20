package top.flyfire.json.deserialize;

import top.flyfire.json.Token;

/**
 * Created by shyy_work on 2016/6/21.
 */
public final class Tokenizer implements Token {

    public static boolean isArrayStart(char token){
        return INX_START == token;
    }

    public static boolean isArrayEnd(char token){
        return INX_END == token;
    }

    public static boolean isObjectStart(char token){
        return STC_START == token;
    }

    public static boolean isObjectEnd(char token){
        return STC_END == token;
    }

    public static boolean isNext(char token){
        return NEXT == token;
    }

    public static boolean isPrp2Val(char token){
        return STC_K2V == token;
    }

    public static boolean isInvisibleChar(char token){
        return SPACE == token || TAB == token || RLINE == token || NLINE == token;
    }

}
