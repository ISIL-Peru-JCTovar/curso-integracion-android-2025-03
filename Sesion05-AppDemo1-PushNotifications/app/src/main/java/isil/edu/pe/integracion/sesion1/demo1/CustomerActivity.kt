package isil.edu.pe.integracion.sesion1.demo1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

import isil.edu.pe.integracion.sesion1.demo1.adapters.CustomerAdapter
import isil.edu.pe.integracion.sesion1.demo1.entidades.Cliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class CustomerActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Invocando el metodo padre
        super.onCreate(savedInstanceState)
        // Seteando el layout de clientes
        setContentView(R.layout.activity_customers)
        // Definiendo el Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Definiendo el RecyclerView (que es el componente tipo lista de elementos)
        recyclerView = findViewById(R.id.recyclerView)
        //recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = CustomerAdapter(emptyList())
        recyclerView.adapter = userAdapter
        // Llenar el RecyclerView desde API REST
        fetchCustomers()
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

    private fun fetchCustomers() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accessToken = "askjdhgjsadjgjasjhd654r6465ret"
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://jsonplaceholder.typicode.com/users")
                    .get()
                    //.addHeader("Authorization", "Bearer $accessToken")
                    //.method("POST", RequestBody.create("text/json".toMediaTypeOrNull(), "{ ""filt""  }"))
                    .build()
                val response = client.newCall(request).execute()
                val jsonData = response.body?.string()
                val userType = object : TypeToken<List<Cliente>>() {}.type
                val users: List<Cliente> = Gson().fromJson(jsonData, userType)
                runOnUiThread {
                    userAdapter.updateData(users)
                }
            } catch (e: Exception) {
                Log.e("UsersActivity", "Error fetching users", e)
            }
        }
    }

}
