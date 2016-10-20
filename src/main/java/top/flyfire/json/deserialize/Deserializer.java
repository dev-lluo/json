package top.flyfire.json.deserialize;

import top.flyfire.json.Parser;
import top.flyfire.json.Peeker;
import top.flyfire.json.Structure;
import top.flyfire.json.Token;
import top.flyfire.json.component.JsonComponent;

/**
 * Created by shyy_work on 2016/6/21.
 *
 */
@SuppressWarnings("all")
public class Deserializer implements Peeker {

    private String source;

    private JsonComponent component;

    private int cursor,cursorBound,level;

    private Parser STRUCTEDPARSER;

    private Parser INDEXEDPARSER;

    private Parser PRIMITIVEPARSER;

    public Deserializer(String source, JsonComponent component) {
        super();
        this.source = source;
        this.cursor = this.level = 0;
        this.cursorBound = source.length();
        this.component = component;
        STRUCTEDPARSER = new ObjectParser();
        INDEXEDPARSER = new ArrayParser();
        PRIMITIVEPARSER = new PrimitiveParser();
    }

    public void deserialize() {
        peek().parse();
    }


    @Override
    public Parser peek() {
        char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before value
        if(Tokenizer.isArrayStart(dest)){
            if(!roll()) throw new RuntimeException(this.source);
            component.openArray(level++);
            return INDEXEDPARSER;
        }else if(Tokenizer.isObjectStart(dest)){
            if(!roll()) throw new RuntimeException(this.source);
            component.openObject(level++);
            return STRUCTEDPARSER;
        }else{
            return PRIMITIVEPARSER;
        }
    }

    private char fetch(){
        return source.charAt(cursor);
    }

    public char fetchIgnoreisInvisibleChar(){
        char dest;
        while(Tokenizer.isInvisibleChar(dest = source.charAt(cursor))){
            if(!roll()) throw new RuntimeException(this.source);
        };
        return dest;
    }

    private boolean roll(){
        return ++cursor<cursorBound;
    }

    private class ObjectParser implements Parser ,Structure,Peeker {

        private Parser PRIMITIVEPARSER;

        public ObjectParser() {
            super();
            PRIMITIVEPARSER = new ObjectPrimitiveParser();
        }

        @Override
        public void parse() {
            if(validateAndStart()){
                do{

                    key : {
                        fetchIgnoreisInvisibleChar();
//                        int start = cursor;
//                        int end = -1;
//                        while(roll()&&end==-1){
//                            if(Tokenizer.isPrp2Val(fetch())){
//                                end = cursor;
//                            }
//                        }
                        int start = cursor, end = -1;char dest,struct = ((struct = fetch())== Token.D_QUOTE||struct==Token.S_QUOTE)?struct:Token.EMPTY;
                        //single quote and double quote
                        while(roll()&&struct!=Token.EMPTY){
                            if((struct=((dest = fetch())==struct?Token.EMPTY:struct))==Token.EMPTY){
                                if (!roll())throw new RuntimeException(source);
                                end = cursor;
                                break;
                            }
                        }
                        //end single quote and double quote
                        if(end>0) {
                            //skip invisible char after single quote and double quote
                            while (!Tokenizer.isPrp2Val(dest = fetch())) {
                                if (!roll())throw new RuntimeException(source);
                            }
                        }else{
                            //non single/double quote read
                            while (!Tokenizer.isPrp2Val(dest = fetch())) {
                                //invisible char skip
                                if( end < 0 && Tokenizer.isInvisibleChar(dest)){
                                    end = cursor;
                                }else if (end>0 && !Tokenizer.isInvisibleChar(dest)){
                                    throw new RuntimeException(source);
                                }
                                if (!roll())throw new RuntimeException(source);
                            }
                            if(end<0){
                                end = cursor;
                            }
                        }
                        roll();
                        String property = source.substring(start,end);
                        component.indexing(property,level);
                    }

                    value : {
                        peek().parse();
                    }

                }while(hasNext());
            }
            validateAndEnd();
        }

        @Override
        public void validateAndEnd() {
            component.closeObject(--level);
        }

        @Override
        public boolean hasNext() {
            char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before comma
            roll();
            if(Tokenizer.isObjectEnd(dest)){
                return false;
            }else if(Tokenizer.isNext(dest)){
                component.toNext(level);
                return true;
            }else{
                throw new RuntimeException(source.substring(0,cursor));
            }
        }

        @Override
        public boolean validateAndStart() {
            char dest = fetch();
            if(Tokenizer.isObjectEnd(dest)){
                roll();
                return false;
            }else{
                return true;
            }
        }

