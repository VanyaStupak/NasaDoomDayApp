package dev.stupak.asteroids


import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import dev.stupak.adapter.AsteroidsAdapter
import dev.stupak.asteroids.databinding.BottomSheetSortBinding
import dev.stupak.asteroids.databinding.FragmentAsteroidsBinding
import dev.stupak.platform.base.BaseFragment
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class FragmentAsteroids : BaseFragment(R.layout.fragment_asteroids) {
    private val binding by viewBinding(FragmentAsteroidsBinding::bind)
    private val viewModel: AsteroidsViewModel by viewModels()
    private lateinit var adapter: AsteroidsAdapter
    private var selectedFilter: Int = 0
    private var startDate: Date? = null
    private var endDate: Date? = null
    private val pushNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) {}

    override fun configureUi(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isPermissionGranted =
                PermissionChecker.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PermissionChecker.PERMISSION_GRANTED
            if (!isPermissionGranted) {
                pushNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        val lifecycleOwner = viewLifecycleOwner.lifecycle
        adapter =
            AsteroidsAdapter { asteroidId ->
                navigateToFlow(null, false, "asteroids://app/$asteroidId/asteroids")
            }

        adapter.addLoadStateListener { state ->
            when (state.refresh) {
                is LoadState.Loading -> {
                    binding.loader.apply {
                        visibility = View.VISIBLE
                        playAnimation()
                    }
                }

                is LoadState.NotLoading -> {
                    binding.loader.apply {
                        visibility = View.GONE
                    }
                }

                is LoadState.Error -> {
                    binding.loader.apply {
                        playAnimation()
                        visibility = View.GONE
                    }
                }
            }
        }

        binding.apply {
            rvAsteroids.layoutManager = LinearLayoutManager(requireContext())
            rvAsteroids.adapter = adapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.assetsListStateFlow.collect { asteroidList ->
                adapter.submitData(lifecycleOwner, asteroidList)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isDataLoaded.collect { isDataLoaded ->
                if (isDataLoaded) {
                    adapter.addLoadStateListener { loadState ->
                        binding.rvAsteroids.isVisible = loadState.refresh is LoadState.NotLoading
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filterStateFlow.collect { filterState ->
                binding.apply {
                    if (filterState.startDateString != null && filterState.endDateString != null) {
                        tvDate.text =
                            "${filterState.startDateString} - ${filterState.endDateString}"
                        tvClear.visibility = filterState.clearButtonVisibility
                    }
                }
            }
        }

        configureButtons()
    }

    private fun configureButtons() {
        with(binding) {
            btnFilter.setOnClickListener {
                showSortBottomSheet()
            }
            tvClear.setOnClickListener {
                viewModel.setSort(null, null, null)
                rvAsteroids.scrollToPosition(0)
                selectedFilter = 0
                tvDate.text = ""
                tvClear.visibility = View.GONE
                viewModel.setFilterState(null, null, View.GONE)
            }
        }


    }

    private fun convertStringToDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        return dateFormat.parse(dateString) ?: throw Exception()
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(isPotentiallyDangerous: Boolean?) {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val materialDateRangePicker = builder
            .setTextInputFormat(SimpleDateFormat(DATE_PATTERN, Locale.getDefault()))
            .setTheme(dev.stupak.ui.R.style.ThemeOverlay_App_MaterialCalendar)
            .build()

        materialDateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDateMillis = selection.first
            val endDateMillis = selection.second

            if (endDateMillis - startDateMillis > TimeUnit.DAYS.toMillis(7)) {
                Toast.makeText(requireContext(), SNACKBAR_TEXT,Toast.LENGTH_LONG).show()
                return@addOnPositiveButtonClickListener
            } else {

                val startCalendar = Calendar.getInstance()
                startCalendar.timeInMillis = startDateMillis

                val endCalendar = Calendar.getInstance()
                endCalendar.timeInMillis = endDateMillis

                val startDateString =
                    SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(startCalendar.time)
                startDate = convertStringToDate(startDateString)
                val endDateString =
                    SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(endCalendar.time)
                endDate = convertStringToDate(endDateString)

                viewModel.setSort(
                    convertStringToDate(startDateString),
                    convertStringToDate(endDateString),
                    isPotentiallyDangerous = isPotentiallyDangerous
                )
                binding.apply {
                    viewModel.setFilterState(startDateString, endDateString, View.VISIBLE)
                    rvAsteroids.scrollToPosition(0)
                }
            }
        }

        materialDateRangePicker.show(
            requireActivity().supportFragmentManager,
            materialDateRangePicker.toString(),
        )
    }

    private fun showSortBottomSheet() {
        val fragmentBinding = binding
        val binding = BottomSheetSortBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(binding.root)
        dialog.show()
        var isPotentiallyDangerous: Boolean? = null
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            dev.stupak.ui.R.array.filter_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilter.adapter = adapter

        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (parent?.getItemAtPosition(position).toString()) {
                    "Yes" -> {
                        Log.d("eroieriuger", "Yes = $startDate + $endDate" )
                        if (selectedFilter != 1) {
                            viewModel.setSort(startDate, endDate, true)
                        }
                        fragmentBinding.tvClear.visibility = View.VISIBLE
                        selectedFilter = 1
                        isPotentiallyDangerous = true
                    }

                    "No" -> {
                        Log.d("eroieriuger", "NO = $startDate + $endDate" )
                        if (selectedFilter != 2) {
                            viewModel.setSort(startDate, endDate, false)
                        }
                        fragmentBinding.tvClear.visibility = View.VISIBLE
                        selectedFilter = 2
                        isPotentiallyDangerous = false
                    }

                    "All" -> {
                        Log.d("eroieriuger", "All = $startDate + $endDate" )
                        if (selectedFilter != 0) {
                            viewModel.setSort(startDate, endDate, null)
                        }
                        if (fragmentBinding.tvDate.text.isNullOrEmpty()) {
                            fragmentBinding.tvClear.visibility = View.GONE
                        }
                        selectedFilter = 0
                        isPotentiallyDangerous = null
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        binding.btnDate.setOnClickListener {
            showDatePicker(isPotentiallyDangerous)
        }
        binding.spinnerFilter.setSelection(selectedFilter)
    }

    companion object {
        private const val SNACKBAR_TEXT = "You can't choose more than 7 days"
        private const val DATE_PATTERN = "yyyy-MM-dd"
    }

}