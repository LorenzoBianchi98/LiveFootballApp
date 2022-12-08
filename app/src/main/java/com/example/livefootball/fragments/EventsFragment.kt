package com.example.livefootball.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.livefootball.*
import com.example.livefootball.adapters.EventsAdapter
import com.example.livefootball.adapters.LeaderboardAdapter
import com.example.livefootball.adapters.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_leaderboard.*
import kotlinx.android.synthetic.main.fragment_matches.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventsFragment : Fragment() {
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null

    private val url = URL_PYTHONANYWHERE + "leagues?userId="+ userId

    private var teamsList = mutableListOf<String>()
    private var pointsList = mutableListOf<String>()

    private var idListLeagues = mutableListOf<String>()
    private var nameListLegues = mutableListOf<String>()

    lateinit var optionLeague : Spinner

    private var homeTeamsList = mutableListOf<String>()
    private var awayTeamsList = mutableListOf<String>()
    private var resultsList = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var myFragment = inflater.inflate(R.layout.fragment_events, container, false)
        getLeagues()

        Handler().postDelayed({
            optionLeague = myFragment.findViewById(R.id.leagueSpinner) as Spinner
            var options = nameListLegues

            optionLeague.adapter = ArrayAdapter<String>(this.requireContext(), android.R.layout.simple_list_item_1, options)

            optionLeague.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    leagueCode = idListLeagues[p2]
                    Handler().postDelayed({
                        makeAPIRequest() }, 500)
                    Handler().postDelayed({
                        makeLBAPIRequest()}, 500)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
        } }, 1000)

        // Inflate the layout for this fragment
        viewPager = myFragment.findViewById(R.id.viewPager)
        tabLayout = myFragment.findViewById(R.id.tabLayout)
        return myFragment
    }

    //Call onActivity Create method
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViewPager(viewPager)
        tabLayout?.setupWithViewPager(viewPager)
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setUpViewPager(viewPager: ViewPager?) {
        val adapter = SectionPagerAdapter(childFragmentManager)
        adapter.addFragment(MatchesFragment(), "Matches")
        adapter.addFragment(LeaderboardFragment(), "Leaderboard")
        viewPager?.setAdapter(adapter)
    }

    companion object {
        val instance: EventsFragment
            get() = EventsFragment()
    }


    private fun getLeagues() {
        idListLeagues.clear()
        nameListLegues.clear()
        //Create request queue
        val requestQueue = Volley.newRequestQueue(context)
        //Create new String request
        GlobalScope.launch(Dispatchers.IO) {

            val stringRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        val resultArray = response.getJSONArray("leagues")
                        for (i in 0 until resultArray.length()) {
                            var jo = resultArray.getJSONObject(i)
                            var league_id = jo.getString("leagueID")
                            var league_name = jo.getString("name")
                            Log.i("favLeagues", "Result = $league_name")

                            idListLeagues.add(league_id)
                            nameListLegues.add(league_name)

                        }
                        Log.i("myLeagues", "Result = $nameListLegues")

                        if(idListLeagues.size != 0) leagueCode = idListLeagues[0]
                        else
                            Toast.makeText(context, "SELECT A LEAGUE!", Toast.LENGTH_LONG).show()


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

    private fun setUpRecyclerView() {
        rv_events_recyclerView.layoutManager = LinearLayoutManager(this.context)
        rv_events_recyclerView.adapter = EventsAdapter(homeTeamsList, awayTeamsList, resultsList)
    }

    private fun addToList(home: String, away: String, result: String) {
        homeTeamsList.add(home)
        awayTeamsList.add(away)
        resultsList.add(result)
    }

    private fun makeAPIRequest() {
        homeTeamsList.clear()
        awayTeamsList.clear()
        resultsList.clear()

        val api = Retrofit.Builder()
            .baseUrl(EVENTS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MatchesAPIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getFootballNews("/v4/competitions/$leagueCode/matches",numFixture)

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

    private fun setUpLBRecyclerView() {
        rv_lb_recyclerView.layoutManager = LinearLayoutManager(this.context)
        rv_lb_recyclerView.adapter = LeaderboardAdapter(teamsList, pointsList)
    }

    private fun addLBToList(team: String, points: Int) {
        teamsList.add(team)
        pointsList.add(points.toString())
    }

    private fun makeLBAPIRequest() {
        teamsList.clear()
        pointsList.clear()

        val api = Retrofit.Builder()
            .baseUrl(EVENTS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LeaderboardAPIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {

                val response = api.getLeaderboard(("/v4/competitions/$leagueCode/standings"))

                for (item in response.standings[0].table) {
                    Log.i("Leaderboard", "Result = $item")
                    addLBToList(item.team.shortName, item.points)
                }
                withContext(Dispatchers.Main) {
                    setUpLBRecyclerView()
                }
            } catch (e: Exception) {
                Log.e("Leaderboard", e.toString())
            }
        }

    }


}