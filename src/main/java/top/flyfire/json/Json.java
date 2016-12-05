package top.flyfire.json;

import top.flyfire.common.reflect.ReflectUtils;
import top.flyfire.common.reflect.wrapper.WrapperFactory;
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
        FormatCpt formatCpt = new FormatCpt();
        new Deserializer(json,formatCpt).deserialize();
        return formatCpt.result();
    }

    public static Object deserialize(String json){
        ParseJavaObjectCpt parseJavaObjectCpt = new ParseJavaObjectCpt();
        Json.exec(json,parseJavaObjectCpt);
        return parseJavaObjectCpt.result();
    }

    public static Object deserialize(String json, Type type){
        HighParseJavaObjectCpt highParseJavaObjectCpt = new HighParseJavaObjectCpt(ReflectUtils.getMetaInfo(type));
        Json.exec(json,highParseJavaObjectCpt);
        return  highParseJavaObjectCpt.result();
    }

    public static String serialize(Object object){
        JsonBuilder component = new JsonBuilder();
        Serializer serializer = new Serializer(object,component);
        serializer.parse();
        return component.result();
    }

    public static void exec(String json, JsonComponent component){
        new Deserializer(json,component).deserialize();
    }

}
