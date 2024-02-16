package com.kotlinHero.tawfeer.products.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.databinding.FragmentProductsBinding
import com.kotlinHero.tawfeer.products.ui.adapters.ProductsAdapter
import com.kotlinHero.tawfeer.products.ui.adapters.ProductsLoadingAdapter
import com.kotlinHero.tawfeer.products.ui.viewmodels.ProductsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsFragment : Fragment() {

    private val viewModel by viewModel<ProductsViewModel>()

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productsAdapter = ProductsAdapter(requireContext()) {
            val action = ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(it)
            findNavController().navigate(action)
        }
        val adapter = productsAdapter.withLoadStateFooter(ProductsLoadingAdapter(productsAdapter))

        binding.swiperefresh.setColorSchemeResources(R.color.green)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        productsAdapter.addLoadStateListener {
            when(it.refresh) {
                is LoadState.Error -> showSnackBar(view, getString(R.string.could_not_reach_remote_server))
                else -> Unit
            }
        }

        binding.swiperefresh.setOnRefreshListener {
            productsAdapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.channel.collect {
                        when (it) {
                            is ChannelAction.ShowSnackBar -> {
                                showSnackBar(view, it.snackBarEvent.message.asString(requireContext()))
                            }
                            is ChannelAction.Navigate -> {
                                it.navigationAction(findNavController())
                            }
                            else -> Unit
                        }
                    }
                }
                launch {
                    viewModel.productsFlow.collect {
                        productsAdapter.submitData(it)
                        binding.swiperefresh.isRefreshing = false
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
        snackBar.setTextColor(foregroundColor)
        snackBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}