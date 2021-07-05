package com.airat.happyplacesapp.adapters

import android.view.View

interface BaseAdapterCallback<T> {
    fun onItemClick(model: T, view: View)
}