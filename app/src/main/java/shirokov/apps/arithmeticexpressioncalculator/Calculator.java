package shirokov.apps.arithmeticexpressioncalculator;


import java.util.EmptyStackException;
import java.util.Stack;

// В КАЧЕСТВЕ ЛЮБОГО КОРНЯ ИСПОЛЬЗУЕМ a^(b/c)

class Calculator {

    static String expressionToRPN(String expression) {
        StringBuilder current = new StringBuilder();  // Строка польской нотации (здесь будут
        Stack<Character> stack = new Stack<>();
        int priority;

        for (int i = 0; i < expression.length(); i++) {    // Проходимся по выражению

            priority = getPriority(expression.charAt(i));   // Считаем приоритет текущего символа в строке

            if (priority == 0) {   // Если текущий символ является цифрой или пробелом  (имеет приоритет 0) - то
                current.append(expression.charAt(i));  // добавляем в строку
            } else if (priority == 1) {    // Если текущий символ является открывающей скобкой то
                stack.push(expression.charAt(i));
            } else if (priority > 1) {   //  Если текущий символ какой либо из операторов (+,-,*,/,^) то
                current.append(' '); // к текущей строке прибавляем пробел (идет после операнда)
                while (!stack.empty()) {
                    if (getPriority(stack.peek()) >= priority) {
                        current.append(stack.pop());
                    } else {
                        break;
                    }
                }
                stack.push(expression.charAt(i));
            } else if (priority == -1) {
                current.append(' ');
                while (getPriority(stack.peek()) != 1) {
                    current.append(stack.pop());
                }

                stack.pop(); // "выбрасываем" открывающую скобку из стека
            }

        }

        while (!stack.empty()) {   // Пока стек не станет пустым:
            current.append(stack.pop()); // выталкиваем из стека оставшиеся операторы в строку
        }

        return current.toString();
    }

    static double RPNtoAnswer(String rpn) throws IndexOutOfBoundsException, EmptyStackException, StringIndexOutOfBoundsException {


        StringBuilder operand = new StringBuilder("");
        Stack<Double> stack = new Stack<>();

        for (int i = 0; i < rpn.length(); i++) {
            if (rpn.charAt(i) == ' ') {
                continue;
            }

            if (getPriority(rpn.charAt(i)) == 0) {   // Если текущий символ в польской записи является цифрой:
                while (rpn.charAt(i) != ' ' && getPriority(rpn.charAt(i)) == 0) {  // то пока цифры идут подряд
                    operand.append(rpn.charAt(i++));   //
                    if (i == rpn.length()) {
                        break;
                    }
                }

                stack.push(Double.parseDouble(operand.toString()));  // Помещаем в стек текущий операнд
                operand = new StringBuilder(""); // Сбрасываем
            }

                if (getPriority(rpn.charAt(i)) > 1) {

                    if (rpn.charAt(i) == '!') {
                        stack.push(-stack.pop());
                        continue;
                    }

                    double a = stack.pop();
                    double b = stack.pop();



                    if (rpn.charAt(i) == '+') {
                        stack.push(a + b);
                    } else if (rpn.charAt(i) == '-') {
                        stack.push(b - a);
                    } else if (rpn.charAt(i) == '*' || rpn.charAt(i) == '\u00d7' || rpn.charAt(i) == 'x') {
                        stack.push(a * b);
                    } else if (rpn.charAt(i) == '/' || rpn.charAt(i) == ':' || rpn.charAt(i) == '\u00f7') {
                        stack.push(b / a);
                    } else if (rpn.charAt(i) == '^') {
                        stack.push(Math.pow(b,a));
                    }
                }
            }


        return stack.pop();
    }

    private static int getPriority(char token) {

        switch (token) {
            case '^':
                return 5;
            case '!':
                return 4;
            case '*':
            case 'x':
            case '/':
            case ':':
            case '\u00f7':   // ÷  - этот знак
            case '\u00d7':  // ×  - этот знак
                return 3;
            case '+':
            case '-':
                return 2;
            case '(':
                return 1;
            case ')':
                return -1;
            default:
                return 0;

        }

    }

}