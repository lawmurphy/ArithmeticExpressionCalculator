package shirokov.apps.arithmeticexpressioncalculator;
// Подгружаем необходимые классы
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.EmptyStackException;

public class MainActivity extends AppCompatActivity {

    public TextView inputExpression;  // Объявляем 3 объекта класса TextView. inputExpression будет строкой ввода пользователя
    public TextView resultString;     // resultString для строки с ответом
    public TextView rpnResultString;  // rpnResultString для строки с полученной обратной польской записью
    public int counter = 1;   // Счетчик

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {  // Метод, который вызывается при открытии приложения
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Устанавливаем для отображения activity_main.xml
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  // Отключаем возможность поворота экрана

        inputExpression = findViewById(R.id.inputString);  // Связываем нашу TextView переменную с элементом с id inputString (в activity_main)
        resultString = findViewById(R.id.resultString);   // То же самое делаем с остальными переменными
        rpnResultString = findViewById(R.id.rpnResultString);
    }

    @SuppressLint("SetTextI18n")
    public void onClickButtons(View view)  {  // Метод, который будет выполняться, если нажимается любая кнопка

            if (counter == 0) {  // Если счетчик равен 0 - то мы очищаем строки из всех элементов
                inputExpression.setText("");
                resultString.setText("");
                rpnResultString.setText("");
            }

            Button button = (Button) view;   // Даункастим до Button объект view, чтобы присвоить переменной класса Button
            String s = button.getText().toString(); // Получаем текст нажатой кнопки и кладем в переменную s
            String currentInputString = inputExpression.getText().toString();  // Получаем текущую строку из элемента inputExpression

        switch (s) {  //  В зависимости от того, какой у кнопки текст, совершаем различные операции
            case "clear":   // В случае если текст является "clear"
                inputExpression.setText("");  // Логично, что стираем строку введенную пользователем
                break;  // Дальше метод не выполняется

            case "унарный минус":
                inputExpression.setText(currentInputString + '⁃'); //
                counter = 1;
                break;

            case "backspace":
                try {
                    inputExpression.setText(currentInputString.substring(0, currentInputString.length() - 1));
                } catch (StringIndexOutOfBoundsException e) { // В случае, если пытаемся удалить последний символ пустой строки

                }
                break;

            case "=":
                try {
                    String RPNString = Calculator.expressionToRPN(currentInputString);
                    resultString.setText(String.valueOf(Calculator.RPNtoAnswer(RPNString)));
                    rpnResultString.setText(RPNString);
                } catch (EmptyStackException
                        | ArrayIndexOutOfBoundsException
                        | StringIndexOutOfBoundsException
                        | NumberFormatException e) {
                    inputExpression.setText("Неверно задано выражение!");
                }
                counter = 0;
                break;

            default:
                inputExpression.setText(currentInputString + s);
                counter = 1;
                break;
        }

    }




}
