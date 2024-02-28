package com.misbah.todo.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.misbah.todo.R
import com.misbah.todo.core.data.model.Category
/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class CategoryArrayAdapter(context: Context, resource: Int, var items: List<Category>)
    : ArrayAdapter<Category>(context, resource, items) {
    
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, container: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.item_cateory_spinner, container, false)
        }
        (view?.findViewById(android.R.id.text1) as TextView).text = getItem(position)!!.name
        return view
    }
    
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.item_cateory_spinner, parent, false)
        }
        (view?.findViewById(android.R.id.text1) as TextView).text = getItem(position)!!.name
        return view
    }
}