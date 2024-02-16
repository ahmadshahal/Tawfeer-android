package com.kotlinHero.tawfeer.products.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import com.kotlinHero.tawfeer.common.utils.getUnsafeClient
import com.kotlinHero.tawfeer.databinding.ProductItemBinding
import com.kotlinHero.tawfeer.products.domain.models.Product


class ProductsAdapter(
    private val context: Context,
    private val onItemClicked: (Int) -> Unit
) :
    PagingDataAdapter<Product, ProductsAdapter.CardViewHolder>(ProductDiffCallBack()) {

    inner class CardViewHolder(val view: ProductItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ProductItemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return CardViewHolder(binding)
    }

    class ProductDiffCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val product = getItem(position) ?: return
        val view = holder.view

        view.root.setOnClickListener {
            onItemClicked(product.id)
        }

        val imageLoader = ImageLoader.Builder(context).okHttpClient { getUnsafeClient() }.build()
        view.image.load(product.thumbnail, imageLoader)

        view.category.text = product.category
        view.price.text = "$${product.price}"
        view.title.text = product.title
        view.priceWithDiscount.text = "$${product.priceAfterDiscount}"
    }
}