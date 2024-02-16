package com.kotlinHero.tawfeer.products.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.common.states.FetchState
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.common.utils.SnackBarEvent
import com.kotlinHero.tawfeer.databinding.FragmentCartBinding
import com.kotlinHero.tawfeer.products.ui.adapters.CartAdapter
import com.kotlinHero.tawfeer.products.ui.viewmodels.CartViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment() {

    private val viewModel by viewModel<CartViewModel>()

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CartAdapter(
            context = requireContext(),
            products = emptyList(),
            onPlusClicked = {
                viewModel.onPlusClicked(it)
            },
            onMinusClicked = {
                viewModel.onMinusClicked(it)
            },
            onRemoveClicked = {
                viewModel.onRemoveClicked(it)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.checkoutButton.setOnClickListener {
            viewModel.checkout()
            Toast.makeText(context, getString(R.string.checked_out_successfully), Toast.LENGTH_LONG).show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.channel.collect {
                        when (it) {
                            is ChannelAction.ShowSnackBar -> {
                                when(it.snackBarEvent) {
                                    is SnackBarEvent.Confirmation -> showSnackBarWithUndo(view, it.snackBarEvent.message.asString(requireContext()))
                                    else -> showSnackBar(view, it.snackBarEvent.message.asString(requireContext()))
                                }
                            }
                            is ChannelAction.Navigate -> {
                                it.navigationAction(findNavController())
                            }
                            else -> Unit
                        }
                    }
                }
                launch {
                    viewModel.fetchStateFlow.collect {
                        when(it) {
                            is FetchState.Success<*> -> {
                                binding.successLayout.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                            }
                            is FetchState.Loading, FetchState.Initial -> {
                                binding.successLayout.visibility = View.GONE
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            else -> {
                                binding.successLayout.visibility = View.GONE
                                binding.progressBar.visibility = View.GONE
                            }
                        }
                    }
                }
                launch {
                    viewModel.cartProductsFlow.collect {
                        adapter.updateItems(it)
                    }
                }
                launch {
                    viewModel.cartProductsFlow.collect {
                        val total = it.map { productWithCart -> productWithCart.inCart * productWithCart.product.price }.sum()
                        val totalWithDiscount = it.map { productWithCart -> productWithCart.inCart * productWithCart.product.priceAfterDiscount }.sum()
                        binding.total.text = getString(R.string.total) + " " + total
                        binding.totalWithDiscount.text = getString(R.string.total_with_discount) + " " + totalWithDiscount
                    }
                }
            }
        }
    }

    private fun showSnackBar(view: View, message: String) {
        val snackBar = Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_LONG
        )
        val backgroundColor = ContextCompat.getColor(requireContext(), R.color.dark_blue)
        snackBar.setBackgroundTint(backgroundColor)
        val foregroundColor = ContextCompat.getColor(requireContext(), R.color.white)
        snackBar.setActionTextColor(foregroundColor)
        snackBar.setTextColor(foregroundColor)
        snackBar.show()
    }

    private fun showSnackBarWithUndo(view: View, message: String) {
        val snackBar = Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.undo)) { viewModel.restoreLastProduct() }
        val backgroundColor = ContextCompat.getColor(requireContext(), R.color.dark_blue)
        snackBar.setBackgroundTint(backgroundColor)
        val foregroundColor = ContextCompat.getColor(requireContext(), R.color.white)
        snackBar.setActionTextColor(foregroundColor)
        snackBar.setTextColor(foregroundColor)
        snackBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}