        @Override
        public Parser peek() {
            char dest;
            if(Tokenizer.isArrayStart(dest = fetchIgnoreisInvisibleChar())){//ignore invisible char before value
                if(!roll()) throw new RuntimeException(source);
                component.openArray(level++);
                return INDEXEDPARSER;
            }else if(Tokenizer.isObjectStart(dest)){
                if(!roll()) throw new RuntimeException(source);
                component.openObject(level++);
                return STRUCTEDPARSER;
            }else{
                return PRIMITIVEPARSER;
            }
        }
    }

    private class ArrayParser implements Parser ,Structure , Peeker {

        private Parser PRIMITIVEPARSER;

        public ArrayParser() {
            super();
            PRIMITIVEPARSER = new IndexedPrimitiveParser();
        }

        @Override
        public void parse() {
            if(validateAndStart()){
                int i = 0;
                do{
                    index : {
                        component.indexing(i++,level);
                    }
                    value : {
                        peek().parse();
                    }
                }while(hasNext());
            }
            validateAndEnd();
        }


        @Override
        public void validateAndEnd() {
            component.closeArray(--level);
        }

        @Override
        public boolean hasNext() {
            char dest = fetchIgnoreisInvisibleChar();//ignore invisible char before comma
            roll();
            if (Tokenizer.isArrayEnd(dest)){
                return false;
            }else if(Tokenizer.isNext(dest)){
                component.toNext(level);
                return true;
            }else{
                throw new RuntimeException(source.substring(0,cursor));
            }
        }

        @Override
        public boolean validateAndStart() {
            char dest;
            if(Tokenizer.isArrayEnd(dest = fetch())){
                roll();
                return false;
            }else{
                return true;
            }
        }

        @Override
        public Parser peek() {
            char dest ;
            if(Tokenizer.isArrayStart(dest = fetchIgnoreisInvisibleChar())){//ignore invisible char before value
                if(!roll()) throw new RuntimeException(source);
                component.openArray(level++);
                return INDEXEDPARSER;
            }else if(Tokenizer.isObjectStart(dest)){
                if(!roll()) throw new RuntimeException(source);
                component.openObject(level++);
                return STRUCTEDPARSER;
            }else{
                return PRIMITIVEPARSER;
            }
        }
    }

    private class PrimitiveParser implements Parser {
        @Override
        public void parse() {
            component.value(source,level);
        }
    }

    private class ObjectPrimitiveParser implements Parser {
        @Override
        public void parse() {
            int start = cursor, end = -1;char dest,struct = ((struct = fetch())== Token.D_QUOTE||struct==Token.S_QUOTE)?struct:Token.EMPTY;
            //single quote and double quote
            while(roll()&&struct!=Token.EMPTY){
                if((struct=((dest = fetch())==struct?Token.EMPTY:struct))==Token.EMPTY){
                    if (!roll())throw new RuntimeException(source);
                    end = cursor;
                    break;
                }
            }
            //end single quote and double quote
            if(end>0) {
                //skip invisible char after single quote and double quote
                while (!Tokenizer.isNext(dest = fetch()) && !Tokenizer.isObjectEnd(dest)) {
                    if (!roll())throw new RuntimeException(source);
                }
            }else{
                //non single/double quote read
                while (!Tokenizer.isNext(dest = fetch()) && !Tokenizer.isObjectEnd(dest)) {
                    //invisible char skip
                    if( end < 0 && Tokenizer.isInvisibleChar(dest)){
                        end = cursor;
                    }else if (end>0 && !Tokenizer.isInvisibleChar(dest)){
                        throw new RuntimeException(source);
                    }
                    if (!roll())throw new RuntimeException(source);
                }
                if(end<0){
                    end = cursor;
                }
            }
            component.value(source.substring(start, end),level);
        }
    }

    private class IndexedPrimitiveParser implements Parser {
        @Override
        public void parse() {
            int start = cursor,end = -1;char dest ,struct = ((struct =fetch())=='\"'||struct=='\'')?struct:'\0';
            while(roll()&&struct!=Token.EMPTY){
                if((struct=((dest = fetch())==struct?Token.EMPTY:struct))==Token.EMPTY){
                    if (!roll())throw new RuntimeException(source);
                    end = cursor;
                    break;
                }
            }
            //end single quote and double quote
            if(end>0) {
                //skip invisible char after single quote and double quote
                while (!Tokenizer.isNext(dest = fetch()) && !Tokenizer.isArrayEnd(dest)) {
                    if (!roll())throw new RuntimeException(source);
                }
            }else{
                //non single/double quote read
                while (!Tokenizer.isNext(dest = fetch()) && !Tokenizer.isArrayEnd(dest)) {
                    //invisible char skip
                    if( end < 0 && Tokenizer.isInvisibleChar(dest)){
                        end = cursor;
                    }else if (end>0 && !Tokenizer.isInvisibleChar(dest)){
                        throw new RuntimeException(source);
                    }
                    if (!roll())throw new RuntimeException(source);
                }
                if(end<0){
                    end = cursor;
                }
            }
            component.value(source.substring(start, end),level);
        }
    }

}
