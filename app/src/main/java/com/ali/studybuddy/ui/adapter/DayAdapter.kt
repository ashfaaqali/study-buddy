package com.ali.studybuddy.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ali.studybuddy.R
import com.ali.studybuddy.data.model.SubjectModel
import com.ali.studybuddy.databinding.SubjectItemBinding

class DayAdapter(private val context: Context, private var subjectList: List<SubjectModel>) :
    RecyclerView.Adapter<ViewHolder>() {
    var clickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = SubjectItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return SubjectViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return subjectList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val subject = subjectList[position]

        // Item Views
        val name: TextView = viewHolder.itemView.findViewById(R.id.subject_name)
        val classTime: TextView = viewHolder.itemView.findViewById(R.id.subject_time)

        name.text = subject.subjectName
        if (subject.timeOfClass != null) {
            classTime.text = subject.timeOfClass.toString()
        } else {
            classTime.text = "Class time not set"
        }

        viewHolder.itemView.setOnClickListener {
            clickListener?.onItemClickListener(subject.subjectName, subject.day)
        }

        // More....
    }

    fun updateData(newSubjects: List<SubjectModel>) {
        subjectList = newSubjects
        notifyDataSetChanged()
    }

    inner class SubjectViewHolder(binding: SubjectItemBinding) : ViewHolder(binding.root)
}

interface OnItemClickListener {
    fun onItemClickListener(subjectName: String, day: String)
}