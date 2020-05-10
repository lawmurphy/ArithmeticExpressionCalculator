package shirokov.apps.arithmeticexpressioncalculator;

// Класс реализующий алгоритм обратной польской записи
import java.util.EmptyStackException;
import java.util.Stack;

// (В КАЧЕСТВЕ ЛЮБОГО КОРНЯ ИСПОЛЬЗУЕМ a^(b/c) )

class Calculator {

    static String expressionToRPN(String expression) {    // Статический метод, преобразующий строку в ОПН
        StringBuilder current = new StringBuilder();  // Текущая строка польской нотации
        Stack<Character> stack = new Stack<>();  // Стек в котором будем хранить символы (операторы)
        int priority;  // Переменная будет хранить приоритет

        for (int i = 0; i < expression.length(); i++) {    // Проходимся по выражению

            priority = getPriority(expression.charAt(i));   // Считаем приоритет текущего символа в строке

            if (priority == 0) {   // Если текущий символ является цифрой или пробелом  (имеет приоритет 0) - то
                current.append(expression.charAt(i));  // добавляем в строку этот символ
            } else if (priority == 1) {    // Если текущий символ является открывающей скобкой то
                stack.push(expression.charAt(i));  // добавляем ее в стек
            } else if (priority > 1) {   //  Если текущий символ какой либо из операторов (+,-,*,/,^,⁃,!) то
                current.append(' '); // к текущей строке прибавляем пробел (отделяем операнд)
                while (!stack.empty()) {  // И пока стек не закончился
                    if (getPriority(stack.peek()) >= priority) {  // Если приоритет верхнего элемента в стеке выше чем приоритет текущего оператора
                        current.append(stack.pop());  // выталкиваем из стека верхний элемент и добавляем в текущую строку ОПН
                    } else {
                        break;  //  В ином случае выходим из цикла
                    }
                }
                stack.push(expression.charAt(i)); // В конце добавляем в стек текущий оператор
            } else if (priority == -1) {  // Если текущий символ - ")"
                current.append(' '); // отделяем операнд
                while (getPriority(stack.peek()) != 1) { // И пока не найдем в стеке открывающую скобку
                    current.append(stack.pop());   // мы будем выталкивать оттуда операторы и добавлять в строку ОПН
                }

                stack.pop(); // "выбрасываем" открывающую скобку из стека
            }

        }

        while (!stack.empty()) {   // Пока стек не станет пустым:
            current.append(stack.pop()); // выталкиваем из стека оставшиеся операторы в строку
        }

        return current.toString();  // Возвращаем готовую строку ОПН
    }


    // Этот метод считает ответ через ОПН, которую получает в качестве параметра
    // Также в сигнатуре метода добавлен throws который выбрасывает возможные исключения
    static double RPNtoAnswer(String rpn) throws IndexOutOfBoundsException, EmptyStackException, StringIndexOutOfBoundsException {

        StringBuilder operand = new StringBuilder("");  // Строка будет хранить текущий операнд
        Stack<Double> stack = new Stack<>();  // В стеке будут храниться операнды

        for (int i = 0; i < rpn.length(); i++) {  // Проходимся по ОПН строке
            if (rpn.charAt(i) == ' ') {  // Если текущий символ в ОПН строке пустой - то пропускаем итерацию
                continue;
            }

            if (getPriority(rpn.charAt(i)) == 0) {   // Если текущий символ в польской записи является цифрой:
                while (rpn.charAt(i) != ' ' && getPriority(rpn.charAt(i)) == 0) {  // то пока цифры идут подряд
                    operand.append(rpn.charAt(i++));   // добавляем эту цифру в текущий операнд
                    if (i == rpn.length()) { // если счетчик равен длине ОПН строки то выходим из цикла
                        break;
                    }
                }

                stack.push(Double.parseDouble(operand.toString()));  // Помещаем в стек текущий операнд
                operand = new StringBuilder(""); // Сбрасываем строку с операндом, переходим к следующему
            }

                if (getPriority(rpn.charAt(i)) > 1) {  // Если попался оператор

                    if (rpn.charAt(i) == '⁃' || rpn.charAt(i) == '!') {  // Если это унарный минус, то
                        stack.push(-stack.pop());  // Выталкиваем из стека верхний элемент, делаем его отрицательным и пихаем обратно
                        continue;  // и пропускаем итерацию, код дальше не выполняется
                    }

                    double a = stack.pop();  // Выталкиваем два элемента из стека и присваиваем их переменным
                    double b = stack.pop();

                    // Ниже выполняются операции относительно полученного символа
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

        return stack.pop();  // Возвращаем оставшийся последний элемент в стеке, который будет являться ответом
    }

    // С помощью этого приватного метода получаем приоритет символа, переданного в качестве параметра
    private static int getPriority(char token) {

        switch (token) {
            case '^':  // Возведение в степень имеет высший приоритет
                return 5;
            case '⁃':  // Унарный минус (2 варианта)
            case '!':
                return 4;
            case '*':  // 3 варианта знака умножения и знака деления
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
            default:  // В случае, если это цифра или пустой символ
                return 0;

        }

    }

}