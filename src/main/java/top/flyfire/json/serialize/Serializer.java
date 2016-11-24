package top.flyfire.json.serialize;


import top.flyfire.json.Json;
import top.flyfire.json.Parser;
import top.flyfire.json.Peeker;
import top.flyfire.json.Structure;
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

    private Parser JSONVALUEDPARSER, JSONKYSTRUCTPARSER, JSONIDXSTRUCTPARSER;

    public Serializer(Object object) {
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
            System.out.print(getJsonValued().toJson());
            render = getJsonValued().render();
        }
    }

    public class JsonKyStructParser implements Parser ,Structure {

        @Override
        public void parse() {
            if(validateAndStart()){
                do{
                    Structed.Transfer transfer = getJsonKyStruct().peeking();
                    System.out.print(transfer.indexing());
                    System.out.print(":");
                    render = transfer.value();
                   peek().parse();
                }while (hasNext());
            }
            validateAndEnd();
        }

        @Override
        public boolean validateAndStart() {
            System.out.print("{");
            return getJsonKyStruct().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            boolean b = getJsonKyStruct().hasNext();
            if(b){
                System.out.print(",");
            }
            return b;
        }

        @Override
        public void validateAndEnd() {
            System.out.print("}");
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
            System.out.print("[");
            return getJsonIdxStruct().notEmptyAndPeekStart();
        }

        @Override
        public boolean hasNext() {
            boolean b = getJsonIdxStruct().hasNext();
            if(b){
                System.out.print(",");
            }
            return b;
        }

        @Override
        public void validateAndEnd() {
            System.out.print("]");
            render = getJsonIdxStruct().render();
        }
    }

}
