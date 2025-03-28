package isil.edu.pe.integracion.sesion1.demo1

import android.Manifest
import android.os.Bundle
import android.widget.*
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var countryDropdown: AutoCompleteTextView

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
        countryDropdown = findViewById<AutoCompleteTextView>(R.id.countryDropdown)
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
        //Agregar una funcion para cargar la lista de paises (usando API SOAP)
        cargarListaPaises()
        //Token de Push en Firebase
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM Token", "Token: $token")
            } else {
                Log.w("FCM Token", "Fetching FCM registration token failed", task.exception)
            }
        }
        //Solicitar el permiso de Push Notifications
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            solicitarPermisoPushNotifications()
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
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
            R.id.menu_list_customers_1 -> {
                startActivity(Intent(this, CustomerActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cargarListaPaises() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val soapRequestBody = """
                    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
                        <soapenv:Body>
                            <ListOfCountryNamesByName xmlns="http://www.oorsprong.org/websamples.countryinfo"></ListOfCountryNamesByName>
                        </soapenv:Body>
                    </soapenv:Envelope>
                """.trimIndent()
                val request = Request.Builder()
                    .url("https://soap-service-free.mock.beeceptor.com/CountryInfoService.wso")
                    .post(RequestBody.create("text/xml".toMediaTypeOrNull(), soapRequestBody))
                    .addHeader("Content-Type", "text/xml; charset=utf-8")
                    .build()
                val response = client.newCall(request).execute()
                val xmlData = response.body?.string()
                val countryList = parseXML(xmlData)
                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_dropdown_item_1line, countryList)
                    countryDropdown.setAdapter(adapter)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseXML(xml: String?): List<String> {
        val countryList = mutableListOf<String>()
        var currentISO = ""
        var currentName = ""
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(xml?.reader())
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "m:sISOCode" -> currentISO = parser.nextText()
                        "m:sName" -> currentName = parser.nextText()
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "m:tCountryCodeAndName") {
                        //countryList.add(Country(currentISO, currentName))
                        countryList.add(currentName)
                    }
                }
            }
            eventType = parser.next()
        }
        return countryList
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
            }
        }

    private fun solicitarPermisoPushNotifications() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

}