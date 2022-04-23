package com.example.budgetmanager_kotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
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

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        window.navigationBarColor = resources.getColor(R.color.background_gray)

        transactionRootView.setOnClickListener{
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

    private fun monthYearFromDate(date : LocalDate): String {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter)
    }
}