package isil.edu.pe.integracion.sesion1.demo1

import android.app.Activity
import android.content.Intent
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import java.io.File
import java.io.IOException

import isil.edu.pe.integracion.sesion1.demo1.adapters.ChatAdapter

class ChatActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var recorder: MediaRecorder
    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Invocando el metodo padre
        super.onCreate(savedInstanceState)
        // Seteando el layout del chat
        setContentView(R.layout.activity_chat)
        // Agregando el toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Obtener la instancia Firebase como repositorio de Streaming
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        // Definiendo los controles
        val rvChat = findViewById<RecyclerView>(R.id.rvChat)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val btnRecord = findViewById<Button>(R.id.btnRecord)
        // Obtener el chat adapter
        val currentUserId = intent.getStringExtra("user_text")
        chatAdapter = ChatAdapter(currentUserId)
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = chatAdapter
        // Programando el boton de envio
        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                etMessage.text.clear()
            }
        }
        // Programando el boton de guardar
        btnRecord.setOnClickListener {
            startRecording()
        }
        // Colocar un listener de la BD de Firestore
        db.collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    chatAdapter.setMessages(snapshots.documents)
                }
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

    private fun sendMessage(message: String) {
        /*
        val chatMessage = hashMapOf("text" to message,
                                    "userId" to FirebaseAuth.getInstance().currentUser?.uid,
                                    "timestamp" to System.currentTimeMillis())
        */
        val chatMessage = hashMapOf("text" to message,
                                    "userId" to intent.getStringExtra("user_text"),
                                    "timestamp" to System.currentTimeMillis())
        db.collection("messages").add(chatMessage)
          .addOnSuccessListener { Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show() }
          .addOnFailureListener { Toast.makeText(this, "Error al enviar", Toast.LENGTH_SHORT).show() }
    }

    private fun startRecording() {
        try {
            audioFile = File.createTempFile("audio", ".3gp", cacheDir)
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(audioFile!!.absolutePath)
                prepare()
                start()
            }
            Toast.makeText(this, "Grabando...", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Error al grabar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val fileUri: Uri? = data.data
            uploadFile(fileUri)
        }
    }

    private fun uploadFile(fileUri: Uri?) {
        if (fileUri != null) {
            val ref = storage.reference.child("uploads/${System.currentTimeMillis()}")
            ref.putFile(fileUri)
                .addOnSuccessListener { Toast.makeText(this, "Archivo enviado", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener { Toast.makeText(this, "Error al subir archivo", Toast.LENGTH_SHORT).show() }
        }
    }

}
