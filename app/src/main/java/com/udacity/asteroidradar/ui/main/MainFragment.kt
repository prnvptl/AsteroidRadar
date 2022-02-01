package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.AsteroidTimeFilter

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val adapter = MainAdapter(MainAdapter.OnClickListener {
            viewModel.displayAsteroidDetails(it)
        })

        binding.asteroidRecycler.setHasFixedSize(true);
        viewModel.asteroids.observe(viewLifecycleOwner, Observer<List<Asteroid>?> { asteroids ->
            adapter.submitList(asteroids)
        })

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer<Asteroid> {
            if(it != null) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsDone()
            }
        })

        binding.asteroidRecycler.adapter = adapter

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when(item.itemId) {
                R.id.show_today_menu -> AsteroidTimeFilter.TODAY
                R.id.show_weekly_menu -> AsteroidTimeFilter.WEEKLY
                else -> AsteroidTimeFilter.ALL
            }
        )
        return true
    }
}
