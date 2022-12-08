package com.example.livefootball.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.livefootball.*
import com.example.livefootball.adapters.EventsAdapter
import kotlinx.android.synthetic.main.fragment_matches.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Integer.parseInt

const val EVENTS_BASE_URL = "https://api.football-data.org/"

class MatchesFragment : Fragment() {

    lateinit var option : Spinner

    private var homeTeamsList = mutableListOf<String>()
    private var awayTeamsList = mutableListOf<String>()
    private var resultsList = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matches, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        option = view.findViewById(R.id.spinner) as Spinner
        var options = arrayOf("Fixture 1","Fixture 2","Fixture 3","Fixture 4","Fixture 5","Fixture 6","Fixture 7","Fixture 8","Fixture 9","Fixture 10","Fixture 11","Fixture 12","Fixture 13","Fixture 14","Fixture 15","Fixture 16","Fixture 17","Fixture 18","Fixture 19","Fixture 20","Fixture 21","Fixture 22","Fixture 23","Fixture 24","Fixture 25","Fixture 26","Fixture 27","Fixture 28","Fixture 29","Fixture 30","Fixture 31","Fixture 32","Fixture 33","Fixture 34","Fixture 35","Fixture 36","Fixture 37","Fixture 38")

        option.adapter = ArrayAdapter<String>(this.requireContext(), android.R.layout.simple_list_item_1, options)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Handler().postDelayed({
                    makeAPIRequest((p2 + 1).toString())}, 500)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                makeAPIRequest("1")
            }
        }
    }

    private fun setUpRecyclerView() {
        rv_events_recyclerView.layoutManager = LinearLayoutManager(this.context)
        rv_events_recyclerView.adapter = EventsAdapter(homeTeamsList, awayTeamsList, resultsList)
    }

    private fun addToList(home: String, away: String, result: String) {
        homeTeamsList.add(home)
        awayTeamsList.add(away)
        resultsList.add(result)
    }

    private fun makeAPIRequest(num: String) {
        homeTeamsList.clear()
        awayTeamsList.clear()
        resultsList.clear()

        numFixture = num

        val api = Retrofit.Builder()
            .baseUrl(EVENTS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MatchesAPIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getFootballNews("/v4/competitions/$leagueCode/matches",num)

                // check if games have not yet been played
                if (response.matches[0].score.winner == null) {
                    homeTeamsList.clear()
                    awayTeamsList.clear()
                    resultsList.clear()

                    for (article in response.matches) {
                        Log.i("Events", "Result = $article")
                        val hour = article.utcDate.substring(11,13).toInt() + 1
                        val minutes = article.utcDate.substring(14,16)
                        addToList(article.homeTeam.shortName, article.awayTeam.shortName, hour.toString() + ":" + minutes)
                    }
                } else {
                    for (article in response.matches) {
                        Log.i("Events", "Result = $article")
                        val result = article.score.fullTime.home.toString() + " - " + article.score.fullTime.away.toString()
                        addToList(article.homeTeam.shortName, article.awayTeam.shortName, result)
                    }
                }
                withContext(Dispatchers.Main) {
                    setUpRecyclerView()
                }
            } catch (e: Exception) {
                Log.e("Events", e.toString())
            }
        }

    }

}