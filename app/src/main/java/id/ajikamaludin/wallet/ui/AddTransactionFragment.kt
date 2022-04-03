package id.ajikamaludin.wallet.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.ajikamaludin.wallet.ITEM_EXPENSE
import id.ajikamaludin.wallet.ITEM_INCOME
import id.ajikamaludin.wallet.R
import id.ajikamaludin.wallet.WalletApplication
import id.ajikamaludin.wallet.data.Transaction
import id.ajikamaludin.wallet.databinding.FragmentAddTransactionBinding


class AddTransactionFragment : Fragment() {
    private val viewModel: TransactionViewModel by activityViewModels {
        TransactionViewModelFactory(
            (activity?.application as WalletApplication).database.transactionDao()
        )
    }
    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    lateinit var transaction: Transaction
    private val navigationArgs: AddTransactionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // force select list
        val items = listOf(getString(R.string.income), getString(R.string.expense))
        val adapter = ArrayAdapter(requireContext(), R.layout.item_type, items)
        binding.itemType.let {
            it.setAdapter(adapter)
            it.setText(getString(R.string.income), false)
        }
        binding.deleteAction.visibility = View.INVISIBLE

        val id = navigationArgs.id
        if(id > 0) {
            viewModel.retrieveTransaction(id).observe(this.viewLifecycleOwner) { selectedTransaction ->
                transaction = selectedTransaction
                bind(transaction)
            }
            binding.deleteAction.visibility = View.VISIBLE
            binding.deleteAction.setOnClickListener {
                showConfirmationDialog()
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.itemAmount.text.toString(),
            binding.itemDescription.text.toString(),
            binding.itemType.text.toString()
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            val modelType = getTransactionType()
            viewModel.addTransaction(
                binding.itemAmount.text.toString(),
                binding.itemDescription.text.toString(),
                modelType
            )
            val action = AddTransactionFragmentDirections.actionAddTransactionFragmentToTransactionListFragment()
            findNavController().navigate(action)
        } else {
            binding.itemAmount.error = "Amount required"
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }

    private fun deleteItem() {
        viewModel.deleteTransaction(transaction)
        findNavController().navigateUp()
    }

    private fun bind(transaction: Transaction) {
        binding.apply {
            itemAmount.setText(transaction.amount.toInt().toString(), TextView.BufferType.SPANNABLE)
            itemDescription.setText(transaction.description, TextView.BufferType.SPANNABLE)
            when(transaction.type) {
                ITEM_INCOME -> itemType.setText(getString(R.string.income), false)
                else -> itemType.setText(getString(R.string.expense), false)
            }

            saveAction.setOnClickListener { updateItem() }
        }
    }

    private fun updateItem() {
        if (isEntryValid()) {
            val modelType = getTransactionType()
            viewModel.update(
                this.navigationArgs.id,
                binding.itemAmount.text.toString(),
                binding.itemDescription.text.toString(),
                modelType,
                transaction.createdAt
            )
            val action = AddTransactionFragmentDirections.actionAddTransactionFragmentToTransactionListFragment()
            findNavController().navigate(action)
        } else {
            binding.itemAmount.error = "Amount required"
        }
    }

    private fun getTransactionType(): Int {
        val modelType = when(binding.itemType.text.toString()) {
            getString(R.string.income) -> ITEM_INCOME
            else -> ITEM_EXPENSE
        }

        return modelType
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}