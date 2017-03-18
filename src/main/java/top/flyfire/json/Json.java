package top.flyfire.json;

import top.flyfire.common.annotation.NotSupported;
import top.flyfire.common.reflect.ReflectUtils;
import top.flyfire.json.deserialize.DeserializeConfig;
import top.flyfire.json.deserialize.component.defaults.format.FormatBuilder;
import top.flyfire.json.deserialize.component.defaults.highparse.HighParseJavaObjectBuilder;
import top.flyfire.json.deserialize.component.defaults.parse.ParseJavaObjectBuilder;
import top.flyfire.json.deserialize.DeserializeWorker;
import top.flyfire.json.event.JsonWorkListener;
import top.flyfire.json.serialize.SerializeConfig;
import top.flyfire.json.serialize.SerializeWorker;
import top.flyfire.json.serialize.component.defaults.tojson.ParseJsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by devll on 2016/10/19.
 */
public class Json {


    public static String format(String json){
        FormatBuilder formatBuilder = new FormatBuilder();
        new DeserializeWorker(json,formatBuilder).parse();
        return formatBuilder.get();
    }

    public static Object deserialize(String json){
        ParseJavaObjectBuilder builder = new ParseJavaObjectBuilder();
        Json.deserialize(json,builder);
        return builder.get();
    }

    @NotSupported
    public static Object deserialize(String json, DeserializeConfig config){
        return  null;
    }

    public static Object deserialize(String json, Type type){
        HighParseJavaObjectBuilder builder = new HighParseJavaObjectBuilder(ReflectUtils.getMetaInfo(type));
        Json.deserialize(json,builder);
        return  builder.get();
    }

    @NotSupported
    public static Object deserialize(String json,Type type, DeserializeConfig config){
        return  null;
    }

    public static String serialize(Object object){
        ParseJsonBuilder builder = new ParseJsonBuilder();
        Json.serialize(object,builder);
        return builder.get();
    }

    public static String serialize(Object object, SerializeConfig config){
        ParseJsonBuilder builder = new ParseJsonBuilder(config);
        Json.serialize(object,builder);
        return builder.get();
    }

    public static void deserialize(String json, JsonWorkListener markBuilder){
        Json.exec(new DeserializeWorker(json,markBuilder));
    }

    public static void serialize(Object object, JsonWorkListener markBuilder){
        Json.exec(new SerializeWorker(object,markBuilder));
    }

    public static void exec(JsonWorker parser){
        parser.parse();
    }
}
