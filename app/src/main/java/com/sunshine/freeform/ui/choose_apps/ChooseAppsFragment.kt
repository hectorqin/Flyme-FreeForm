package com.sunshine.freeform.ui.choose_apps

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherActivityInfo
import android.content.pm.LauncherApps
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.UserManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunshine.freeform.R
import com.sunshine.freeform.databinding.FragmentChooseAppsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Collator

class ChooseAppsFragment : Fragment() {

    private var _binding: FragmentChooseAppsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChooseAppsViewModel

    private lateinit var pm: PackageManager
    private val allAppsList = ArrayList<LauncherActivityInfo>()
    private var appsList: ArrayList<*>? = null

    private lateinit var userManager: UserManager
    private lateinit var launcherApps: LauncherApps

    private var needToUpdateView = false

    private var firstObserver = true

    private var type = TYPE_FLOATING

    private var adapter: AppsRecyclerAdapter<*>? = null

    private val filter = AppNameFilter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        type = arguments?.getInt("type", TYPE_FLOATING) ?: TYPE_FLOATING
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseAppsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this)[ChooseAppsViewModel::class.java]

        pm = requireContext().packageManager
        userManager = requireContext().getSystemService(Context.USER_SERVICE) as UserManager
        launcherApps = requireContext().getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
        viewModel.type = type

        if (type == TYPE_FLOATING) {
            viewModel.getAllApps().observe(viewLifecycleOwner) { list ->
                this.appsList = list as ArrayList<*>
                showAppsList(type)
            }
        } else {
            viewModel.getAllNotificationApps().observe(viewLifecycleOwner) { list ->
                this.appsList = list as ArrayList<*>
                showAppsList(type)
            }
        }
    }

    private fun showAppsList(type: Int) {
        if (firstObserver || needToUpdateView) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val intent = Intent(Intent.ACTION_MAIN, null)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)

                if (allAppsList.isEmpty()) {
                    userManager.userProfiles.forEach {
                        allAppsList.addAll(launcherApps.getActivityList(null, it))
                    }

                    allAppsList.sortWith { o1, o2 ->
                        Collator.getInstance().compare(
                            o1!!.applicationInfo.loadLabel(pm),
                            o2!!.applicationInfo.loadLabel(pm)
                        )
                    }
                }

                withContext(Dispatchers.Main) {
                    _binding?.let {

                        it.recyclerApps.layoutManager = LinearLayoutManager(requireContext())
                        adapter = AppsRecyclerAdapter(
                            allAppsList,
                            viewModel,
                            appsList!!,
                            type,
                            requireActivity()
                        )
                        it.recyclerApps.adapter = adapter

                        firstObserver = false
                        needToUpdateView = false
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_choose_apps, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter.filter(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.finish()
            }
            R.id.choose_all -> {
                needToUpdateView = true
                viewModel.insertAllApps(allAppsList, userManager)
            }
            R.id.choose_all_cancel -> {
                if (appsList != null && appsList!!.isNotEmpty()) {
                    needToUpdateView = true
                    viewModel.deleteAll()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class AppNameFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            var newAllAppsList =  ArrayList<LauncherActivityInfo>()

            if (constraint.isBlank()) {
                newAllAppsList = allAppsList
            } else {
                allAppsList.forEach {
                    if (it.label.contains(constraint, ignoreCase = true)) {
                        newAllAppsList.add(it)
                    }
                }
            }

            results.values = newAllAppsList
            results.count = newAllAppsList.size
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            adapter?.updateDate(results.values as ArrayList<LauncherActivityInfo>)
        }
    }

    companion object {
        const val TYPE_FLOATING = 1
        const val TYPE_NOTIFICATION = 2

        fun newInstance(type: Int): ChooseAppsFragment {
            val fragment = ChooseAppsFragment()
            val args = Bundle()
            args.putInt("type", type)
            fragment.arguments = args
            return fragment
        }
    }
}