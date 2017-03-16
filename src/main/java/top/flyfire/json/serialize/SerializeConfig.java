package top.flyfire.json.serialize;


import top.flyfire.common.DateUtils;
import top.flyfire.common.StringUtils;
import top.flyfire.common.chainedmode.Handler;
import top.flyfire.common.chainedmode.HandlerChain;
import top.flyfire.common.reflect.ReflectUtils;
import top.flyfire.json.Token;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by devll on 2017/2/14.
 */
public class SerializeConfig {

    public static final SerializeConfig DEFAULT = new SerializeConfig();

    private boolean ignoreCircularReference;

    private int readDeep;

    private HandlerChain<String,Object> value2StringHandlerChain;


    public SerializeConfig() {
        this(true,3);
    }

    public SerializeConfig(boolean ignoreCircularReference) {
        this(ignoreCircularReference,0);
    }

    public SerializeConfig(int readDeep) {
        this(true,readDeep);
    }

    public SerializeConfig(boolean ignoreCircularReference, int readDeep) {
        this(ignoreCircularReference,readDeep,HandlerChain.buildChain(
                new Handler<String, Object>() {
                    @Override
                    public String handling(Object o, HandlerChain<String, Object> handlerChain) {
                        if (o == null) {
                            return "null";
                        } else {
                            return handlerChain.handling(o);
                        }
                    }
                }, new Handler<String, Object>() {
                    @Override
                    public String handling(Object o, HandlerChain<String, Object> handlerChain) {
                        if (o instanceof Date) {
                            return DateUtils.format((Date) o);
                        } else {
                            return handlerChain.handling(o);
                        }
                    }
                }, new Handler<String, Object>() {
                    @Override
                    public String handling(Object o, HandlerChain<String, Object> handlerChain) {
                        return o.toString();
                    }
                }
        ));
    }

    public SerializeConfig(boolean ignoreCircularReference, int readDeep, HandlerChain<String,Object> value2StringHandlerChain) {
        this.ignoreCircularReference = ignoreCircularReference;
        this.readDeep = readDeep;
        this.value2StringHandlerChain = value2StringHandlerChain;
    }

    public boolean isIgnoreCircularReference() {
        return ignoreCircularReference;
    }

    public int getReadDeep() {
        return readDeep;
    }

    public String value2String(Object object){
        return value2StringHandlerChain.handling(object);
    }
}
