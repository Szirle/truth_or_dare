package com.szymonlukiewicz.truthordare

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.szymonlukiewicz.truthordare.SingleTextViewAdapter.SingleTextViewViewHolder

class SingleTextViewAdapter(private val context: Context, private val singleTextViewClickListener: SingleTextViewClickListener) : RecyclerView.Adapter<SingleTextViewViewHolder>() {
    private var playersList: List<String>? = null


    fun loadData(playersList: List<String>?) {
        this.playersList = playersList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleTextViewViewHolder {
        val itemView = LayoutInflater.from(context)
                .inflate(R.layout.single_text_view_row, parent, false)
        return SingleTextViewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SingleTextViewViewHolder, position: Int) {
        holder.textView.text = playersList!![position]
    }

    override fun getItemCount(): Int {
        return if (playersList == null) {
            0
        } else {
            playersList!!.size
        }
    }

    inner class SingleTextViewViewHolder(view: View) : ViewHolder(view), View.OnClickListener {
        val textView: TextView
        var singleTextViewClickListener1: SingleTextViewClickListener = singleTextViewClickListener
        override fun onClick(v: View) {
            singleTextViewClickListener1.onSingleTextViewClick(adapterPosition)
        }

        init {
            textView = view.findViewById(R.id.playerNameTextView)
            val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraintLayoutPlayerRow)
            constraintLayout.setOnClickListener(this)
            textView.setOnClickListener(this)
        }
    }

    interface SingleTextViewClickListener {
        fun onSingleTextViewClick(position: Int)
    }
}