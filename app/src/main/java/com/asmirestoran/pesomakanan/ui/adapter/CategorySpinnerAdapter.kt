package com.asmirestoran.pesomakanan.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.model.Category
import java.util.*


class CategorySpinnerAdapter(context: Context, val list: List<Category>) : BaseAdapter() {

    var inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Category {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, p2: ViewGroup?): View? {
        val holder: ViewHolder
        var convertView = view;
        if (convertView == null) {
            holder = ViewHolder()
            convertView = inflater.inflate(R.layout.role_item, null)
            holder.txtTitle = convertView.findViewById(R.id.title)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.txtTitle?.text = getItem(position).name
        return convertView
    }

    private class ViewHolder {
        var txtTitle: TextView? = null
    }
}