package top.flyfire.json;

import top.flyfire.common.reflect.ReflectUtils;
import top.flyfire.json.deserialize.component.defaults.format.FormatBuilder;
import top.flyfire.json.deserialize.component.defaults.format.FormatCpt;
import top.flyfire.json.deserialize.component.defaults.highparse.HighParseJavaObjectCpt;
import top.flyfire.json.deserialize.component.defaults.parse.ParseJavaObjectCpt;
import top.flyfire.json.deserialize.Deserializer;
import top.flyfire.json.serialize.Serializer;
import top.flyfire.json.serialize.component.defaults.tojson.JsonBuilder;

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
        ParseJavaObjectCpt parseJavaObjectCpt = new ParseJavaObjectCpt();
        Json.deserialize(json,parseJavaObjectCpt);
        return parseJavaObjectCpt.result();
    }

    public static Object deserialize(String json, Type type){
        HighParseJavaObjectCpt highParseJavaObjectCpt = new HighParseJavaObjectCpt(ReflectUtils.getMetaInfo(type));
        Json.deserialize(json,highParseJavaObjectCpt);
        return  highParseJavaObjectCpt.result();
    }

    public static String serialize(Object object){
        JsonBuilder component = new JsonBuilder();
        Json.serialize(object,component);
        return component.result();
    }

    public static void deserialize(String json, JsonComponent component){
        Json.exec(new Deserializer(json,component));
    }

    public static void serialize(Object object, JsonComponent component){
        Json.exec(new Serializer(object,component));
    }

    public static void exec(Parser parser){
        parser.parse();
    }
}
