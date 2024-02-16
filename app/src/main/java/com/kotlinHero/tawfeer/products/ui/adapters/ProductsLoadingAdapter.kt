package com.kotlinHero.tawfeer.products.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.databinding.ProductLoadingStateItemBinding

class ProductsLoadingAdapter(
    private val adapter: ProductsAdapter
) : LoadStateAdapter<ProductsLoadingAdapter.ProductLoadingStateItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        ProductLoadingStateItemViewHolder(
            binding = ProductLoadingStateItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_loading_state_item, parent, false)
            ),
            retryCallback = { adapter.retry() }
        )

    override fun onBindViewHolder(holder: ProductLoadingStateItemViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    class ProductLoadingStateItemViewHolder(
        private val binding: ProductLoadingStateItemBinding,
        private val retryCallback: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retryCallback() }
        }

        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            val messageVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
            binding.errorMsg.isVisible = messageVisible
            binding.errorMsg.text = (loadState as? LoadState.Error)?.error?.message ?: ""
        }
    }
}
