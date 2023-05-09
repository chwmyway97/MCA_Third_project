package com.chocho.mca_project_tablet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent

class ServingAdapter(private val items: List<String>,private val context: Context) : RecyclerView.Adapter<ServingAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_serving_item, parent, false)

        //파이어베이스 어뎁터 추가
        val holder = ViewHolder(view)
        val innerLayoutManager = LinearLayoutManager(parent.context)

        val itemsFirebase = listOf("음료수", "물")


        holder.recyclerView2.layoutManager = innerLayoutManager
        holder.recyclerView2.adapter = ServingAdapterFirebase(parent.context)
        holder.recyclerView2.setHasFixedSize(true)

        val viewModel = ViewModelProvider(parent.context as ServingPage1, ViewModelProvider.AndroidViewModelFactory.getInstance((parent.context as ServingPage1).application)).get(SubViewModel::class.java)
        viewModel.fetchData().observe(parent.context as ServingPage1, Observer {
            holder.recyclerView2.adapter?.let { adapter ->
                if (adapter is ServingAdapterFirebase) {
                    adapter.setListData(it)
                    adapter.notifyDataSetChanged()
                }
            }
        })



        return ViewHolder(view)
    }

    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item
    }

    // 아이템 개수 반환
    override fun getItemCount(): Int {
        return items.size
    }

    // 뷰 홀더 클래스
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val recyclerView2: RecyclerView = itemView.findViewById(R.id.recyclerView2)

    }



}