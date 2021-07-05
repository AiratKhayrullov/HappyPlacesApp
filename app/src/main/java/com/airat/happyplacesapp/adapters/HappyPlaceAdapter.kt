package com.airat.happyplacesapp.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airat.happyplacesapp.R
import com.airat.happyplacesapp.activities.AddHappyPlaceActivity
import com.airat.happyplacesapp.activities.MainActivity
import com.airat.happyplacesapp.models.HappyPlaceModel
import de.hdodenhof.circleimageview.CircleImageView
import java.text.FieldPosition

class HappyPlaceAdapter() : BaseAdapter<HappyPlaceModel>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HappyPlaceModel> {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_happy_place, parent, false))
    }


    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int){
        val intent = Intent(activity.baseContext, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, mDataList[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }


    class ViewHolder(itemView: View) : BaseViewHolder<HappyPlaceModel>(itemView = itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle) as TextView
        var tvDescription: TextView = itemView.findViewById(R.id.tvDescription) as TextView
        var ivPlaceImage: CircleImageView = itemView.findViewById(R.id.iv_place_image) as CircleImageView

        override fun bind(model: HappyPlaceModel) {
            tvTitle.text = model.title
            tvDescription.text = model.description
            ivPlaceImage.setImageURI((Uri.parse(model.image)))
        }
    }

}