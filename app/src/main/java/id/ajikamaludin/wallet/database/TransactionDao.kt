package id.ajikamaludin.wallet.database

import androidx.room.*
import id.ajikamaludin.wallet.ITEM_EXPENSE
import id.ajikamaludin.wallet.ITEM_INCOME
import id.ajikamaludin.wallet.data.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: Transaction)
    @Update
    suspend fun update(transaction: Transaction)
    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT * from transactions ORDER BY created_at DESC")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT (SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = ${ITEM_INCOME}) - (SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = ${ITEM_EXPENSE})")
    fun getTotalAmount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = $ITEM_INCOME")
    fun getTotalIncome(): Flow<Int>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = $ITEM_EXPENSE")
    fun getTotalExpense(): Flow<Int>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransaction(id: Long): Flow<Transaction>

}