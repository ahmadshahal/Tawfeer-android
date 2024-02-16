package com.kotlinHero.tawfeer.settings.ui

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.kotlinHero.tawfeer.R
import com.kotlinHero.tawfeer.common.domain.enums.Language
import com.kotlinHero.tawfeer.common.utils.ChannelAction
import com.kotlinHero.tawfeer.common.utils.changeLanguage
import com.kotlinHero.tawfeer.databinding.FragmentSettingsBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.are_you_sure_to_logout))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.sure)) { dialog, _ ->
                viewModel.logout()
                dialog.dismiss()
            }

        binding.logoutButton.setOnClickListener {
            alertDialogBuilder.show()
        }

        binding.languageRadioGroup.setOnCheckedChangeListener { _, id ->
            when(id) {
                R.id.radio_arabic -> {
                    viewModel.onChangeLanguage(Language.Arabic)
                    requireContext().changeLanguage(Language.Arabic.languageCode)
                }
                R.id.radio_english -> {
                    viewModel.onChangeLanguage(Language.English)
                    requireContext().changeLanguage(Language.English.languageCode)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.channel.collect {
                        when (it) {
                            is ChannelAction.ShowSnackBar -> {
                                showSnackBar(
                                    view,
                                    it.snackBarEvent.message.asString(requireContext())
                                )
                            }
                            is ChannelAction.Navigate -> {
                                it.navigationAction(findNavController())
                            }
                            else -> Unit
                        }
                    }
                }
                launch {
                    when(viewModel.languageFlow.first()) {
                        Language.Arabic -> binding.languageRadioGroup.check(R.id.radio_arabic)
                        Language.English -> binding.languageRadioGroup.check(R.id.radio_english)
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