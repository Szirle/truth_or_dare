package com.szymonlukiewicz.truthordare

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.szymonlukiewicz.truthordare.SingleTextViewWithXAdapter.SingleTextViewViewHolder

class SingleTextViewWithXAdapter(private val context: Context, private val singleTextViewWithXClickListener: SingleTextViewWithXClickListener) : RecyclerView.Adapter<SingleTextViewViewHolder>() {
    private var playersList: List<String>? = null
    fun loadData(playersList: List<String>?) {
        this.playersList = playersList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleTextViewViewHolder {
        val itemView = LayoutInflater.from(context)
                .inflate(R.layout.single_text_view_with_x_row, parent, false)
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
        val textView: TextView = view.findViewById(R.id.truthOrDareTextView)
        var singleTextViewWithXClickListener1: SingleTextViewWithXClickListener = singleTextViewWithXClickListener

        init {
            val imageView: ImageView = view.findViewById(R.id.xImageView)
            imageView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            singleTextViewWithXClickListener1.onSingleTextViewWithXClick(adapterPosition)
        }
    }

    interface SingleTextViewWithXClickListener {
        fun onSingleTextViewWithXClick(position: Int)
    }
}