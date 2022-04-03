package id.ajikamaludin.wallet.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.ajikamaludin.wallet.WalletApplication
import id.ajikamaludin.wallet.databinding.FragmentTransactionListBinding

class TransactionListFragment: Fragment() {
    private val viewModel: TransactionViewModel by activityViewModels {
        TransactionViewModelFactory(
            (activity?.application as WalletApplication).database.transactionDao()
        )
    }
    private var _binding: FragmentTransactionListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransactionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            val action = TransactionListFragmentDirections.actionTransactionListFragmentToAddTransactionFragment()
            findNavController().navigate(action)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = TransactionListAdapter {
            val action = TransactionListFragmentDirections.actionTransactionListFragmentToAddTransactionFragment(it.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.transactions.observe(viewLifecycleOwner) { items ->
            items.let { adapter.submitList(it) }
        }
    }
}