package com.example.bookstore.filters

import android.widget.Filter
import com.example.bookstore.models.AdapterBook
import com.example.bookstore.models.AdapterBookUser
import com.example.bookstore.models.Book

class FilterBookUser: Filter {
    private var filterList: ArrayList<Book>
    private var adapterBookUser: AdapterBookUser

    constructor(filterList: ArrayList<Book>, adapterBookUser: AdapterBookUser) {
        this.filterList = filterList
        this.adapterBookUser = adapterBookUser
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        var results = FilterResults()

        if(constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().uppercase()

            val filterModel: ArrayList<Book> = ArrayList()

            for(i in 0 until filterList.size) {
                if(filterList[i].bookName.uppercase().contains(constraint)) {
                    filterModel.add(filterList[i])
                }
                if(filterList[i].id.contains(constraint)) {
                    filterModel.add(filterList[i])
                }
            }

            results.count = filterModel.size
            results.values = filterModel
        }
        else {
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {
        adapterBookUser.bookArrayList = results.values as ArrayList<Book>

        adapterBookUser.notifyDataSetChanged()
    }


}