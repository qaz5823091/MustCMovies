package com.ncyucsie.mustcmovies.Fragments

import android.content.Intent
import android.content.res.Resources
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ncyucsie.mustcmovies.ItemAdapter
import com.ncyucsie.mustcmovies.MVVM.Movie
import com.ncyucsie.mustcmovies.MVVM.MovieViewModel
import com.ncyucsie.mustcmovies.R
import com.ncyucsie.mustcmovies.SharedActivity
import com.ncyucsie.mustcmovies.databinding.ListFragmentBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class ListFragment : Fragment() {

    private var _binding: ListFragmentBinding? = null
    private var displayItems = mutableListOf<Movie>()
    private lateinit var itemAdapter: ItemAdapter

    private val binding get() = _binding!!

    private val viewModel : MovieViewModel by viewModels()

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ListFragmentBinding.inflate(inflater, container, false)

        listenerInitial()

        return binding.root
    }

    private fun listenerInitial() {
        // Refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            onUpdate()

            Snackbar.make(requireView(), "已更新！", Snackbar.LENGTH_LONG).show()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        // List all movies
        if (!binding.swipeRefreshLayout.isRefreshing) {
            viewModel.getAllMovies().observe(viewLifecycleOwner) {
                displayItems = it as MutableList<Movie>
                itemAdapter = ItemAdapter(displayItems)
                binding.recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                binding.recyclerView.adapter = itemAdapter
                itemAdapter.onItemClick = { movie ->
                    val bundle = Bundle()
                    bundle.putInt("id", movie.id!!)

                    findNavController().navigate(R.id.action_FirstFragment_to_ThirdFragment, bundle)
                }
            }
        }

        // Add a new movie
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        // Export movie list
        binding.fabExport.setOnClickListener {
            val intent = Intent (requireActivity(), SharedActivity::class.java)
            requireActivity().startActivity(intent)
        }
    }

    private fun onUpdate() {
        viewModel.updateAllMovies(displayItems)
    }

    private var simpleCallback = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
        ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            var sourcePosition = viewHolder.bindingAdapterPosition
            var targetPosition = target.bindingAdapterPosition

            itemAdapter.onItemMove(sourcePosition, targetPosition)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position = viewHolder.bindingAdapterPosition

            when (direction) {
                ItemTouchHelper.LEFT -> {
                    var deletedMovie = displayItems[position]
                    displayItems.removeAt(position)
                    binding.recyclerView.adapter?.notifyItemRemoved(position)
                    binding.recyclerView.adapter?.notifyDataSetChanged()

                    Snackbar.make(view!!, "已刪除 ${deletedMovie.title}", Snackbar.LENGTH_LONG)
                        .setAction("復原") {
                            displayItems.add(position, deletedMovie)
                            binding.recyclerView.adapter?.notifyItemInserted(position)
                            binding.recyclerView.adapter?.notifyDataSetChanged()
                        }.show()
                }

                ItemTouchHelper.RIGHT -> {
                    var editedMovie = displayItems[position]
                    val bundle = Bundle()
                    bundle.putInt("id", editedMovie.id!!)
                    findNavController().navigate(R.id.action_FirstFragment_to_FourthFragment, bundle)
                }
            }
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            var builder = RecyclerViewSwipeDecorator(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            builder.setSwipeLeftActionIconId(R.drawable.swipe_delete)
            builder.setSwipeLeftBackgroundColor(resources.getColor(R.color.third))
            builder.setSwipeRightActionIconId(R.drawable.swipe_edit)
            builder.setSwipeRightBackgroundColor(ContextCompat.getColor(context!!, R.color.second))

            builder.decorate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}