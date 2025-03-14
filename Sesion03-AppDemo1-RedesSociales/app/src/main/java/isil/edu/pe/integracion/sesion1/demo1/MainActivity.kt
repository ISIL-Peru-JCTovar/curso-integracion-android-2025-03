package isil.edu.pe.integracion.sesion1.demo1

import android.os.Bundle
import android.widget.*
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        // Invocando el metodo padre
        super.onCreate(savedInstanceState)
        // Seteando el layout main
        setContentView(R.layout.activity_main)
        // Agregando el toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_auth -> {
                startActivity(Intent(this, GoogleAuthActivity::class.java))
                true
            }
            R.id.menu_x -> {
                startActivity(Intent(this, XActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}