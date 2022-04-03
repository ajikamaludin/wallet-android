package id.ajikamaludin.wallet.ui

import androidx.lifecycle.*
import id.ajikamaludin.wallet.data.Transaction
import id.ajikamaludin.wallet.database.TransactionDao

class TransactionViewModel(private val transactionDao: TransactionDao): ViewModel() {
    val transactions: LiveData<List<Transaction>> = transactionDao.getTransactions().asLiveData()
    val amount: LiveData<String> = transactionDao.getTotalAmount().asLiveData()
    val expense: LiveData<String> = transactionDao.getTotalExpense().asLiveData()
    val income: LiveData<String> = transactionDao.getTotalIncome().asLiveData()
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