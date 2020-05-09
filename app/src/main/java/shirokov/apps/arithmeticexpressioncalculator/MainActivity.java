package shirokov.apps.arithmeticexpressioncalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import java.util.EmptyStackException;

public class MainActivity extends AppCompatActivity {

    public TextView inputExpression;
    public TextView resultString;
    public TextView rpnResultString;
    public int counter = 1;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        inputExpression = findViewById(R.id.inputString);
        resultString = findViewById(R.id.resultString);
        rpnResultString = findViewById(R.id.rpnResultString);

    }

    @SuppressLint("SetTextI18n")
    public void onClickButtons(View view)  {

            if (counter == 0) {
                inputExpression.setText("");
                resultString.setText("");
                rpnResultString.setText("");
            }

            Button button = (Button) view;
            String s = button.getText().toString();
            String currentInputString = inputExpression.getText().toString();

            if (s.equals("clear")) {
                inputExpression.setText("");
            } else if (s.equals("x")) {
                inputExpression.setText(currentInputString + '*');
                counter = 1;
            } else if (s.equals("унарный минус")) {
                inputExpression.setText(currentInputString + '!');
                counter = 1;
            } else if (s.equals("backspace")) {
                try {
                    inputExpression.setText(currentInputString.substring(0, currentInputString.length() - 1));
                } catch (StringIndexOutOfBoundsException e) { // В случае, если пытаемся удалить последний символ пустой строки

                }
            } else if (s.equals("=")) {
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
            } else {
                inputExpression.setText(currentInputString + s);
                counter = 1;
            }
        }




}
