package com.example.budgetmanager_kotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

class DetailActivity : AppCompatActivity() {
    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        window.navigationBarColor = resources.getColor(R.color.background_gray)

        transaction = intent.getSerializableExtra("transaction") as Transaction
        labelInput.setText(transaction.label)
        amountInput.setText((-transaction.amount).toString())

        detailRootView.setOnClickListener{
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        labelInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE

            if (it!!.count() > 0)
                labelLayout.error = null
        }

        amountInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE

            if (it!!.count() > 0)
                amountLayout.error = null
        }

        updateBtn.setOnClickListener {
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
                    val newTransaction = Transaction(transaction.id, label, -amount, transaction.date)
                    //val newTransaction = Transaction(transaction.id, label, -amount)
                    update(newTransaction)
                }
            }
        }

        closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun update(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "transactions"
        ).build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}