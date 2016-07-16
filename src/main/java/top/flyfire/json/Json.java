package top.flyfire.json;

import top.flyfire.common.reflect.MetaInfo;
import top.flyfire.common.reflect.ReflectUtils;
import top.flyfire.json.deserialize.Deserializer;
import top.flyfire.json.deserialize.data.DataFactory;

/**
 * Created by shyy_work on 2016/6/21.
 */
public final class Json {

    public static Object deserialize(String json){
        Deserializer deserializer = new Deserializer(json, DataFactory.getDefault());
        return deserializer.deserialize();
    }

    public static <T> T deserialize(String json,Class<T> clzz){
        MetaInfo metaInfo = ReflectUtils.unWrap(clzz);
        Deserializer deserializer = new Deserializer(json,DataFactory.getMetaInfoFactory());
        return (T)deserializer.deserialize();
    }

}
