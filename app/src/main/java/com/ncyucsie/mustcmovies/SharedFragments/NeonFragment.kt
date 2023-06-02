package com.ncyucsie.mustcmovies.SharedFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ncyucsie.mustcmovies.MVVM.Movie
import com.ncyucsie.mustcmovies.MVVM.MovieViewModel
import com.ncyucsie.mustcmovies.SharedItemAdapter
import com.ncyucsie.mustcmovies.databinding.NeonLayoutBinding

class NeonFragment : Fragment() {
    private var _binding: NeonLayoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedItemAdapter: SharedItemAdapter

    private val viewModel : MovieViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container:
    ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = NeonLayoutBinding.inflate(inflater, container, false)

        viewModel.getAllMovies().observe(viewLifecycleOwner) {
            sharedItemAdapter = SharedItemAdapter(it as MutableList<Movie>, "Neon")
            binding.sharedRecyclerView1.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            binding.sharedRecyclerView1.adapter = sharedItemAdapter
        }

        return binding.root
    }
}