package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var result: EditText //lateinit: lateinititalisation w non-nullable value
    private lateinit var newNumber: EditText
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<TextView>(R.id.operation)
        //View is a class where all the widgets are defined.
        //R.id.myName specifies a view with ID's name myName.
        //R is a class that contains ID's of all the views.
        //findViewById() finds the view by the ID it is given.
    }
    //lazy function is thread safe.

    //variables to hold operands and types of calculation
    private var operand1: Double? = null
    private var operand2: Double = 0.0
    private var pendingOperation = "="



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        result = findViewById(R.id.RSLT)
        newNumber = findViewById(R.id.newNumber)

        //buttons(0-dot)
        val button0 = findViewById<Button>(R.id.BTN0)
        val button1 = findViewById<Button>(R.id.BTN1)
        val button2 = findViewById<Button>(R.id.BTN2)
        val button3 = findViewById<Button>(R.id.BTN3)
        val button4 = findViewById<Button>(R.id.BTN4)
        val button5 = findViewById<Button>(R.id.BTN5)
        val button6 = findViewById<Button>(R.id.BTN6)
        val button7 = findViewById<Button>(R.id.BTN7)
        val button8 = findViewById<Button>(R.id.BTN8)
        val button9 = findViewById<Button>(R.id.BTN9)
        val buttonDot = findViewById<Button>(R.id.BTN_DOT)

        //operations
        val buttonEquals = findViewById<Button>(R.id.BTN_EQU)
        val buttonPlus = findViewById<Button>(R.id.BTN_ADD)
        val buttonMinus = findViewById<Button>(R.id.BTN_SUB)
        val buttonDivide = findViewById<Button>(R.id.BTN_DIV)
        val buttonMultiply = findViewById<Button>(R.id.BTN_MUL)

        val listener = View.OnClickListener { v ->
            val tap = v as Button
            newNumber.append(tap.text)
        }
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            val value = newNumber.text.toString()

            if(value.isNotEmpty()) {
                performOperation(value, op)
            }
            pendingOperation = op
            displayOperation.text = pendingOperation

        }

        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)


        }
    //operand1, 2
    private fun performOperation(value: String, op: String) {
       if(operand1 == null) {
          operand1 = value.toDouble()
       } else {
           operand2 = value.toDouble()

           if(pendingOperation == "=") {
               pendingOperation = op
           }
           when(pendingOperation) {
               "=" -> operand1 = operand2
               "*" -> operand1 = operand1!! * operand2
               "/" -> if(operand2 == 0.0) {
                   operand1 = Double.NaN
                    }else {
                   operand1 = operand1!! / operand2
                    }
               "+" -> operand1 = operand1!! + operand2
               "-" -> operand1 = operand1!! - operand2
           }
           result.setText(operand1.toString())
           newNumber.setText("")
       }
    }
}