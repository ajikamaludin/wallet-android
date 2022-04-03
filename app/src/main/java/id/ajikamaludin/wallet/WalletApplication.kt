package id.ajikamaludin.wallet

import android.app.Application
import id.ajikamaludin.wallet.database.AppDatabase

const val ITEM_EXPENSE = 2
const val ITEM_INCOME = 1

class WalletApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}