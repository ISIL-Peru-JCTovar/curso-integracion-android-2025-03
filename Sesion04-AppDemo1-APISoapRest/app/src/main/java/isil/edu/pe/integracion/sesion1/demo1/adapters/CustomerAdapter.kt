package isil.edu.pe.integracion.sesion1.demo1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import isil.edu.pe.integracion.sesion1.demo1.R
import isil.edu.pe.integracion.sesion1.demo1.entidades.Cliente

class CustomerAdapter(private var userList: List<Cliente>) : RecyclerView.Adapter<CustomerAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvUserEmail: TextView = itemView.findViewById(R.id.tvUserEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.tvUserName.text = user.name
        holder.tvUserEmail.text = user.email
    }

    override fun getItemCount(): Int = userList.size

    fun updateData(newUsers: List<Cliente>) {
        userList = newUsers
        notifyDataSetChanged()
    }

}

