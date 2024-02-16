package com.kotlinHero.tawfeer.products.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.databinding.ProductCartItemBinding
import com.kotlinHero.tawfeer.products.domain.models.ProductWithCart
import kotlin.math.ceil

class CartAdapter(
    private val context: Context,
    private var products: List<ProductWithCart>,
    private val onPlusClicked: (ProductWithCart) -> Unit,
    private val onMinusClicked: (ProductWithCart) -> Unit,
    private val onRemoveClicked: (ProductWithCart) -> Unit,
)
    :RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ProductCartItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount() = products.size

    fun updateItems(products: List<ProductWithCart>) {
        this.products = products
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = products[position]

        holder.binding.inCart.text = product.inCart.toString() + " " + context.resources.getString(R.string.in_cart)
        holder.binding.price.text = "$${ceil(product.product.price * product.inCart)}"
        holder.binding.priceWithDiscount.text = "$${ceil(product.product.priceAfterDiscount * product.inCart)}"
        holder.binding.title.text = product.product.title
        holder.binding.discount.text = "${ceil(product.inCart * (product.product.price - product.product.priceAfterDiscount))}"
        holder.binding.delete.setOnClickListener { onRemoveClicked(product) }
        holder.binding.plus.setOnClickListener { onPlusClicked(product) }
        holder.binding.minus.setOnClickListener { onMinusClicked(product) }
    }

    inner class CartViewHolder(val binding: ProductCartItemBinding)
        :RecyclerView.ViewHolder(binding.root)

}