package isil.edu.pe.integracion.sesion1.demo1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Invocando el metodo padre
        super.onCreate(savedInstanceState)
        // Seteando el layout secundario
        setContentView(R.layout.activity_second)
        // Obteniendo los controles
        val textView = findViewById<TextView>(R.id.txtSaludo)
        val receivedText = intent.getStringExtra("user_text")
        // Seteando el mensaje
        textView.text = getString(R.string.activity_second_input_message, receivedText ?: "")
    }

}