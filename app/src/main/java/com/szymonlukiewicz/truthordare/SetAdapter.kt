package com.szymonlukiewicz.truthordare

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.szymonlukiewicz.truthordare.SetAdapter.SetViewHolder

class SetAdapter(private val context: Context, private val setClickListener: SetClickListener) : RecyclerView.Adapter<SetViewHolder>() {
    private lateinit var setList: ArrayList<SetChoiceActivityViewModel.SetData>


    fun loadData(playersList: ArrayList<SetChoiceActivityViewModel.SetData>) {
        this.setList = playersList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val itemView = LayoutInflater.from(context)
                .inflate(R.layout.set_row_layout, parent, false)
        return SetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val set = setList[position]
        holder.nameTextView.text = set.set_name

        val daresCount = if(set.dares_list == null){
            "0"
        }else{
            set.dares_list!!.size.toString()
        }
        holder.daresCountTextVIew.text = daresCount

        val truthCount = if(set.truths_list == null){
            "0"
        }else{
            set.truths_list!!.size.toString()
        }
        holder.truthsCountTextView.text = truthCount
    }

    override fun getItemCount(): Int {
        return setList.size
    }

    inner class SetViewHolder(view: View) : ViewHolder(view), View.OnClickListener {
        val nameTextView: TextView = view.findViewById(R.id.setNameTextView)
        val truthsCountTextView: TextView = view.findViewById(R.id.truthsCountTextView)
        val daresCountTextVIew: TextView = view.findViewById(R.id.daresCountTextVIew)
        private val constraintLayoutPlayerRow: ConstraintLayout  = view.findViewById(R.id.constraintLayoutPlayerRow)
        private val imageView: ImageView = view.findViewById(R.id.xImageView)

        var setClickListener1: SetClickListener = setClickListener

        init {
            nameTextView.setOnClickListener(this)
            truthsCountTextView.setOnClickListener(this)
            daresCountTextVIew.setOnClickListener(this)
            constraintLayoutPlayerRow.setOnClickListener(this)
            imageView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            setClickListener1.onSetCLick(adapterPosition)
        }
    }

    interface SetClickListener {
        fun onSetCLick(position: Int)
    }
}