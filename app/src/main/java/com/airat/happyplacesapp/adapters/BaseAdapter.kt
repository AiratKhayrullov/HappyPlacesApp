package com.airat.happyplacesapp.adapters
import androidx.recyclerview.widget.RecyclerView
import pl.kitek.rvswipetodelete.SwipeToEditCallback
import java.util.ArrayList


abstract class BaseAdapter<P>: RecyclerView.Adapter<BaseViewHolder<P>>() {
    protected var mDataList: MutableList<P> = ArrayList()
    private var mBaseCallback: BaseAdapterCallback<P>? = null

    var hasItems = false

    fun attachCallback(callback: BaseAdapterCallback<P>) {
        this.mBaseCallback = callback
    }


    fun detachCallback() {
        this.mBaseCallback = null
    }

    fun setList(dataList: List<P>) {
        mDataList.addAll(dataList)
        hasItems = true
        notifyDataSetChanged()
    }

    fun addItem(newItem: P) {
        mDataList.add(newItem)
        notifyItemInserted(mDataList.size - 1)
    }

    fun addItemToTop(newItem: P) {
        mDataList.add(0, newItem)
        notifyItemInserted(0)
    }

    fun updateItems(itemsList: List<P>) {
        mDataList.clear()
        setList(itemsList)
    }

    fun deleteItem(position: Int){
        mDataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int) : P {
        return mDataList[position]
    }

    override fun onBindViewHolder(holder: BaseViewHolder<P>, position: Int) {
        holder.bind(mDataList[position])

        holder.itemView.setOnClickListener {
            mBaseCallback?.onItemClick(mDataList[position], holder.itemView)
        }
    }

    override fun getItemCount(): Int {
        return mDataList.count()
    }
}