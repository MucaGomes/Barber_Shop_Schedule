package com.app.baberagenda.view

import android.content.ContentValues
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.app.baberagenda.databinding.ActivityScheduleBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class Schedule : AppCompatActivity() {

    private lateinit var binding: ActivityScheduleBinding
    private val calendar: Calendar = Calendar.getInstance()
    private val db = Firebase.firestore
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private var data = ""
    private var hora = ""
    private var name = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val documentRef = db.collection("users").document(userId)
        documentRef.addSnapshotListener(MetadataChanges.INCLUDE) { document, e ->
            if (document != null ){
                name = document.getString("name").toString()
            }
        }

        val txtBarber1 = binding.nameBarber1.text
        val txtBarber2 = binding.nameBarber2.text

        val datePicker = binding.DatePicker
        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->

            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            var day = dayOfMonth.toString()
            var month: String

            if (dayOfMonth < 10) {
                day = "0$dayOfMonth"
            }
            if (monthOfYear < 10) {
                month = "" + (monthOfYear + 1)
            } else {
                month = (monthOfYear + 1).toString()
            }

            data = "$day / $month / $year"

        }

        binding.timePiker.setOnTimeChangedListener { _, hourOfDay, minute ->

            val minuto: String

            if (minute < 10) {
                minuto = "0$minute"
            } else {
                minuto = minute.toString()
            }

            hora = "$hourOfDay:$minuto"
        }


        binding.timePiker.setIs24HourView(true)

        binding.btsShedule.setOnClickListener {
            val barber1 = binding.barber1
            val barber2 = binding.barber2

            when {
                hora.isEmpty() -> {
                    message(it, "Preencha o horário", "#FF0000")
                }
                hora < "8:00" && hora > "19:00" -> {
                    message(
                        it,
                        "A barbearia está Fechada - horário de atendimento : 00:00 as 19:00",
                        "#FF0000"
                    )
                }
                data.isEmpty() -> {
                    message(it, "Selecione uma data!", "#FF0000")
                }
                barber1.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {

                    val date = "$data às $hora"

                    val schedule = hashMapOf(
                        "date" to date,
                        "name-barber" to txtBarber1.toString(),
                        "user-name" to name
                    )

                    db.collection("schedule").document(userId).set(schedule)
                        .addOnCompleteListener { documentReference ->
                            Log.d(
                                ContentValues.TAG,
                                "Document added with ID: ${documentReference.result}"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                        }

                    message(it, "Agendamento Realizado com sucesso", "#FF03DAC5")
                }

                barber2.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    message(it, "Agendamento Realizado com sucesso", "#FF03DAC5")
                }
                else -> {
                    message(it, "Escolha um barbeiro!", "#FF0000")
                }
            }
        }
    }

    private fun message(view: View, message: String, color: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor(color))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }
}