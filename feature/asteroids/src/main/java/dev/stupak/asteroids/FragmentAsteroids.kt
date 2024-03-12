package dev.stupak.asteroids


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LOGGER
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

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class FragmentAsteroids : BaseFragment(R.layout.fragment_asteroids) {
    private val binding by viewBinding(FragmentAsteroidsBinding::bind)
    private val viewModel: AsteroidsViewModel by viewModels()
    private lateinit var adapter: AsteroidsAdapter
    private var selectedFilter: Int = 0
    private var startDate: Date? = null
    private var endDate: Date? = null

    override fun configureUi(savedInstanceState: Bundle?) {
        val lifecycleOwner = viewLifecycleOwner.lifecycle
        adapter =
            AsteroidsAdapter {asteroidId ->
                navigateToFlow(null,false,"asteroids://app/$asteroidId/asteroids")
            }

        binding.apply {
            rvAsteroids.layoutManager = LinearLayoutManager(requireContext())
            rvAsteroids.adapter = adapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.assetsListStateFlow.collect { asteroidList->
                adapter.submitData(lifecycleOwner, asteroidList)
            }
        }

        lifecycleScope.launch {
            viewModel.filterStateFlow.collect { filterState ->
                binding.apply {
                    if (filterState.startEndDateString != null) {
                        tvDate.text = filterState.startEndDateString
                        tvIsDangerous.text = filterState.isDangerous
                        tvClear.visibility = filterState.clearButtonVisibility
                    }
                }
            }
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

        configureButtons()
    }

    private fun configureButtons(){
        with(binding) {
            btnFilter.setOnClickListener {
                showSortBottomSheet()
            }
            tvClear.setOnClickListener {
                viewModel.setSort(null,null, null)
                rvAsteroids.scrollToPosition(0)
                selectedFilter = 0
                startDate = null
                endDate = null
                tvDate.text = ""
                tvClear.visibility = View.GONE
                tvIsDangerous.text = ""
                viewModel.setFilterState(null,null,  View.GONE)
            }
        }
    }

    private fun convertStringToDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        return dateFormat.parse(dateString) ?: throw Exception()
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(isPotentiallyDangerous: Boolean?){
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val materialDateRangePicker = builder
            .setTextInputFormat(SimpleDateFormat(DATE_PATTERN, Locale.getDefault()))
            .setTheme(dev.stupak.ui.R.style.ThemeOverlay_App_MaterialCalendar)
            .build()
        materialDateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDateMillis = selection.first
            val endDateMillis = selection.second

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

            val isDangerous = if(isPotentiallyDangerous == null){
                DANGEROUS_All} else if(isPotentiallyDangerous == true) {
                DANGEROUS_YES} else {
                DANGEROUS_NO}

            viewModel.setSort(convertStringToDate(startDateString),convertStringToDate(endDateString), isPotentiallyDangerous = isPotentiallyDangerous)
            binding.apply {
                viewModel.setFilterState("$startDateString - $endDateString",isDangerous,  View.VISIBLE)
                rvAsteroids.scrollToPosition(0)
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
        var isPotentiallyDangerous:Boolean? = null
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(),
            dev.stupak.ui.R.array.filter_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilter.adapter = adapter

        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(parent?.getItemAtPosition(position).toString()){
                    "Yes" -> {
                        if(selectedFilter != 1) {
                            viewModel.setSort(startDate, endDate, true)
                            viewModel.setFilterState(formatDate(startDate,endDate),
                                DANGEROUS_YES,View.VISIBLE )
                        }
                        selectedFilter = 1
                        isPotentiallyDangerous = true
                    }
                    "No" -> {
                        if(selectedFilter != 2) {
                            viewModel.setSort(startDate, endDate, false)
                            viewModel.setFilterState(formatDate(startDate,endDate),
                                DANGEROUS_NO,View.VISIBLE )
                        }
                        selectedFilter = 2
                        isPotentiallyDangerous = false
                    }
                    "All" -> {
                        if(selectedFilter != 0) {
                            viewModel.setSort(startDate, endDate, null)
                        }
                        if(fragmentBinding.tvDate.text.isNullOrEmpty()){
                            fragmentBinding.tvClear.visibility = View.GONE
                        } else{
                            viewModel.setFilterState(formatDate(startDate,endDate),
                                DANGEROUS_All,View.VISIBLE )
                        }
                        selectedFilter = 0
                        isPotentiallyDangerous = null
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        binding.btnDate.setOnClickListener{
            showDatePicker(isPotentiallyDangerous)
        }
        binding.spinnerFilter.setSelection(selectedFilter)
    }
    private fun formatDate(startDate: Date?, endDate: Date?): String {
        val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        val firstDate = startDate?.let { dateFormat.format(it) }
        val secondDate = endDate?.let { dateFormat.format(it) }
        return if(startDate != null || endDate != null){"$firstDate -$secondDate"} else ""
    }

    companion object{
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private const val DANGEROUS_YES = "Potentially dangerous - Yes"
        private const val DANGEROUS_NO = "Potentially dangerous - No"
        private const val DANGEROUS_All = "Potentially dangerous - All"
    }

}