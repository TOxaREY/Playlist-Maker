package xyz.toxarey.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.toxarey.playlistmaker.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {
    private val viewModel: SettingsFragmentViewModel by viewModel()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(
            inflater,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        viewModel.currentThemeIsDark.observe(viewLifecycleOwner) {checked ->
            binding.darkThemeSwitch.isChecked = checked
        }

        binding.darkThemeSwitch.isChecked = viewModel.getCurrentThemeIsDark()

        binding.darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }

        binding.shareAppFrameLayout.setOnClickListener {
            viewModel.shareApp()
        }

        binding.writeToSupportFrameLayout.setOnClickListener {
            viewModel.writeToSupport()
        }

        binding.termsOfUseFrameLayout.setOnClickListener {
            viewModel.termsOfUseFrame()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}