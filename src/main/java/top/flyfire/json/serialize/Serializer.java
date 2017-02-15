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

    private JsonValue render;

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
        render = JsonValue.buildRender(object,null);
        this.config = config;
        JSONVALUEDPARSER = new JsonValuedParser();
        JSONKYSTRUCTPARSER = new JsonKyStructParser();
        JSONIDXSTRUCTPARSER = new JsonIdxStructParser();
    }

    private JsonValue getJsonValued(){
        return (JsonValue) render;
    }

    private JsonKyStruct getJsonKyStruct(){
        return (JsonKyStruct) render;
    }

    private JsonIdxStruct getJsonIdxStruct(){
        return (JsonIdxStruct) render;
    }

    @Override
    public void parse() {
        peek().parse();
    }

    @Override
    public Parser peek() {
        if(render instanceof JsonKyStruct){
            return JSONKYSTRUCTPARSER;
        }else if(render instanceof JsonIdxStruct){
            return JSONIDXSTRUCTPARSER;
        }else if(render instanceof JsonValue){
            return JSONVALUEDPARSER;
        }else {
            throw new RuntimeException();
        }
    }

    public class JsonValuedParser implements Parser{

        @Override
        public void parse() {
            component.value(config.value2String(getJsonValued().getCached()),level);
            render = getJsonValued().getParent();
        }
    }

    public class JsonKyStructParser implements Parser ,Structure {

        @Override
        public void parse() {
            if(validateAndStart()){
                do{
                    StructSwap.Transfer transfer = getJsonKyStruct().peeking();
                    component.indexing(transfer.indexing(),level);
                    render = transfer.value();
                   peek().parse();
                }while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public boolean validateAndStart() {
            component.openObject(level++);
            return getJsonKyStruct().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            boolean b = getJsonKyStruct().hasNext();
            if(b){
                component.toNext(level);
            }
            return b;
        }

        @Override
        public void validateAndEnd() {
            component.closeObject(--level);
            render = getJsonKyStruct().getParent();
        }
    }


    public class JsonIdxStructParser implements Parser ,Structure {

        @Override
        public void parse() {
            if(validateAndStart()){
                do{
                    StructSwap.Transfer transfer = getJsonIdxStruct().peeking();
                    render = transfer.value();
                    peek().parse();
                }while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public boolean validateAndStart() {
            component.openArray(level++);
            return getJsonIdxStruct().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            boolean b = getJsonIdxStruct().hasNext();
            if(b){
                component.toNext(level);
            }
            return b;
        }

        @Override
        public void validateAndEnd() {
            component.closeArray(--level);
            render = getJsonIdxStruct().getParent();
        }
    }

}
