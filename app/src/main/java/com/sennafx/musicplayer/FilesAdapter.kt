package com.sennafx.musicplayer

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FilesAdapter(var fileList: ArrayList<String>) :
    RecyclerView.Adapter<FilesAdapter.MyViewHolder>() {

    var itemClic: itemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.files_holder, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.fileName.text = Uri.parse(fileList[position]).lastPathSegment
        holder.holder.setOnClickListener {
            itemClic?.onSongClickListener(position)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val holder: LinearLayout = itemView.findViewById(R.id.holder)
        val imgView: ImageView = itemView.findViewById(R.id.icon_type)
        val fileName: TextView = itemView.findViewById(R.id.file_name)
    }

    fun setClickListener(i: itemClick) {
        itemClic = i
    }

    interface itemClick {
        fun onSongClickListener(position: Int)
    }
}