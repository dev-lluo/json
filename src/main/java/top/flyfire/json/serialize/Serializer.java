package top.flyfire.json.serialize;


import top.flyfire.json.*;
import top.flyfire.json.mark.*;
import top.flyfire.json.serialize.component.StructNode;
import top.flyfire.json.serialize.component.defaults.JsonArrayStruct;
import top.flyfire.json.serialize.component.defaults.JsonObjectStruct;
import top.flyfire.json.serialize.component.defaults.JsonValue;

/**
 * Created by devll on 2016/11/9.
 */
public class Serializer implements Peeker ,Parser {

    private JsonValue value;

    private JsonMarkBuilder markBuilder;

    private JsonRoute route;

    private boolean breakOff;

    private Parser JSONVALUEDPARSER, JSONKYSTRUCTPARSER, JSONIDXSTRUCTPARSER;

    public Serializer(Object object, JsonMarkBuilder markBuilder){
        this.markBuilder = markBuilder;
        this.route = new JsonRoute();
        value = JsonValue.buildValue(object,null);
        JSONVALUEDPARSER = new JsonValuedParser();
        JSONKYSTRUCTPARSER = new JsonKyStructParser();
        JSONIDXSTRUCTPARSER = new JsonIdxStructParser();
    }

    private JsonValue getJsonValued(){
        return (JsonValue) value;
    }

    private StructNode getStructSwap(){
        return (StructNode) value;
    }

    @Override
    public void parse() {
        peek().parse();
    }

    @Override
    public Parser peek() {
        if(value instanceof JsonObjectStruct){
            return JSONKYSTRUCTPARSER;
        }else if(value instanceof JsonArrayStruct){
            return JSONIDXSTRUCTPARSER;
        }else if(value instanceof JsonValue){
            return JSONVALUEDPARSER;
        }else {
            throw new RuntimeException();
        }
    }


    private void flush(){
        JsonValue temp = value;
        value = temp.getParent();
        temp.destroy();
    }

    public class JsonValuedParser implements Parser{

        @Override
        public void parse() {
            markBuilder.markValue(new JsonMarkValue(route.getLevel(),route.get(),getJsonValued().getCached(),false,false,false));
        }
    }

    public class JsonKyStructParser implements Parser ,Enumeration {

        @Override
        public void parse() {
            if(validateAndStart()){
                do{
                    StructNode.Entry entry = getStructSwap().peeking();
                    route.pushObjectKey((String) entry.indexing());
                    markBuilder.markIndex(new JsonMarkIndex(route.getLevel(),route.get(),entry.indexing(),true));
                    value = entry.value();
                    Serializer.this.parse();
                }while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public boolean validateAndStart() {
            markBuilder.markOpen(new JsonMarkStruct(route.getLevel(),route.get(),true));
            return getStructSwap().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            flush();
            boolean b = getStructSwap().hasNext();
            markBuilder.markNext(new JsonMarkNext(route.getLevel(),route.get(),b));
            return b;
        }

        @Override
        public void validateAndEnd() {
            markBuilder.markClose(new JsonMarkStruct(route.getLevel(),route.get(),true));
        }
    }


    public class JsonIdxStructParser implements Parser ,Enumeration {

        @Override
        public void parse() {
            if(validateAndStart()){
                do{
                    StructNode.Entry entry = getStructSwap().peeking();
                    route.pushArrayIndex((int) entry.indexing());
                    markBuilder.markIndex(new JsonMarkIndex(route.getLevel(),route.get(),entry.indexing(),false));
                    value = entry.value();
                    Serializer.this.parse();
                }while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public boolean validateAndStart() {
            markBuilder.markOpen(new JsonMarkStruct(route.getLevel(),route.get(),false));
            return getStructSwap().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            flush();
            boolean b = getStructSwap().hasNext();
            markBuilder.markNext(new JsonMarkNext(route.getLevel(),route.get(),b));
            return b;
        }

        @Override
        public void validateAndEnd() {
            markBuilder.markClose(new JsonMarkStruct(route.getLevel(),route.get(),false));
        }
    }


}
