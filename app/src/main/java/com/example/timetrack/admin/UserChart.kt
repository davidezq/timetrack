package com.example.timetrack.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.timetrack.R
import com.example.timetrack.DatePickerDialog
import com.example.timetrack.databinding.ActivityUserChartBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class UserChart : AppCompatActivity() {
    lateinit var binding: ActivityUserChartBinding
    lateinit var pieChart: PieChart
    lateinit var pieEntryList: MutableList<PieEntry>
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //actionBar
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        userId = intent.getStringExtra("id")!!

        // chart
        pieChart = binding.chart
        pieEntryList = mutableListOf()
        setChartValues()
        setUpChart()

        // events
        binding.seachDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    fun setUpChart() {
        val pieDataset = PieDataSet(pieEntryList, "")
        pieDataset.colors = ColorTemplate.PASTEL_COLORS.asList()
        pieDataset.valueTextColor = resources.getColor(R.color.white)

        val pieData = PieData(pieDataset)
        pieData.setValueTextSize(12f)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.invalidate()


    }

    fun setChartValues() {
        pieEntryList.add(PieEntry(8f, getString(R.string.untracked)))
    }

    fun showDatePickerDialog() {
        val datePicker = DatePickerDialog { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day: Int, month: Int, year: Int) {
        val minDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val maxDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        mostrarActividades(minDate, maxDate)
        binding.seachDate.setText("$year-$month-$day")
    }

    private fun mostrarActividades(minDate: Long, maxDate: Long) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .collection("activities")
            .whereGreaterThanOrEqualTo("doneAt", Timestamp(minDate / 1000, 0))
            .whereLessThanOrEqualTo("doneAt", Timestamp(maxDate / 1000, 0))
            .get()
            .addOnSuccessListener { result ->
                val jobHours = 28800000f
                var totalJobHours = 0f
                pieEntryList.clear()
                for (document in result) {
                    totalJobHours += document.data["activityDuration"].toString().toFloat()
                    pieEntryList.add(
                        PieEntry(
                            convertMillisToHours(
                                document.data["activityDuration"].toString().toLong()
                            ),
                            document.data["activityName"].toString()
                        )
                    )
                    // Log.d("waka", "${document.id} => ${document.data}")
                }
                if (totalJobHours > jobHours) {
                    setUpChart()
                } else {
                    pieEntryList.add(
                        PieEntry(
                            convertMillisToHours((jobHours - totalJobHours).toLong()),
                            getString(R.string.not_registered)
                        )
                    )
                    setUpChart()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "${getString(R.string.error_get_documents)}: $exception",
                    Toast.LENGTH_LONG
                )
            }

    }

    fun convertMillisToHours(millis: Long): Float {
        val seconds = millis / 1000.0
        val minutes = seconds / 60.0
        val hours = minutes / 60.0
        return hours.toFloat()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}