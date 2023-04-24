package com.example.bookstore.filters

import android.widget.Filter
import com.example.bookstore.models.AdapterCategoryUser
import com.example.bookstore.models.Category

class FilterCategoryUser: Filter {
    private var filterList: ArrayList<Category>
    private var adapterCategoryUser: AdapterCategoryUser

    constructor(filterList: ArrayList<Category>, adapterCategoryUser: AdapterCategoryUser) {
        this.filterList = filterList
        this.adapterCategoryUser = adapterCategoryUser
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        var results = FilterResults()

        if(constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().uppercase()

            val filterModel: ArrayList<Category> = ArrayList()

            for(i in 0 until filterList.size) {
                if(filterList[i].category.uppercase().contains(constraint)) {
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

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterCategoryUser.categoryArrayList = results.values as ArrayList<Category>

        adapterCategoryUser.notifyDataSetChanged()
    }

}