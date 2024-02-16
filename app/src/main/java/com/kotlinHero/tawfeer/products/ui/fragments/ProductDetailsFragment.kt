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
import coil.ImageLoader
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.common.states.FetchState
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.common.utils.getUnsafeClient
import com.kotlinHero.tawfeer.databinding.FragmentProductDetailsBinding
import com.kotlinHero.tawfeer.products.domain.models.Product
import com.kotlinHero.tawfeer.products.ui.dialogs.ProductDialogFragment
import com.kotlinHero.tawfeer.products.ui.viewmodels.ProductDetailsViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsFragment : Fragment() {

    private val viewModel by viewModel<ProductDetailsViewModel>()

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    viewModel.productDetailsStateFlow.map { it.fetchState }.collect {
                        when(it) {
                            is FetchState.Success<*> -> {
                                inflateProduct(it.result as Product)
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
            }
        }
    }

    private fun inflateProduct(product: Product) {
        val imageLoader = ImageLoader.Builder(requireContext()).okHttpClient { getUnsafeClient() }.build()
        binding.image.load(product.images.firstOrNull() ?: product.thumbnail, imageLoader)
        binding.priceDetail.text = "$${product.price}"
        binding.titleDetail.text = product.title
        binding.descriptionDetail.text = product.description
        binding.priceAfterDiscountDetail.text = "$${product.priceAfterDiscount.toString()}"

        binding.addToCartButton.setOnClickListener {
            ProductDialogFragment(product).show(childFragmentManager, ProductDialogFragment.TAG)
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