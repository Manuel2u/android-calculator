package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

        // Assign IDs and set OnClickListener
        button_c = findViewById(R.id.button_c);
        button_c.setOnClickListener(this);
        button_ac = findViewById(R.id.button_ac);
        button_ac.setOnClickListener(this);
        button_P_or_N = findViewById(R.id.button_P_or_N);
        button_P_or_N.setOnClickListener(this);
        button_percent = findViewById(R.id.button_percentage);
        button_percent.setOnClickListener(this);
        button_divide = findViewById(R.id.button_divide);
        button_divide.setOnClickListener(this);
        button_x = findViewById(R.id.button_x);
        button_x.setOnClickListener(this);
        button_subtract = findViewById(R.id.button_subtract);
        button_subtract.setOnClickListener(this);
        button_add = findViewById(R.id.button_add);
        button_add.setOnClickListener(this);
        button_equalTo = findViewById(R.id.button_equal);
        button_equalTo.setOnClickListener(this);
        button_0 = findViewById(R.id.button_0);
        button_0.setOnClickListener(this);
        button_1 = findViewById(R.id.button_1);
        button_1.setOnClickListener(this);
        button_2 = findViewById(R.id.button_2);
        button_2.setOnClickListener(this);
        button_3 = findViewById(R.id.button_3);
        button_3.setOnClickListener(this);
        button_4 = findViewById(R.id.button_4);
        button_4.setOnClickListener(this);
        button_5 = findViewById(R.id.button_5);
        button_5.setOnClickListener(this);
        button_6 = findViewById(R.id.button_6);
        button_6.setOnClickListener(this);
        button_7 = findViewById(R.id.button_7);
        button_7.setOnClickListener(this);
        button_8 = findViewById(R.id.button_8);
        button_8.setOnClickListener(this);
        button_9 = findViewById(R.id.button_9);
        button_9.setOnClickListener(this);
        button_dot = findViewById(R.id.button_dot);
        button_dot.setOnClickListener(this);
    }

    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    boolean isResultDisplayed = false;

    @Override
    public void onClick(View v) {
        MaterialButton button = (MaterialButton) v;
        String buttonText = button.getText().toString();

        if (isResultDisplayed) {
            // Replace the screen with zero and start a new expression
            expression.setLength(0);
            isResultDisplayed = false;
        }

        if (v.getId() == R.id.button_0 || v.getId() == R.id.button_1 ||
                v.getId() == R.id.button_2 || v.getId() == R.id.button_3 ||
                v.getId() == R.id.button_4 || v.getId() == R.id.button_5 ||
                v.getId() == R.id.button_6 || v.getId() == R.id.button_7 ||
                v.getId() == R.id.button_8 || v.getId() == R.id.button_9 ||
                v.getId() == R.id.button_dot) {
            // Append digit or decimal point to the expression
            expression.append(buttonText);
            updateResultDisplay(expression.toString());
        } else if (v.getId() == R.id.button_percentage) {
            if (!endsWithOperator(expression)) {
                float expressionValue = Float.parseFloat(expression.toString());
                System.out.println(expressionValue);
                float percentage = (float) (expressionValue * 0.01);




                // Clear the expression and append the percentage value to it
                expression.setLength(0);
                expression.append(percentage);
                System.out.println(expression.toString());

                updateResultDisplay(expression.toString());
            }
        } else if (v.getId() == R.id.button_c) {
            // Remove the last character from the expression
            if (expression.length() > 0) {
                expression.deleteCharAt(expression.length() - 1);
                updateResultDisplay(expression.toString());
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
        } else if (v.getId() == R.id.button_equal) {
            // Evaluate the expression
            try {
                if (!endsWithOperator(expression)) {
                    double resultValue = evaluateExpression(expression.toString());
                    expression.setLength(0);
                    expression.append(formatResult(resultValue));
                    result.setText(expression.toString());
                    isResultDisplayed = true;  // Set the result displayed flag to true
                }
            } catch (ArithmeticException e) {
                expression.setLength(0);
                expression.append("Error");
                result.setText(expression.toString());
            }
        } else if (isOperatorButton(v.getId())) {
            String[] tokens = expression.toString().split(" ");
            int numTokens = tokens.length;

            // Check if the last token is an operator
            if (numTokens > 0 && isOperator(tokens[numTokens - 1])) {
                // Remove the last token (operator)
                expression.delete(expression.length() - tokens[numTokens - 1].length() - 1, expression.length());
            }

            // Append the operator to the expression
            expression.append(" ").append(buttonText).append(" ");

            // Update the result display
            updateResultDisplay(expression.toString());
        }
    }

    private boolean isOperatorButton(int buttonId) {
        return buttonId == R.id.button_add ||
                buttonId == R.id.button_subtract ||
                buttonId == R.id.button_x ||
                buttonId == R.id.button_divide;
    }

    // Helper method to check if the expression ends with an operator
    private boolean endsWithOperator(CharSequence expression) {
        return expression.length() > 0 && isOperator(expression.charAt(expression.length() - 1));
    }

    private void updateResultDisplay(String expression) {
        StringBuilder formattedExpression = new StringBuilder();

        // Split the expression into numeric portions and operators
        String[] parts = expression.split(" ");
        int partsLength = parts.length;

        // Format each part individually
        for (int i = 0; i < partsLength; i++) {
            String part = parts[i];

            // Skip empty parts
            if (part.isEmpty()) {
                continue;
            }

            // Format the number or operator
            if (isNumeric(part)) {
                System.out.println(part);

//                part = formatNumber(part);
                System.out.println(part);
            } else if (part.equalsIgnoreCase("x")) {
                part = "*";
            }

            formattedExpression.append(part);

            // Add a space after each part except the last one
            if (i < partsLength - 1) {
                formattedExpression.append(" ");
            }
        }

        result.setText(formattedExpression.toString());
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private String formatResult(double result) {
        // Use DecimalFormat to format the result with commas for thousands
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(result);
    }

    private String formatPercent(String number) {

            // Format the number with commas
          DecimalFormat  decimalFormat = new DecimalFormat("#,###.####");

        return decimalFormat.format(Double.parseDouble(number));
    }
    private String formatNumber(String number) {


        // Format the number with commas
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(Double.parseDouble(number));
    }

    private boolean isNumeric(String str) {
        // Check if the string is numeric
        str = str.trim();
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(str).matches();
    }

    private double evaluateExpression(String expression) {
        String[] tokens = expression.split(" ");
        List<Double> operands = new ArrayList<>();
        List<String> operators = new ArrayList<>();

        // Remove any empty tokens caused by consecutive operators
        for (String token : tokens) {
            if (!token.isEmpty()) {
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
        }

        if (operands.size() == 0) {
            // No operands found
            return 0;
        }

        double result = operands.get(0);
        int operatorIndex = 0;

        for (int i = 1; i < operands.size(); i++) {
            String operator = operators.get(operatorIndex);
            double operand = operands.get(i);

            switch (operator) {
                case "+":
                    result += operand;
                    break;
                case "-":
                    result -= operand;
                    break;
                case "*":
                    result *= operand;
                    break;
                case "/":
                    if (operand != 0) {
                        result /= operand;
                    } else {
                        // Division by zero error
                        return 0;
                    }
                    break;
                default:
                    // Invalid operator
                    return 0;
            }

            operatorIndex++;
        }

        return result;
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }
}
