package top.flyfire.json.serialize;


import top.flyfire.json.*;
import top.flyfire.json.serialize.component.Render;
import top.flyfire.json.serialize.component.Structed;
import top.flyfire.json.serialize.component.defaults.JsonIdxStruct;
import top.flyfire.json.serialize.component.defaults.JsonKyStruct;
import top.flyfire.json.serialize.component.defaults.JsonRender;
import top.flyfire.json.serialize.component.defaults.JsonValued;

/**
 * Created by devll on 2016/11/9.
 */
public class Serializer implements Peeker ,Parser {

    private Render render;

    private JsonComponent component;

    private int level;

    private Parser JSONVALUEDPARSER, JSONKYSTRUCTPARSER, JSONIDXSTRUCTPARSER;

    public Serializer(Object object, JsonComponent component) {
        this.level = 0;
        this.component = component;
        render = JsonRender.buildRender(object,null);
        JSONVALUEDPARSER = new JsonValuedParser();
        JSONKYSTRUCTPARSER = new JsonKyStructParser();
        JSONIDXSTRUCTPARSER = new JsonIdxStructParser();
    }

    private JsonValued getJsonValued(){
        return (JsonValued) render;
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
        if(render instanceof JsonValued){
            return JSONVALUEDPARSER;
        }else if(render instanceof JsonKyStruct){
            return JSONKYSTRUCTPARSER;
        }else if(render instanceof JsonIdxStruct){
            return JSONIDXSTRUCTPARSER;
        }else{
            throw new RuntimeException();
        }
    }

    public class JsonValuedParser implements Parser{

        @Override
        public void parse() {
            component.value(getJsonValued().toJson(),level);
            render = getJsonValued().render();
        }
    }

    public class JsonKyStructParser implements Parser ,Structure {

        @Override
        public void parse() {
            if(validateAndStart()){
                do{
                    Structed.Transfer transfer = getJsonKyStruct().peeking();
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
            render = getJsonKyStruct().render();
        }
    }


    public class JsonIdxStructParser implements Parser ,Structure {

        @Override
        public void parse() {
            if(validateAndStart()){
                do{
                    Structed.Transfer transfer = getJsonIdxStruct().peeking();
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
            render = getJsonIdxStruct().render();
        }
    }

}
