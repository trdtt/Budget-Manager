package com.example.budgetmanager_kotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.android.synthetic.main.activity_add_transaction.amountInput
import kotlinx.android.synthetic.main.activity_add_transaction.amountLayout
import kotlinx.android.synthetic.main.activity_add_transaction.closeBtn
import kotlinx.android.synthetic.main.activity_add_transaction.labelInput
import kotlinx.android.synthetic.main.activity_add_transaction.labelLayout
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddTransactionActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        window.navigationBarColor = resources.getColor(R.color.background_gray)

        transactionRootView.setOnClickListener {
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        labelInput.addTextChangedListener {
            if (it!!.count() > 0)
                labelLayout.error = null
        }

        amountInput.addTextChangedListener {
            if (it!!.count() > 0)
                amountLayout.error = null
        }

        initButtons()

        addTransactionBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            when {
                label.isEmpty() -> {
                    labelLayout.error = "Please enter a valid label"
                }
                amount == null -> {
                    amountLayout.error = "Please enter a valid amount"
                }
                else -> {
                    val currentDate = monthYearFromDate(LocalDate.now())
                    val transaction = Transaction(0, label, -amount, currentDate)
                    //val transaction = Transaction(0, label, -amount)
                    insert(transaction)
                }
            }
        }

        closeBtn.setOnClickListener {
            finish()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.label1 -> labelInput.setText(label1.text.toString())
            R.id.label2 -> labelInput.setText(label2.text.toString())
            R.id.label3 -> labelInput.setText(label3.text.toString())
            R.id.label4 -> labelInput.setText(label4.text.toString())
            R.id.label5 -> labelInput.setText(label5.text.toString())
            R.id.label6 -> labelInput.setText(label6.text.toString())

            R.id.amount1 -> changeAmount(amount1)
            R.id.amount2 -> changeAmount(amount2)
            R.id.amount3 -> changeAmount(amount3)
            R.id.amount4 -> changeAmount(amount4)
            R.id.amount5 -> changeAmount(amount5)

            R.id.sign -> {
                var amount : Double = amountInput.text.toString().toDoubleOrNull().let { it ?: 0.0 }
                amount = -amount
                amountInput.setText(amount.toString())
            }
        }
    }

    private fun initButtons() {
        val label1 : Button = findViewById(R.id.label1)
        val label2 : Button = findViewById(R.id.label2)
        val label3 : Button = findViewById(R.id.label3)
        val label4 : Button = findViewById(R.id.label4)
        val label5 : Button = findViewById(R.id.label5)
        val label6 : Button = findViewById(R.id.label6)

        val amount1 : Button = findViewById(R.id.amount1)
        val amount2 : Button = findViewById(R.id.amount2)
        val amount3 : Button = findViewById(R.id.amount3)
        val amount4 : Button = findViewById(R.id.amount4)
        val amount5 : Button = findViewById(R.id.amount5)

        val sign : Button = findViewById(R.id.sign)

        label1.setOnClickListener(this)
        label2.setOnClickListener(this)
        label3.setOnClickListener(this)
        label4.setOnClickListener(this)
        label5.setOnClickListener(this)
        label6.setOnClickListener(this)
        amount1.setOnClickListener(this)
        amount2.setOnClickListener(this)
        amount3.setOnClickListener(this)
        amount4.setOnClickListener(this)
        amount5.setOnClickListener(this)
        sign.setOnClickListener(this)
    }

    private fun changeAmount(amount : MaterialButton) {
        val oldAmount : Double = amountInput.text.toString().toDoubleOrNull().let { it ?: 0.0 }
        val newAmount : Double = oldAmount + rmEuro(amount.text.toString())
        amountInput.setText(newAmount.toString())
    }

    private fun rmEuro(amount: String): Double {
        val noEuro = amount.replace("€", "")
        return noEuro.toDouble()
    }

    private fun insert(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "transactions"
        ).build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }
}