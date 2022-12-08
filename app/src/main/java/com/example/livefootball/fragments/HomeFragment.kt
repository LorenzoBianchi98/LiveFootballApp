package com.example.livefootball.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.livefootball.*
import com.example.livefootball.BASE_URL
import com.example.livefootball.adapters.RecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.currentsapi.services/"

class HomeFragment : Fragment() {

    private var idList =  mutableListOf<String>()
    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idList.clear()
        titlesList.clear()
        descList.clear()
        imagesList.clear()
        linksList.clear()
        makeAPIRequest()
    }

    private fun setUpRecyclerView() {
        rv_recyclerView.layoutManager = LinearLayoutManager(this.context)
        rv_recyclerView.adapter = RecyclerAdapter(this.context, idList, titlesList, descList, imagesList, linksList, false)
    }

    private fun addToList(id: String, title: String, description: String, image: String, link: String) {
        idList.add(id)
        titlesList.add(title)
        descList.add(description)
        imagesList.add(image)
        linksList.add(link)
    }

    private fun makeAPIRequest() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getNews()

                for (article in response.news) {
                    Log.i("MainActivity", "Result = $article")
                    addToList(
                        article.id,
                        article.title,
                        article.description,
                        article.image,
                        article.url
                    )
                }
                withContext(Dispatchers.Main) {
                    setUpRecyclerView()
                }
            } catch (e: Exception) {
                Log.e("MainActivity", e.toString())
            }
        }
    }
}