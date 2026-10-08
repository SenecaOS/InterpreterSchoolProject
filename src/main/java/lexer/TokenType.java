package lexer;

public enum TokenType {

    LOOP,
    IF,
    IDENTIFIER,
    VALUE,

    PLUS,
    MINUS,

    LEFT_PARENS,
    RIGHT_PARENS,

    LEFT_SQBRACKET,
    RIGHT_SQBRACKET,

    LEFT_CURLYBRAC,
    RIGHT_CURLYBRAC,

    STAR,
    SEMICOLON,
    DOT,
    SLASH,
    EOF,
    STRING,
    NOT,
    NOT_EQUALS,
    OR,
    AND,

    LESSER,
    GREATER,
    GREATER_EQUALS,
    LESS_EQUALS,

    SMTH,
    TRUE,
    ASSIGNMENT,
    EQUALS,
    ELSE,
    TERMINATOR,
    RETURN,
    NOTH,
    FALSE,

    COMMA,
    PRINT,
    FUNCTION
}
