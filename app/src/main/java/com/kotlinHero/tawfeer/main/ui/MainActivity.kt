package com.kotlinHero.tawfeer.main.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
    username: atuny0
    password: 9uQFF1Lh
 */

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.splashScreenVisible
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        val navBuilder = NavOptions.Builder()
        navBuilder.setEnterAnim(androidx.transition.R.anim.abc_fade_in)
            .setExitAnim(androidx.transition.R.anim.abc_fade_out)
            .setPopEnterAnim(androidx.transition.R.anim.abc_fade_in)
            .setPopExitAnim(androidx.transition.R.anim.abc_fade_out)


        binding.bottomNavigationBar.setOnItemSelectedListener {
            val currentDestinationId = navController.currentDestination?.id ?: 0
            when (it.itemId) {
                R.id.ProductsFragment -> {
                    if (currentDestinationId != R.id.ProductsFragment) {
                        navController.popBackStack()
                        navController.navigate(R.id.ProductsFragment, null, navBuilder.build())
                    }
                    true
                }
                R.id.SettingsFragment -> {
                    if (currentDestinationId != R.id.SettingsFragment) {
                        navController.popBackStack()
                        navController.navigate(R.id.SettingsFragment, null, navBuilder.build())
                    }
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.ProductsFragment, R.id.SettingsFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    binding.bottomNavigationBar.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    supportActionBar?.title = getString(R.string.app_name)
                }
                R.id.ProductDetailsFragment, R.id.CartFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    binding.bottomNavigationBar.visibility = View.GONE
                    supportActionBar?.title = ""
                    binding.toolbar.visibility = View.VISIBLE
                }
                else -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    supportActionBar?.title = ""
                    binding.bottomNavigationBar.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.channel.collect {
                        when (it) {
                            is ChannelAction.Navigate -> it.navigationAction(navController)
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.products_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                // TODO: Navigate To Search
                true
            }
            R.id.cart -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.CartFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}