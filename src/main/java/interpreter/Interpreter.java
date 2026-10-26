package interpreter;

import lexer.Token;
import parser.Expr;

public class Interpreter implements Expr.Visitor<Object>{
    @Override
    public Object visitBinary(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch(expr.operator.getTokenType()){
            case GREATER:
                checkNumberOperand(expr.operator, right);
                return (double)left > (double)right;
            case GREATER_EQUALS:
                checkNumberOperand(expr.operator, right);
                return (double)left >= (double)right;
            case LESSER:
                checkNumberOperand(expr.operator, right);
                return (double)left < (double)right;
            case LESS_EQUALS:
                checkNumberOperand(expr.operator, right);
                return (double)left <= (double)right;
            case NOT_EQUALS: return !isEqual(left, right);
            case EQUALS: return isEqual(left, right);
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return (double)left - (double)right;
            case SLASH:
                checkNumberOperand(expr.operator, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperand(expr.operator, right);
                return (double)left*(double)right;
            case PLUS:
                if(left instanceof Double && right instanceof Double){
                    return (double)left + (double)right;
                }
                if(left instanceof String && right instanceof String){
                    return (String)left + (String)right;
                }
                throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings");


        }
        return null;
    }

    public void interpret(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            System.out.println("Error on line " + error.token.getLine() + ". Wrong token type: " + error.token.getTokenType().toString());

        }
    }

    private String stringify(Object object) {
        if(object == null) return "noth";
        if(object instanceof Double){
            String text = object.toString();
            if(text.endsWith(".0")){
                text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }


    private void checkNumberOperand(Token operator, Object operand) {
        if(operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number");
    }

    private boolean isEqual(Object a, Object b) {
        if(a == null && b == null) return true;
        if(a == null) return false;

        return a.equals(b);

    }

    @Override
    public Object visitLiteral(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnary(Expr.Unary expr) {
        Object right = evaluate(expr.expr);

        switch(expr.operator.getTokenType()){
            case MINUS:
                return -(double)right;
            case NOT:
                return !isTruthy(right);

        }
        return null;
    }

    private boolean isTruthy(Object object) {
        if(object == null) return false;
        if(object instanceof Boolean) return (boolean)object;
        return true;
    }

    @Override
    public Object visitGrouping(Expr.Grouping expr) {
        return evaluate(expr.expr);
    }



    @Override
    public Object visitCall(Expr.Call expr) {
        return null;
    }

    @Override
    public Object visitVariable(Expr.Variable expr) {
        return null;
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }


}
