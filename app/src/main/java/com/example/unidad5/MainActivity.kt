package com.example.unidad5

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    // Declarar las variables globalmente
    private lateinit var textTodayDate: TextView
    private lateinit var textBornDate: TextView
    private lateinit var textAge: TextView
    private lateinit var btnSelectDate: Button
    private val todayCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvFechaHoy = findViewById<TextView>(R.id.tvfechahoy)
        val tvNaciste = findViewById<TextView>(R.id.tvnaciste)
        tvNaciste.text = "Naciste..."
        val tvResultado = findViewById<TextView>(R.id.tvresultado)
        tvResultado.text= "Calcula el número de días desde tu fecha de nacimiento"
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mMonth = c [Calendar.MONTH]
        val mDay = c [Calendar.DAY_OF_MONTH]
        "Hoy es $mDay/${mMonth+1}/$mYear".also { tvFechaHoy.text = it }




        // Inicialización de las vistas
        textTodayDate = findViewById(R.id.tvfechahoy)
        textBornDate = findViewById(R.id.tvnaciste)
        textAge = findViewById(R.id.tvresultado)
        btnSelectDate = findViewById(R.id.button)

        // Mostrar la fecha de hoy
        val today = "Hoy es ${todayCalendar.get(Calendar.DAY_OF_MONTH)}/" +
                "${todayCalendar.get(Calendar.MONTH) + 1}/" +
                "${todayCalendar.get(Calendar.YEAR)}"

        textTodayDate.text = today

        // Evento para seleccionar fecha de nacimiento
        btnSelectDate.setOnClickListener {
            val initialDate = Calendar.getInstance()
            val datePickerDialog =
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val birthDate = Calendar.getInstance()
                    birthDate.set(year, month, dayOfMonth)

                    // Llamada a la función calculaDias
                    val resultado = calculaDias(
                        todayCalendar.get(Calendar.MONTH) + 1,
                        month + 1,
                        todayCalendar.get(Calendar.DAY_OF_MONTH),
                        dayOfMonth,
                        todayCalendar.get(Calendar.YEAR),
                        year
                    )

                    // Mostrar el resultado
                    val anhos = resultado[0]
                    val dias = resultado[1]
                    textBornDate.text = "Naciste el $dayOfMonth/${month + 1}/$year"
                    textAge.text = "Tienes $anhos años y $dias días"

                },
                initialDate.get(Calendar.YEAR),
                initialDate.get(Calendar.MONTH),
                initialDate.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    // Función para calcular los días y años
    private fun calculaDias(
        mesActual: Int,
        mesNacimiento: Int,
        diaActual: Int,
        diaNacimiento: Int,
        anhoActual: Int,
        anhoNacimiento: Int
    ): IntArray {
        var dias = 0
        val calculo = IntArray(2)
        var anhos = anhoActual - anhoNacimiento
        if (mesActual >= mesNacimiento) {
            for (i in mesNacimiento + 1..mesActual) dias += numeroDeDias(i, anhoActual)
            if (diaActual < diaNacimiento && mesActual == mesNacimiento) {
                anhos--
                dias = 365 + diaActual - diaNacimiento
            } else {
                dias += diaActual - diaNacimiento
            }
        } else {
            anhos--
            dias = diasRestantes(diaNacimiento, mesNacimiento, anhoActual - 1)
            val veces = 12 + mesActual - mesNacimiento - 1
            var mesVer = mesNacimiento + 1
            var anhoVer = anhoActual - 1
            for (i in 1..veces) {
                if (mesVer > 12) {
                    mesVer = 1
                    anhoVer++
                }
                dias += numeroDeDias(mesVer, anhoVer)
                mesVer++
            }
            dias += diaActual
        }
        calculo[0] = anhos
        calculo[1] = dias
        return calculo
    }

    // Función para determinar si un año es bisiesto
    private fun esBisiesto(anho: Int): Boolean {
        var bisiesto = false
        if (anho > 1582) {
            if (anho % 400 == 0 || anho % 4 == 0 && anho % 100 != 0) {
                bisiesto = true
            }
        }
        return bisiesto
    }

    // Función que devuelve el número de días en un mes
    private fun numeroDeDias(m: Int, a: Int): Int {
        return when (m) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            else -> if (esBisiesto(a)) {
                29
            } else {
                28
            }
        }
    }

    // Función para calcular los días restantes en el mes de nacimiento
    private fun diasRestantes(dia: Int, mes: Int, anho: Int): Int {
        return numeroDeDias(mes, anho) - dia
    }
}






