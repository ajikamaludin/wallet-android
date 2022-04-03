package id.ajikamaludin.wallet.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.ajikamaludin.wallet.ITEM_INCOME
import id.ajikamaludin.wallet.data.Transaction
import id.ajikamaludin.wallet.databinding.ItemTransactionBinding

class TransactionListAdapter(private val onItemClicked: (Transaction) -> Unit):
    ListAdapter<Transaction, TransactionListAdapter.ItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemTransactionBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ItemViewHolder(private var binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            binding.apply {
                val amount = transaction.amount.toString()
                when(transaction.type) {
                    ITEM_INCOME -> textAmount.text = amount
                    else -> textAmount.text = "-${amount}"
                }
                textDate.text = transaction.createdAt
                textDescription.text = transaction.description
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem.createdAt == newItem.createdAt
            }
        }
    }
}