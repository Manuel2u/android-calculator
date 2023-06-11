package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result;
    MaterialButton button_c, button_ac, button_P_or_N, button_percent;
    MaterialButton button_divide, button_x, button_subtract, button_add, button_equalTo;
    MaterialButton button_0, button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9;
    MaterialButton button_dot;

    StringBuilder expression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);

        expression = new StringBuilder();

        assignId(button_c, R.id.button_c);
        assignId(button_ac, R.id.button_ac);
        assignId(button_P_or_N, R.id.button_P_or_N);
        assignId(button_percent, R.id.button_percentage);
        assignId(button_divide, R.id.button_divide);
        assignId(button_x, R.id.button_x);
        assignId(button_subtract, R.id.button_subtract);
        assignId(button_add, R.id.button_add);
        assignId(button_equalTo, R.id.button_equal);
        assignId(button_0, R.id.button_0);
        assignId(button_1, R.id.button_1);
        assignId(button_2, R.id.button_2);
        assignId(button_3, R.id.button_3);
        assignId(button_4, R.id.button_4);
        assignId(button_5, R.id.button_5);
        assignId(button_6, R.id.button_6);
        assignId(button_7, R.id.button_7);
        assignId(button_8, R.id.button_8);
        assignId(button_9, R.id.button_9);
        assignId(button_dot, R.id.button_dot);
    }

    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();

        if (v.getId() == R.id.button_0 || v.getId() == R.id.button_1 ||
                v.getId() == R.id.button_2 || v.getId() == R.id.button_3 ||
                v.getId() == R.id.button_4 || v.getId() == R.id.button_5 ||
                v.getId() == R.id.button_6 || v.getId() == R.id.button_7 ||
                v.getId() == R.id.button_8 || v.getId() == R.id.button_9 ||
                v.getId() == R.id.button_dot) {
            // Append digit or decimal point to the expression
            expression.append(buttonText);
        } else if (v.getId() == R.id.button_c) {
            // Remove the last character from the expression
            if (expression.length() > 0) {
                expression.deleteCharAt(expression.length() - 1);
            }
            if (expression.length() == 0) {
                result.setText("0");
                return;
            }
        } else if (v.getId() == R.id.button_ac) {
            // Clear the entire expression
            expression.setLength(0);
            result.setText("0");
            return;
        } else if (v.getId() == R.id.button_add || v.getId() == R.id.button_subtract ||
                v.getId() == R.id.button_divide) {
            // Append the operator to the expression
            if (expression.length() > 0) {
                char lastChar = expression.charAt(expression.length() - 1);
                if (!isOperator(lastChar)) {
                    expression.append(" ").append(buttonText).append(" ");
                }
            }
        } else if (v.getId() == R.id.button_x) {
            // Append the multiplication operator to the expression
            if (expression.length() > 0) {
                char lastChar = expression.charAt(expression.length() - 1);
                if (!isOperator(lastChar)) {
                    expression.append(" * ");
                }
            }
        } else if (v.getId() == R.id.button_equal) {
            // Evaluate the expression
            try {
                double resultValue = evaluateExpression(expression.toString());
                expression.setLength(0);
                expression.append(formatResult(resultValue));
            } catch (ArithmeticException e) {
                expression.setLength(0);
                expression.append("Error");
            }
        }

        result.setText(expression.toString());
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private String formatResult(double result) {
        // Use DecimalFormat to format the result with commas for thousands
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(result);
    }


    private double evaluateExpression(String expression) {
        String[] tokens = expression.split(" ");
        List<Double> operands = new ArrayList<>();
        List<String> operators = new ArrayList<>();

        for (String token : tokens) {
            if (isOperator(token)) {
                operators.add(token);
            } else {
                try {
                    double operand = Double.parseDouble(token);
                    operands.add(operand);
                } catch (NumberFormatException e) {
                    // Invalid operand
                    return 0;
                }
            }
        }

        if (operands.size() != operators.size() + 1) {
            // Invalid expression
            return 0;
        }

        for (int i = 0; i < operators.size(); i++) {
            String operator = operators.get(i);
            double operand1 = operands.get(i);
            double operand2 = operands.get(i + 1);
            double result;

            switch (operator) {
                case "+":
                    result = operand1 + operand2;
                    break;
                case "-":
                    result = operand1 - operand2;
                    break;
                case "X":
                case "*":
                    result = operand1 * operand2;
                    break;
                case "/":
                    if (operand2 != 0) {
                        result = operand1 / operand2;
                    } else {
                        // Division by zero error
                        return 0;
                    }
                    break;
                default:
                    // Invalid operator
                    return 0;
            }

            operands.set(i + 1, result);
        }

        return operands.get(operands.size() - 1);
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("X") || token.equals("/") || token.equals("*");
    }
}
