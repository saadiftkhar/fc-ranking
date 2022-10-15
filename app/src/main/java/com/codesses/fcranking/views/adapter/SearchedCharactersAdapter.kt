/*
 *
 * Created by Saad Iftikhar on 10/11/21, 5:19 PM
 * Copyright (c) 2021. All rights reserved
 *
 */

package com.codesses.fcranking.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.codesses.fcranking.R
import com.codesses.fcranking.databinding.LayoutItemSearchCharacterBinding
import com.codesses.fcranking.interfaces.OnCharacterClick
import com.codesses.fcranking.model.Characters
import com.codesses.fcranking.views.viewholder.SearchedCharactersViewHolder

class SearchedCharactersAdapter(val mContext: Context, private val listener: OnCharacterClick) :
    ListAdapter<Characters, SearchedCharactersViewHolder>(
        object : DiffUtil.ItemCallback<Characters>() {
            override fun areItemsTheSame(old: Characters, aNew: Characters): Boolean =
                old.character_id == aNew.character_id

            override fun areContentsTheSame(old: Characters, aNew: Characters): Boolean =
                old == aNew
        }) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedCharactersViewHolder {
        val binding: LayoutItemSearchCharacterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.layout_item_search_character, parent, false
        )
        return SearchedCharactersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchedCharactersViewHolder, position: Int) {
        val userEvent = getItem(position)
        holder.bind(mContext, userEvent, listener)
    }


}