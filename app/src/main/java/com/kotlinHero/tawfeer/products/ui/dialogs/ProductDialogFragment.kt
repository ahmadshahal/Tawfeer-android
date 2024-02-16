package com.kotlinHero.tawfeer.products.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.ImageLoader
import coil.load
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.common.utils.getUnsafeClient
import com.kotlinHero.tawfeer.databinding.DialogFragmentProductBinding
import com.kotlinHero.tawfeer.products.domain.models.Product
import com.kotlinHero.tawfeer.products.ui.viewmodels.ProductDialogViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDialogFragment(private val product: Product) : DialogFragment() {
    private val viewModel by viewModel<ProductDialogViewModel>()

    private var _binding: DialogFragmentProductBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "Product Fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.picker.minValue = 1
        binding.picker.maxValue = 10

        val imageLoader = ImageLoader.Builder(requireContext()).okHttpClient { getUnsafeClient() }.build()
        binding.image.load(product.images.firstOrNull() ?: product.thumbnail, imageLoader)
        binding.title.text = product.title

        binding.addToCart.setOnClickListener {
            val amount = binding.picker.value
            viewModel.addToCart(product, amount)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.channel.collect {
                        when (it) {
                            is ChannelAction.CleanUp -> dismiss()
                            else -> Unit
                        }
                    }
                }
                launch {
                    viewModel.cartPreferencesFlow.map { it.products[product.id] ?: 0 }.collect {
                        binding.inCart.text = "$it ${getString(R.string.in_cart)}"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}