package com.ncyucsie.mustcmovies.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ncyucsie.mustcmovies.MVVM.MovieViewModel
import com.ncyucsie.mustcmovies.databinding.ReadFragmentBinding

class ReadFragment : Fragment() {

    private var _binding: ReadFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ReadFragmentBinding.inflate(inflater, container, false)

        val id = requireArguments().getInt("id")
        viewModel.getOneMovie(id).observe(viewLifecycleOwner) {
            binding.showTitle.text = it.title
            binding.showRatingBar.rating = it.rating
            binding.showComment.text = it.comment
        }

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}