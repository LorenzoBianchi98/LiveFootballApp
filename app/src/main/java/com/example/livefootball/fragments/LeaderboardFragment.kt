package com.example.livefootball.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.livefootball.LeaderboardAPIRequest
import com.example.livefootball.MatchesAPIRequest
import com.example.livefootball.R
import com.example.livefootball.adapters.EventsAdapter
import com.example.livefootball.adapters.LeaderboardAdapter
import com.example.livefootball.leagueCode
import kotlinx.android.synthetic.main.fragment_leaderboard.*
import kotlinx.android.synthetic.main.fragment_matches.*
import kotlinx.android.synthetic.main.fragment_matches.rv_events_recyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LeaderboardFragment : Fragment() {

    private var teamsList = mutableListOf<String>()
    private var pointsList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeAPIRequest()
    }

    private fun setUpRecyclerView() {
        rv_lb_recyclerView.layoutManager = LinearLayoutManager(this.context)
        rv_lb_recyclerView.adapter = LeaderboardAdapter(teamsList, pointsList)
    }

    private fun addToList(team: String, points: Int) {
        teamsList.add(team)
        pointsList.add(points.toString())
    }

    private fun makeAPIRequest() {
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
                    addToList(item.team.shortName, item.points)
                }
                withContext(Dispatchers.Main) {
                    setUpRecyclerView()
                }
            } catch (e: Exception) {
                Log.e("Leaderboard", e.toString())
            }
        }

    }

}