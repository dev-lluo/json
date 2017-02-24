package top.flyfire.json;

import top.flyfire.common.reflect.ReflectUtils;
import top.flyfire.json.deserialize.component.defaults.format.FormatBuilder;
import top.flyfire.json.deserialize.component.defaults.highparse.HighParseJavaObjectBuilder;
import top.flyfire.json.deserialize.component.defaults.parse.ParseJavaObjectBuilder;
import top.flyfire.json.deserialize.Deserializer;
import top.flyfire.json.mark.JsonMarkBuilder;
import top.flyfire.json.serialize.Serializer;
import top.flyfire.json.serialize.component.defaults.tojson.ParseJsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by devll on 2016/10/19.
 */
public class Json {


    public static String format(String json){
        FormatBuilder formatBuilder = new FormatBuilder();
        new Deserializer(json,formatBuilder).parse();
        return formatBuilder.get();
    }

    public static Object deserialize(String json){
        ParseJavaObjectBuilder builder = new ParseJavaObjectBuilder();
        Json.deserialize(json,builder);
        return builder.get();
    }

    public static Object deserialize(String json, Type type){
        HighParseJavaObjectBuilder builder = new HighParseJavaObjectBuilder(ReflectUtils.getMetaInfo(type));
        Json.deserialize(json,builder);
        return  builder.get();
    }

    public static String serialize(Object object){
        ParseJsonBuilder builder = new ParseJsonBuilder();
        Json.serialize(object,builder);
        return builder.get();
    }

    public static void deserialize(String json, JsonMarkBuilder markBuilder){
        Json.exec(new Deserializer(json,markBuilder));
    }

    public static void serialize(Object object, JsonMarkBuilder markBuilder){
        Json.exec(new Serializer(object,markBuilder));
    }

    public static void exec(Parser parser){
        parser.parse();
    }
}
