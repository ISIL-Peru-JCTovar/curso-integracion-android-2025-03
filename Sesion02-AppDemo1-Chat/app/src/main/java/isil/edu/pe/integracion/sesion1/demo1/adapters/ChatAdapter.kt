package isil.edu.pe.integracion.sesion1.demo1.adapters

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot

import isil.edu.pe.integracion.sesion1.demo1.R

class ChatAdapter(userId: String?) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val messages = mutableListOf<DocumentSnapshot>()
    private val currentUserId = userId

    fun setMessages(newMessages: List<DocumentSnapshot>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val messageDoc = messages[position]
        val messageText = messageDoc.getString("text")
        val senderId = messageDoc.getString("userId")
        //val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        // Determinar si el mensaje es del usuario actual
        val isMyMessage = senderId == currentUserId
        // Obtener el mensaje
        holder.tvMessage.text = messageText
        // Mostrar el alias
        holder.tvAlias.text = senderId
        // Obtener el layout param del mensaje
        val layoutParams = holder.tvMessage.layoutParams as ViewGroup.MarginLayoutParams
        // Ajustar diseño según el usuario
        if (isMyMessage) {
            //-- Color distinto
            holder.tvMessage.setBackgroundResource(R.drawable.bg_my_message)
            //-- Margenes (moviendo a la derecha)
            layoutParams.marginStart = 100 // Mover a la derecha
            layoutParams.marginEnd = 10
            //-- Mensaje alineado a la derecha
            holder.tvMessage.gravity = Gravity.END
            (holder.tvMessage.parent as LinearLayout).gravity = Gravity.END
        } else {
            //-- Color distinto
            holder.tvMessage.setBackgroundResource(R.drawable.bg_other_message)
            // Margenes (moviendo a la izquierda)
            layoutParams.marginStart = 10
            layoutParams.marginEnd = 100
            //-- Mensaje alineado a la izquierda
            holder.tvMessage.gravity = Gravity.START
            (holder.tvMessage.parent as LinearLayout).gravity = Gravity.START
        }
        holder.tvMessage.text = messageText
    }


    override fun getItemCount(): Int = messages.size

    private fun playAudio(context: Context, audioUri: Uri) {
        try {
            val mediaPlayer = MediaPlayer().apply {
                setDataSource(context, audioUri)
                prepare()
                start()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error al reproducir audio", Toast.LENGTH_SHORT).show()
        }
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvAlias: TextView = itemView.findViewById(R.id.tvAlias)
        val ivPlayAudio: ImageView = itemView.findViewById(R.id.ivPlayAudio)
    }

}
