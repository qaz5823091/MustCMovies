package com.ncyucsie.mustcmovies.Fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ncyucsie.mustcmovies.MVVM.Movie
import com.ncyucsie.mustcmovies.MVVM.MovieViewModel
import com.ncyucsie.mustcmovies.databinding.AddFragmentBinding

class AddFragment : Fragment() {

    private var _binding: AddFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = AddFragmentBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        return binding.root
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            AlertDialog.Builder(requireContext())
                .setMessage("尚未儲存變更，確定要離開嗎？")
                .setTitle("提示")
                .setPositiveButton("確定") { _,_ ->
                    isEnabled = false // DON'T FORGET THIS!
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                .setNeutralButton("取消", null)
                .show()
        }
    }

    private fun createMovie() {
        val title = binding.inputTitle.text.toString()
        val rating = binding.ratingBar.rating
        val comment = binding.inputComment.text.toString()
        val data = Movie(null, title, rating, comment)

        viewModel.addMovie(data)
    }

    private fun isTitleNotEmpty(): Boolean {
        return binding.inputTitle.text.isNotEmpty()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSubmit.setOnClickListener {
            if (isTitleNotEmpty()) {
                createMovie()
                findNavController().popBackStack()
            }
            else {
                Snackbar.make(requireView(), "請填入電影名稱！", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}