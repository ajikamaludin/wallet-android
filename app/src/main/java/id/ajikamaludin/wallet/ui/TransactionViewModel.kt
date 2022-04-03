package id.ajikamaludin.wallet.ui

import androidx.lifecycle.*
import id.ajikamaludin.wallet.data.Transaction
import id.ajikamaludin.wallet.database.TransactionDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TransactionViewModel(private val transactionDao: TransactionDao): ViewModel() {
    val transactions: LiveData<List<Transaction>> = transactionDao.getTransactions().asLiveData()
    val amount: LiveData<Int> = transactionDao.getTotalAmount().asLiveData()
    val expense: LiveData<Int> = transactionDao.getTotalExpense().asLiveData()
    val income: LiveData<Int> = transactionDao.getTotalIncome().asLiveData()

    private fun insertTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.insert(transaction)
        }
    }

    private fun getNewTransactionEntry(amount: String, description: String, type: Int): Transaction {
        val current = Date()

        val formatter = SimpleDateFormat("dd-M-yyyy HH:mm", Locale.US)
        val formatted = formatter.format(current)

        return Transaction(
            amount = amount.toInt(),
            description = description,
            type = type,
            createdAt = formatted
        )
    }

    fun addTransaction(amount: String, description: String, type: Int) {
        val newTransaction = getNewTransactionEntry(amount, description, type)
        insertTransaction(newTransaction)
    }

    fun isEntryValid(amount: String, description: String, type: String): Boolean {
        if(amount.isBlank() || type.isBlank()) {
            return false
        }
        return true
    }

    fun retrieveTransaction(id: Long): LiveData<Transaction> {
        return transactionDao.getTransaction(id).asLiveData()
    }

    private fun getUpdatedTransactionEntry(id: Long, amount: String, description: String, type: Int, createdAt: String): Transaction {
        return Transaction(
            id = id,
            amount = amount.toInt(),
            description = description,
            type = type,
            createdAt = createdAt
        )
    }

    private fun updateTransaction(transaction: Transaction){
        viewModelScope.launch {
            transactionDao.update(transaction)
        }
    }

    fun update(id:Long, amount: String, description: String, type: Int, createdAt: String){
        val transaction = getUpdatedTransactionEntry(id, amount, description, type, createdAt)
        updateTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.delete(transaction)
        }
    }
}

class TransactionViewModelFactory(private val transactionDao: TransactionDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(transactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}