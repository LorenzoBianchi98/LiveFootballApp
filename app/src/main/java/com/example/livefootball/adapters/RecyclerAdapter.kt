package com.example.livefootball.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.livefootball.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class RecyclerAdapter(contextFrag: Context?, private var ids: List<String>, private var titles: List<String>,
                      private var details: List<String>,
                      private var images: List<String>,
                      private var links: List<String>,
                      private var isFav: Boolean) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var context : Context = contextFrag!!

    private val urlCreate = URL_PYTHONANYWHERE + "createfavorite"
    private val urlDelete = URL_PYTHONANYWHERE + "deletefavorite"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        val itemDetail: TextView = itemView.findViewById(R.id.tv_description)
        val itemPicture: ImageView = itemView.findViewById(R.id.tv_image)
        val news_no_pref: ImageView = itemView.findViewById(R.id.empty_star)
        val news_pref: ImageView = itemView.findViewById(R.id.star)

        init {
            itemView.setOnClickListener { v: View ->
                val position: Int = adapterPosition

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(links[position])
                startActivity(itemView.context, intent, null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {

        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]

        Glide.with(holder.itemPicture)
            .load(images[position])
            .into(holder.itemPicture)

        if(userPrefeNewsId.contains(ids[position])){
            holder.news_pref.visibility = View.VISIBLE
            holder.news_no_pref.visibility = View.GONE
        }else{
            holder.news_pref.visibility = View.GONE
            holder.news_no_pref.visibility = View.VISIBLE
        }

        if(isFav) {
            holder.news_pref.visibility = View.VISIBLE
            holder.news_no_pref.visibility = View.GONE

            holder.news_pref.setOnClickListener {
                holder.news_pref.visibility = View.GONE
                holder.news_no_pref.visibility = View.VISIBLE
                deletePrefe(position)
            }
        }
        else {
            holder.news_no_pref.setOnClickListener {
                holder.news_pref.visibility = View.VISIBLE
                holder.news_no_pref.visibility = View.GONE
                createPrefe(position)
            }

            holder.news_pref.setOnClickListener {
                holder.news_pref.visibility = View.GONE
                holder.news_no_pref.visibility = View.VISIBLE
                deletePrefe(position)
            }
        }

    }

    override fun getItemCount(): Int {
        return titles.size
    }

    private fun createPrefe(position : Int){
        val requestQueue = Volley.newRequestQueue(context)
        val newsJSON = JSONObject()
        newsJSON.put("id", ids[position])
        newsJSON.put("webTitle", titles[position])
        newsJSON.put("webImage", images[position])
        newsJSON.put("webDesc", details[position])
        newsJSON.put("webUrl", links[position])
        val jsonobj = JSONObject()
        jsonobj.put("news", newsJSON)
        jsonobj.put("userId", userId)
        GlobalScope.launch(Dispatchers.IO) {

            val request = JsonObjectRequest(
                Request.Method.POST,urlCreate,jsonobj,
                { response ->
                    // Process the json
                    try {
                        //Toast.makeText(context, "Response: $response", Toast.LENGTH_LONG).show()
                    }catch (e:Exception){
                        Toast.makeText(context, "Exception: $e", Toast.LENGTH_LONG).show()
                    }

                }, {
                    // Error in request
                    //Toast.makeText(context, "Volley error: $it", Toast.LENGTH_LONG).show()
                })
            requestQueue.add(request)
        }
        userPrefeNewsId.add(ids[position])
    }


    private fun deletePrefe(position : Int){
        val requestQueue = Volley.newRequestQueue(context)
        val jsonobj = JSONObject()
        jsonobj.put("newsId", ids[position])
        jsonobj.put("userId", userId)
        GlobalScope.launch(Dispatchers.IO) {

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, urlDelete, jsonobj,
                { response ->
                    try {
                        //Toast.makeText(context, "Response: $response", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Exception: $e", Toast.LENGTH_LONG).show()
                    }
                }) { error ->  //Toast.makeText(context, "Volley error: $it", Toast.LENGTH_LONG).show()
            }

            requestQueue.add(jsonObjectRequest)
        }
        userPrefeNewsId.remove(ids[position])
    }

}