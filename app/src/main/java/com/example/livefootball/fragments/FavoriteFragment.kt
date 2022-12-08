package com.example.livefootball.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.livefootball.*
import com.example.livefootball.adapters.RecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject


class FavoriteFragment : Fragment() {

    private var idListFav = mutableListOf<String>()
    private var titlesListFav = mutableListOf<String>()
    private var descListFav = mutableListOf<String>()
    private var imagesListFav = mutableListOf<String>()
    private var linksListFav = mutableListOf<String>()

    private val url = URL_PYTHONANYWHERE + "favorites?userId="+userId
    var listStatePrefe: Parcelable? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listStatePrefe= savedInstanceState?.getParcelable("ListStatePrefe");
        getFavoriteNews()

        idListFav.clear()
        titlesListFav.clear()
        descListFav.clear()
        imagesListFav.clear()
        linksListFav.clear()

    }


    private fun getFavoriteNews() {
        //Create request queue
        val requestQueue = Volley.newRequestQueue(context)
        //Create new String request
        GlobalScope.launch(Dispatchers.IO) {

            val stringRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        val resultArray = response.getJSONArray("favorites")
                        for (i in 0 until resultArray.length()) {
                            val jo = resultArray.getJSONObject(i)
                            val news_id = jo.getString("id")
                            val news_title = jo.getString("webTitle")
                            val news_image = jo.getString("webImage")
                            val news_desc = jo.getString("webDesc")
                            val news_link = jo.getString("webUrl")

                            idListFav.add(news_id)
                            titlesListFav.add(news_title)
                            descListFav.add(news_desc)
                            imagesListFav.add(news_image)
                            linksListFav.add(news_link)

                        }
                        rv_recyclerView_fav.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = RecyclerAdapter(
                                context,
                                idListFav,
                                titlesListFav,
                                descListFav,
                                imagesListFav,
                                linksListFav,
                                true
                            )
                        }
                        rv_recyclerView_fav.getLayoutManager()
                            ?.onRestoreInstanceState(listStatePrefe);

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            )  //Method that handles error in volley
            { error: VolleyError ->
                //Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
            stringRequest.setRetryPolicy(object : RetryPolicy {
                override fun getCurrentTimeout(): Int {
                    return 9000000
                }

                override fun getCurrentRetryCount(): Int {
                    return 9000000
                }

                @Throws(VolleyError::class)
                override fun retry(error: VolleyError) {
                }
            })
            //add string request to request queue
            requestQueue.add(stringRequest)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("ListStatePrefe", rv_recyclerView?.getLayoutManager()?.onSaveInstanceState())
    }

}