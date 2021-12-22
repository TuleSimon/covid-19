package com.thee.horrorcorian.covid19tracker.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.thee.horrorcorian.covid19tracker.model.Regcases
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thee.horrorcorian.covid19tracker.R

import kotlinx.android.synthetic.main.row.view.*

class recyclerAdapter(context: Context, val list: ArrayList<Regcases>) : RecyclerView.Adapter<recyclerAdapter.viewHolder>() {
    var context=context
    override fun onCreateViewHolder(view: ViewGroup, p1: Int): viewHolder {
        var view= LayoutInflater.from(context).inflate(R.layout.row,view,false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(viewholder: recyclerAdapter.viewHolder, position: Int) {
       viewholder.bindItems(list.get(position))
    }

    class viewHolder(itemview:View):RecyclerView.ViewHolder(itemview) {
        var deaths=itemView.findViewById<TextView>(R.id.deathsvalue)
        var Discharge=itemView.findViewById<TextView>(R.id.dischargevalue)
        var labcases = itemView.findViewById<TextView>(R.id.labconfirmvalue)
        var onadmissioncases = itemView.findViewById<TextView>(R.id.onadmisioncasevalue)
        var state = itemView.findViewById<TextView>(R.id.stateid)


        fun bindItems(case: Regcases){
           deaths.text=case.deceased
            Discharge.text= case.recovered
            labcases.text= case.labconfirmcases
            onadmissioncases.text= case.admissioncases
            state.text=case.state

        }
    }
}
