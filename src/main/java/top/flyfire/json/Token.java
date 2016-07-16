package top.flyfire.json;

/**
 * Created by shyy_work on 2016/6/16.
 */
public interface Token {
    char INX_START = '[',
    INX_END = ']',
    STC_START = '{',
    STC_K2V = ':',
    STC_END = '}',
    NEXT = ',',
    SPACE = ' ',
    TAB = '\t',
    RLINE = '\r',
    NLINE = '\n',
    ESCAPE = '\\',
    S_QUOTE = '\'',
    D_QUOTE = '\"',
    EMPTY = '\0';

}
