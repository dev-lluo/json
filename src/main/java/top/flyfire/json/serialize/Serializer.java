package top.flyfire.json.serialize;


import top.flyfire.json.*;
import top.flyfire.json.serialize.component.StructSwap;
import top.flyfire.json.serialize.component.defaults.JsonIdxStruct;
import top.flyfire.json.serialize.component.defaults.JsonKyStruct;
import top.flyfire.json.serialize.component.defaults.JsonValue;

/**
 * Created by devll on 2016/11/9.
 */
public class Serializer implements Peeker ,Parser {

    private JsonValue value;

    private JsonComponent component;

    private SerializeConfig config;

    private int level;

    private Parser JSONVALUEDPARSER, JSONKYSTRUCTPARSER, JSONIDXSTRUCTPARSER;

    public Serializer(Object object, JsonComponent component) {
        this(object,component,SerializeConfig.DEFAULT);
    }

    public Serializer(Object object, JsonComponent component,SerializeConfig config){
        this.level = 0;
        this.component = component;
        value = JsonValue.buildValue(object,null);
        this.config = config;
        JSONVALUEDPARSER = new JsonValuedParser();
        JSONKYSTRUCTPARSER = new JsonKyStructParser();
        JSONIDXSTRUCTPARSER = new JsonIdxStructParser();
    }

    private JsonValue getJsonValued(){
        return (JsonValue) value;
    }

    private StructSwap getStructSwap(){
        return (StructSwap) value;
    }

    @Override
    public void parse() {
        peek().parse();
    }

    @Override
    public Parser peek() {
        if(value instanceof JsonKyStruct){
            return JSONKYSTRUCTPARSER;
        }else if(value instanceof JsonIdxStruct){
            return JSONIDXSTRUCTPARSER;
        }else if(value instanceof JsonValue){
            return JSONVALUEDPARSER;
        }else {
            throw new RuntimeException();
        }
    }

    public class JsonValuedParser implements Parser{

        @Override
        public void parse() {
            component.value(config.value2String(getJsonValued().getCached()),level);
            value = getJsonValued().getParent();
        }
    }

    public class JsonKyStructParser implements Parser ,Enumeration {

        @Override
        public void parse() {
            if(validateAndStart()){
                do{
                    StructSwap.Entry entry = getStructSwap().peeking();
                    component.indexing(entry.indexing(),level);
                    value = entry.value();
                   peek().parse();
                }while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public boolean validateAndStart() {
            component.openObject(level++);
            return getStructSwap().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            boolean b = getStructSwap().hasNext();
            if(b){
                component.toNext(level);
            }
            return b;
        }

        @Override
        public void validateAndEnd() {
            component.closeObject(--level);
            value = value.getParent();
        }
    }


    public class JsonIdxStructParser implements Parser ,Enumeration {

        @Override
        public void parse() {
            if(validateAndStart()){
                do{
                    StructSwap.Entry entry = getStructSwap().peeking();
                    value = entry.value();
                    peek().parse();
                }while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public boolean validateAndStart() {
            component.openArray(level++);
            return getStructSwap().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            boolean b = getStructSwap().hasNext();
            if(b){
                component.toNext(level);
            }
            return b;
        }

        @Override
        public void validateAndEnd() {
            component.closeArray(--level);
            value = value.getParent();
        }
    }

}
