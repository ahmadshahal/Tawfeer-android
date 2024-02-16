package com.kotlinHero.tawfeer.auth.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.auth.ui.viewmodels.LoginViewModel
import com.kotlinHero.tawfeer.common.domain.enums.Language
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.common.utils.getLanguage
import com.kotlinHero.tawfeer.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val viewModel by viewModel<LoginViewModel>()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val font = when(requireContext().getLanguage()) {
            Language.Arabic -> R.font.cairo_font_family
            else -> R.font.saira_font_family
        }

        binding.usernameTextInputLayout.typeface =
            ResourcesCompat.getFont(requireContext(), font)
        binding.passwordTextInputLayout.typeface =
            ResourcesCompat.getFont(requireContext(), font)

        binding.loginButton.setOnClickListener {
            viewModel.login()
        }

        binding.usernameTextInput.addTextChangedListener {
            viewModel.onUsernameChange(it?.toString() ?: "")
        }

        binding.passwordTextInput.addTextChangedListener {
            viewModel.onPasswordChange(it?.toString() ?: "")
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
                    viewModel.loginStateFlow.map { it.isLoading }.collect {
                        when (it) {
                            true -> {
                                binding.loginButton.isEnabled = false
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            false -> {
                                binding.loginButton.isEnabled = true
                                binding.progressBar.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
                launch {
                    viewModel.loginStateFlow.map { it.usernameFormValueState }.collect {
                        when(it.isValid) {
                            true -> binding.usernameTextInputLayout.error = null
                            false -> binding.usernameTextInputLayout.error = it.validationMessage?.asString(requireContext()) ?: ""
                        }
                    }
                }
                launch {
                    viewModel.loginStateFlow.map { it.passwordFormValueState }.collect {
                        when(it.isValid) {
                            true -> binding.passwordTextInputLayout.error = null
                            false -> binding.passwordTextInputLayout.error = it.validationMessage?.asString(requireContext()) ?: ""
                        }
                    }
                }
                launch {
                    val usernameText = viewModel.loginStateFlow.first().usernameFormValueState.value
                    binding.usernameTextInput.setText(usernameText)
                }
                launch {
                    val passwordText = viewModel.loginStateFlow.first().passwordFormValueState.value
                    binding.passwordTextInput.setText(passwordText)
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