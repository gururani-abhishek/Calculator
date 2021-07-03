package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.NumberFormatException

private const val TAG = "activity_lifecycle"
private const val ContentsOfPendingOperation = "TEXTVIEW_CONTENTS"
private const val ContentsOfOperand1 = "OPERAND1_CONTENTS"
private const val StateOfOperand1 = "StateOfOperand1"
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
    //we can also initialise displayOperation with lateinit, but then it'll be no more immutable(val)
    //private lateinit var displayOperation: TextView

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
        //neg number:
        val negSign = findViewById<Button>(R.id.BTN_NEG)

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
            try{
            val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            }catch(e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            displayOperation.text = pendingOperation

        }

        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)

        negSign.setOnClickListener { view ->
        val  value = newNumber.text.toString()
        if(value.isEmpty()) {
            newNumber.setText("-")
        } else {
            try{
                var doubleValue = value.toDouble()
                doubleValue *= -1
                newNumber.setText(doubleValue.toString())
            }catch(e: NumberFormatException) {
                newNumber.setText("")
            }
        }
        }


        }
    //operand1, 2
    private fun performOperation(value: Double, op: String) {
       if(operand1 == null) {
          operand1 = value
       } else {
           operand2 = value

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

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState: called")
        super.onSaveInstanceState(outState)
        if(operand1 != null) {
            outState.putDouble(ContentsOfOperand1, operand1!!)
            outState.putBoolean(StateOfOperand1, true)
        }
        //!! BangBang operator returns a NPE if the operand1 will be null.
        //but ? Safe Call Operator, will only proceed if the operand1 is not null.
        //so ? Safe Call Operator is a better thing to use.
        outState.putString(ContentsOfPendingOperation, displayOperation?.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState: called")
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if(savedInstanceState.getBoolean(StateOfOperand1, false)) {
            savedInstanceState.getDouble(ContentsOfOperand1)
        }else {
            null
        }

        pendingOperation = savedInstanceState.getString(ContentsOfPendingOperation).toString()
        displayOperation.text = pendingOperation

    }
}