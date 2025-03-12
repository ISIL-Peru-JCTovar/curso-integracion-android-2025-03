package isil.edu.pe.integracion.sesion1.demo1

import android.os.Bundle
import android.widget.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        // Invocando el metodo padre
        super.onCreate(savedInstanceState)
        // Seteando el layout main
        setContentView(R.layout.activity_main)
        // Inicializar Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        // Definiendo un listener para el boton
        val editText = findViewById<EditText>(R.id.edtNombres)
        val button = findViewById<Button>(R.id.btnIr)
        button.setOnClickListener {
            //-- Enviando el texto hacia un Bundle (que se compartira para la Analitica)
            val usuario = editText.text.toString()
            val bundle = Bundle().apply {
                putString("user_input", usuario)
            }
            //-- Enviar el evento a Firebase Analytics
            firebaseAnalytics.logEvent("button_clicked", bundle)
            //-- Definir alias del usuario - Firebase Analytics
            //firebaseAnalytics.setUserId(usuario)
            firebaseAnalytics.setUserProperty("userId", usuario)
            //-- Definir el usuario en Firestore
            //-- Ir a la segunda pantalla - Chat
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("user_text", usuario)
            startActivity(intent)
        }
    }

}