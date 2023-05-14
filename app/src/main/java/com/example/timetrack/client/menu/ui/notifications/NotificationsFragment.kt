package com.example.timetrack.client.menu.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.timetrack.R
import com.example.timetrack.databinding.FragmentNotificationsBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var pieChart: PieChart
    lateinit var pieEntryList: MutableList<PieEntry>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.seachDate.setOnClickListener {
            showDatePickerDialog()
        }

        // chart
        pieChart = binding.chart
        pieEntryList = mutableListOf()
        setChartValues()
        setUpChart()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        pieEntryList.add(PieEntry(8f, "no registrada"))
    }

    fun mostrarActividades(minDate: Long, maxDate: Long) {
        Log.d("waka", "ejecuta")
        /*val minDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, Calendar.MAY)
            set(Calendar.DAY_OF_MONTH, 2)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val maxDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, Calendar.MAY)
            set(Calendar.DAY_OF_MONTH, 2)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis*/
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(Firebase.auth.currentUser?.uid!!)
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
                            convertMillisToHours(document.data["activityDuration"].toString().toLong()),
                            document.data["activityName"].toString()
                        )
                    )
                    // Log.d("waka", "${document.id} => ${document.data}")
                }
                if(totalJobHours > jobHours){
                    setUpChart()
                } else {
                    pieEntryList.add(
                        PieEntry(
                            convertMillisToHours((jobHours-totalJobHours).toLong()),
                            "No registrada"
                        )
                    )
                    setUpChart()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Error al obtener documentos: $exception",
                    Toast.LENGTH_LONG
                )
            }

    }

    fun showDatePickerDialog() {
        val datePicker = DatePickerDialog({ day, month, year -> onDateSelected(day, month, year) })
        datePicker.show(requireActivity().supportFragmentManager, "datePicker")
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
    fun convertMillisToHours(millis: Long): Float {
        val seconds = millis / 1000.0
        val minutes = seconds / 60.0
        val hours = minutes / 60.0
        return hours.toFloat()
    }
}