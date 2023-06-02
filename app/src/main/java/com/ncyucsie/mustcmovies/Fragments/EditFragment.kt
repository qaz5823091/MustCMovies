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
import com.ncyucsie.mustcmovies.databinding.EditFragmentBinding

class EditFragment : Fragment() {

    private var _binding: EditFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MovieViewModel by viewModels()
    private var id: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = EditFragmentBinding.inflate(inflater, container, false)

        id = requireArguments().getInt("id")
        viewModel.getOneMovie(id).observe(viewLifecycleOwner) {
            binding.editTitle.setText(it.title)
            binding.editRatingBar.rating = it.rating
            binding.editComment.setText(it.comment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

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

    private fun updateMovie(id: Int) {
        val title = binding.editTitle.text.toString()
        val rating = binding.editRatingBar.rating
        val comment = binding.editComment.text.toString()
        val data = Movie(id, title, rating, comment)

        viewModel.updateMovie(data)
    }

    private fun isTitleNotEmpty(): Boolean {
        return binding.editTitle.text.isNotEmpty()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEdit.setOnClickListener {
            if (isTitleNotEmpty()) {
                val bundle = Bundle()
                bundle.putInt("id", id)

                updateMovie(id)
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