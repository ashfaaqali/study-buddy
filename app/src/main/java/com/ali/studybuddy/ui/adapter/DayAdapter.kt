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
        setDataToViews(subject, viewHolder)
    }

    private fun setDataToViews(subject: SubjectModel, viewHolder: ViewHolder) {
        // Item Views
        val nameTv: TextView = viewHolder.itemView.findViewById(R.id.subject_name)
        val classTimeTv: TextView = viewHolder.itemView.findViewById(R.id.subject_time)
        val currentAttendanceTv: TextView = viewHolder.itemView.findViewById(R.id.attendance_percentage)

        // Subject data
        val totalClasses = subject.presentCount + subject.absentCount + subject.cancelledCount
        val currentAttendance = if (totalClasses > 0) (subject.presentCount.toDouble() / totalClasses * 100).toInt() else 0

        nameTv.text = subject.subjectName
        currentAttendanceTv.text = currentAttendance.toString()
        classTimeTv.text = if (subject.timeOfClass != null) subject.timeOfClass.toString() else "Class time not set"

        viewHolder.itemView.setOnClickListener {
            clickListener?.onItemClickListener(subject.id, subject.day, subject.presentCount, subject.absentCount, subject.cancelledCount, totalClasses, currentAttendance)
        }
    }

    fun updateData(newSubjects: List<SubjectModel>) {
        subjectList = newSubjects
        notifyDataSetChanged()
    }

    inner class SubjectViewHolder(binding: SubjectItemBinding) : ViewHolder(binding.root)
}

interface OnItemClickListener {
    fun onItemClickListener(
        subjectId: Long,
        day: String,
        presentCount: Int,
        absentCount: Int,
        cancelledCount: Int,
        totalClasses: Int,
        currentAttendance: Int
    )
}