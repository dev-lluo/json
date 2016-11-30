package top.flyfire.json.deserialize.exception;

import top.flyfire.json.JsonException;

/**
 * Created by devll on 2016/11/30.
 */
public class UnexpectedTokenException extends JsonException {

    public UnexpectedTokenException(String message,int cursor) {
        super(message.substring(0,cursor));
    }
}
