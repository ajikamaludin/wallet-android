package id.ajikamaludin.wallet.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import id.ajikamaludin.wallet.R
import id.ajikamaludin.wallet.WalletApplication
import id.ajikamaludin.wallet.databinding.FragmentAddTransactionBinding
import id.ajikamaludin.wallet.databinding.FragmentTransactionListBinding


class AddTransactionFragment : Fragment() {
    private val viewModel: TransactionViewModel by activityViewModels {
        TransactionViewModelFactory(
            (activity?.application as WalletApplication).database.transactionDao()
        )
    }
    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

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
        val items = listOf(getString(R.string.income), getString(R.string.expense))
        val adapter = ArrayAdapter(requireContext(), R.layout.item_type, items)
        (binding.itemType as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.saveAction.setOnClickListener {
            Snackbar.make(this, binding.itemType.text, Snackbar.LENGTH_SHORT)
        }
    }

}