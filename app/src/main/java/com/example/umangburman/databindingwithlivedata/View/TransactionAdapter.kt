package com.example.umangburman.databindingwithlivedata.View

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.umangburman.databindingwithlivedata.Model.Transaction
import com.example.umangburman.databindingwithlivedata.R
import kotlinx.android.synthetic.main.layout_transaction_item.view.*

class TransactionAdapter internal constructor() : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private var mValues: ArrayList<Transaction> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.bindData(item)

    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_transaction_item, parent, false)) {

        private val titleTv: TextView = itemView.itemLabelTv
        private val valueTv: TextView = itemView.itemValueTv

        fun bindData(item: Transaction) {
            titleTv.text = item.time
            valueTv.text = item.amount.toString()
        }
    }

    fun updateAll(newValues: List<Transaction>) {
        mValues.clear()
        mValues.addAll(newValues)
        notifyDataSetChanged()
    }

    fun add(newValue: Transaction) {
        mValues.add(newValue)
        notifyItemInserted(mValues.size - 1)
    }
}

