package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Main_Activity extends AppCompatActivity {

    StringBuilder temp = new StringBuilder();
    StringBuilder operator = new StringBuilder();
    Boolean wasOperator = false;
    Boolean decimalActive = false;
    StringBuilder buffer = new StringBuilder("0");
    Boolean initialized = false;

    private TextView display;
    private TextView result;
    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button AC;
    private Button C;
    private Button save;
    private Button recall;
    private Button addition;
    private Button subtract;
    private Button multiply;
    private Button divide;
    private Button equal;
    private Button decimalB;

    /*
    This function saves the values of fields in the app before
    the activity gets destroyed.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("displayText", display.getText().toString());
        outState.putString("resultText", result.getText().toString());
        outState.putString("temp_value", temp.toString());
        outState.putString("operator_value", operator.toString());
        outState.putBoolean("entered", initialized);
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the displays to variables
        display = findViewById(R.id.display_result);
        result = findViewById(R.id.display);

        if (savedInstanceState != null) {
            //if there is something saved when the activity is created
            //it copies the values from the variables to current variables
            String savedTemp = savedInstanceState.getString("temp_value");
            String savedOper = savedInstanceState.getString("operator_value");
            String displayText = savedInstanceState.getString("displayText", "0");
            String resultText = savedInstanceState.getString("resultText", "0");
            Boolean getIni = savedInstanceState.getBoolean("entered");
            if (savedTemp != null) {
                temp = new StringBuilder(savedTemp);  // Restore the StringBuilder content
            }
            if (savedOper != null) {
                operator = new StringBuilder(savedOper);  // Restore the StringBuilder content
            }
            display.setText(displayText);
            result.setText(resultText);
            initialized = getIni;

        }


        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        decimalB = findViewById(R.id.decimal_button);
        AC = findViewById(R.id.clearAll_button);
        C = findViewById(R.id.clear_button);
        save = findViewById(R.id.save);
        recall = findViewById(R.id.recall);
        addition = findViewById(R.id.plus_button);
        subtract = findViewById(R.id.minus_button);
        multiply = findViewById(R.id.multiply_button);
        divide = findViewById(R.id.divide_button);
        equal = findViewById(R.id.equals_button);

        Button[] buttons = {button0,button1,button2,button3,button4,button5,button6,button7,button8,button9,AC,C,save,recall,addition,subtract,multiply,divide,equal,decimalB};


        for(Button button : buttons){
            //using the array to set onClick listener on all the buttons at once
            button.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){
                    //defining what happens when each button is clicked
                    Button clickedButton = (Button)v;
                    String buttonText = clickedButton.getText().toString();

                    //handling the case when all the digits are clicked
                    if(buttonText.equals("0") || buttonText.equals("1") || buttonText.equals("2") || buttonText.equals("3") || buttonText.equals("4") || buttonText.equals("5") || buttonText.equals("6") || buttonText.equals("7") || buttonText.equals("8") || buttonText.equals("9")){
                        digitsClicked(buttonText);
                    }
                    //handling the case when the decimal is clicked
                    if(buttonText.equals(".")){
                        decimalPressed();
                    }

                    else if(buttonText.equals("S")){
                        buffer.append(temp.toString());
                    }

                    else if(buttonText.equals("R")){
                        recall();
                    }

                    else if(buttonText.equals("+") || buttonText.equals("-") || buttonText.equals("x") || buttonText.equals("/")){
                        operatorPressed(buttonText);
                    }

                    else if(buttonText.equals("=")){
                        equalPressed();
                    }

                    else if(buttonText.equals("AC")) {
                        allClear();
                    }

                    else if(buttonText.equals("C") && display.getText().length() != 0){
                        clear();
                    }



                }
            });
        }
    }




    private void digitsClicked(String buttonText){
        //saving the number typed by user as string
        temp.append(buttonText);
        //printing the text typed by user
        display.append(buttonText);
        //this below variable is used when two operators are selected back to back
        //in this block we know, this is for digits so we set it to false
        wasOperator = false;
        initialized = true;
    }

    private void decimalPressed(){
        if(!decimalActive){
            temp.append(".");
            display.append(".");
        }
        //decimal active is used so that we dont insert two decimal in the same number
        decimalActive = true;
    }

    //this method first deletes the current number typed by user then it attempts to remove
    //the number from display
    private void recall(){
        temp.setLength(0);
        temp.append(buffer.toString());
        StringBuilder removeNumber = new StringBuilder();
        removeNumber.append(display.getText());
        while(removeNumber.length() >= 1)
            if (removeNumber.charAt(removeNumber.length() - 1) == '0' || removeNumber.charAt(removeNumber.length() - 1) == '1' || removeNumber.charAt(removeNumber.length() - 1) == '2' || removeNumber.charAt(removeNumber.length() - 1) == '3' || removeNumber.charAt(removeNumber.length() - 1) == '4' || removeNumber.charAt(removeNumber.length() - 1) == '5' || removeNumber.charAt(removeNumber.length() - 1) == '6' || removeNumber.charAt(removeNumber.length() - 1) == '7' || removeNumber.charAt(removeNumber.length() - 1) == '8' || removeNumber.charAt(removeNumber.length() - 1) == '9') {
                removeNumber.deleteCharAt(removeNumber.length() - 1);
            }
        display.setText("");
        display.append(removeNumber.toString());
        display.append(temp.toString());
    }

    private void operatorPressed(String buttonText){

        //this if statement handles the case when the app is just opened and
        //the user presses an operator which would lead to a crash
        if(!initialized){}
        else if(wasOperator){
            //handling the case if operator and selected back to back
            display.setText(display.getText().subSequence(0,display.getText().length()-1)+buttonText);
            operator.setLength(0);
            operator.append(buttonText);
        }
        else {
            //taking the current number and then taking the previous number and
            //then applying the appropriate operator and displaying the result and reseting
            //the appropriate variables that need to be reset
            wasOperator = true;
            String previous_result = (String) result.getText();
            String current_result = temp.toString();
            double pre = Double.parseDouble(previous_result);
            double current = Double.parseDouble(current_result);
            double fi = current;
            String y = operator.toString();
            if (y.equals("+")) {
                fi = pre + current;
                operator.setLength(0);
            } else if (y.equals("-")) {
                fi = pre - current;
                operator.setLength(0);
            } else if (y.equals("x")) {
                fi = pre * current;
                operator.setLength(0);
            } else if (y.equals("/")) {
                if(current == 0){
                    display.setText("");
                    result.setText("0");
                    temp.setLength(0);
                    temp.append(0);
                    wasOperator = false;
                    operator.setLength(0);
                }
                else {
                    System.out.println(4);
                    fi = pre / current;
                    operator.setLength(0);
                }
            }
            decimalActive = false;
            result.setText(String.valueOf(fi));
            display.append(buttonText);
            operator.append(buttonText);
            temp.setLength(0);
        }
    }

    //Does the same thing as "operatorPressed" method
    private void equalPressed(){
        if(temp.length() == 0) temp.append(0);

        double current = Double.parseDouble(temp.toString());
        double pre = Double.parseDouble((String) result.getText());
        String y = operator.toString();
        double fi = 0;
        if (y.equals("+")) {
            fi = pre + current;
            operator.setLength(0);
        } else if (y.equals("-")) {
            fi = pre - current;
            operator.setLength(0);
        } else if (y.equals("x")) {
            fi = pre * current;
            operator.setLength(0);
        } else if (y.equals("/")) {
            if(current == 0){
                display.setText("");
                result.setText("0");
                temp.setLength(0);
                temp.append(0);
                wasOperator = false;
                operator.setLength(0);
            }
            else {
                fi = pre / current;
                operator.setLength(0);
            }
        } else if (y.equals("")){
            fi = Double.parseDouble(temp.toString());
        }
        display.setText(String.valueOf(fi));
        temp.setLength(0);
        temp.append(fi);
        result.setText("0");
        operator.setLength(0);
        wasOperator = false;
        decimalActive = false;
    }

    private void allClear(){
        display.setText("");
        result.setText("0");
        temp.setLength(0);
        temp.append(0);
        wasOperator = false;
        operator.setLength(0);
        decimalActive = false;
    }

    private void clear(){
        StringBuilder str = new StringBuilder(display.getText());
        if(temp.length() != 0) {
            str.deleteCharAt(str.length() - 1);
            temp.deleteCharAt(temp.length() - 1);
        }
        display.setText(str.toString());
    }
}
