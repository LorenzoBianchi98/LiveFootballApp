package com.example.livefootball.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
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


class ProfileFragment : Fragment() {

    private val urlCreateLeague = URL_PYTHONANYWHERE + "createleague"
    private val urlDeleteLeague = URL_PYTHONANYWHERE + "deleteleague"
    private val urlGetLeagues = URL_PYTHONANYWHERE + "leagues?userId="+ userId

    private var idListLeagues = mutableListOf<String>()
    private var nameListLegues = mutableListOf<String>()

    private lateinit var logout : Button

    // firebase auth
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLeagues()

        Handler().postDelayed({
            Log.i("myLeagues", "Result = $nameListLegues")


            if(nameListLegues.contains("Serie A")) {
                empty_star_sa.visibility = View.GONE
                star_sa.visibility = View.VISIBLE
            } else{
                empty_star_sa.visibility = View.VISIBLE
                star_sa.visibility = View.GONE
            }

            if(nameListLegues.contains("Premier League")) {
                empty_star_pl.visibility = View.GONE
                star_pl.visibility = View.VISIBLE
            } else {
                empty_star_pl.visibility = View.VISIBLE
                star_pl.visibility = View.GONE
            }

            if(nameListLegues.contains("Ligue 1")) {
                empty_star_l1.visibility = View.GONE
                star_l1.visibility = View.VISIBLE
            } else {
                empty_star_l1.visibility = View.VISIBLE
                star_l1.visibility = View.GONE
            }

            if(nameListLegues.contains("Bundesliga")) {
                empty_star_bl.visibility = View.GONE
                star_bl.visibility = View.VISIBLE
            } else {
                empty_star_bl.visibility = View.VISIBLE
                star_bl.visibility = View.GONE
            }

            if(nameListLegues.contains("LaLiga")) {
                empty_star_ll.visibility = View.GONE
                star_ll.visibility = View.VISIBLE
            } else {
                empty_star_ll.visibility = View.VISIBLE
                star_ll.visibility = View.GONE
            }

            if(nameListLegues.contains("Liga Portugal")) {
                empty_star_lp.visibility = View.GONE
                star_lp.visibility = View.VISIBLE
            } else {
                empty_star_lp.visibility = View.VISIBLE
                star_lp.visibility = View.GONE
            }

            empty_star_sa.setOnClickListener(View.OnClickListener {
                createLeague("SA", "Serie A")
                empty_star_sa.visibility = View.GONE
                star_sa.visibility = View.VISIBLE
            })

            star_sa.setOnClickListener(View.OnClickListener {
                deleteLeague("SA")
                star_sa.visibility = View.GONE
                empty_star_sa.visibility = View.VISIBLE
            })

            empty_star_pl.setOnClickListener(View.OnClickListener {
                createLeague("PL", "Premier League")
                empty_star_pl.visibility = View.GONE
                star_pl.visibility = View.VISIBLE
            })

            star_pl.setOnClickListener(View.OnClickListener {
                deleteLeague("PL")
                star_pl.visibility = View.GONE
                empty_star_pl.visibility = View.VISIBLE
            })

            empty_star_l1.setOnClickListener(View.OnClickListener {
                createLeague("FL1", "Ligue 1")
                empty_star_l1.visibility = View.GONE
                star_l1.visibility = View.VISIBLE
            })

            star_l1.setOnClickListener(View.OnClickListener {
                deleteLeague("FL1")
                star_l1.visibility = View.GONE
                empty_star_l1.visibility = View.VISIBLE
            })

            empty_star_bl.setOnClickListener(View.OnClickListener {
                createLeague("BL1", "Bundesliga")
                empty_star_bl.visibility = View.GONE
                star_bl.visibility = View.VISIBLE
            })

            star_bl.setOnClickListener(View.OnClickListener {
                deleteLeague("BL1")
                star_bl.visibility = View.GONE
                empty_star_bl.visibility = View.VISIBLE
            })

            empty_star_ll.setOnClickListener(View.OnClickListener {
                createLeague("PD", "LaLiga")
                empty_star_ll.visibility = View.GONE
                star_ll.visibility = View.VISIBLE
            })

            star_ll.setOnClickListener(View.OnClickListener {
                deleteLeague("PD")
                star_ll.visibility = View.GONE
                empty_star_ll.visibility = View.VISIBLE
            })

            empty_star_lp.setOnClickListener(View.OnClickListener {
                createLeague("PPL", "Liga Portugal")
                empty_star_lp.visibility = View.GONE
                star_lp.visibility = View.VISIBLE
            })

            star_lp.setOnClickListener(View.OnClickListener {
                deleteLeague("PPL")
                star_lp.visibility = View.GONE
                empty_star_lp.visibility = View.VISIBLE
            }) }, 1000)


        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        var name = firebaseUser?.displayName

        val user_name: TextView = view.findViewById(R.id.profile_name) as TextView
        user_name.text = name.toString()

        logout = view.findViewById(R.id.logoutBtn) as Button

        // handle click , logout user
        logout.setOnClickListener{
            firebaseAuth.signOut()
            val intent = Intent (getActivity(), MainActivity::class.java)
            getActivity()?.startActivity(intent)
        }
    }


    private fun createLeague(leagueID : String, leagueName : String){
        val requestQueue = Volley.newRequestQueue(context)
        GlobalScope.launch(Dispatchers.IO) {
            val leagueJSON = JSONObject()
            leagueJSON.put("leagueID", leagueID)
            leagueJSON.put("name", leagueName)
            val jsonobj = JSONObject()
            jsonobj.put("league", leagueJSON)
            jsonobj.put("userId", userId)
            val request = JsonObjectRequest(
                Request.Method.POST,urlCreateLeague,jsonobj,
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
                }

            )
            requestQueue.add(request)
        }
    }


    private fun deleteLeague(leagueID : String){
        val requestQueue = Volley.newRequestQueue(context)
        val jsonobj = JSONObject()
        jsonobj.put("leagueId", leagueID)
        jsonobj.put("userId", userId)
        GlobalScope.launch(Dispatchers.IO) {
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, urlDeleteLeague, jsonobj,
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
    }

    private fun getLeagues() {
        idListLeagues.clear()
        nameListLegues.clear()
        //Create request queue
        val requestQueue = Volley.newRequestQueue(context)
        //Create new String request
        GlobalScope.launch(Dispatchers.IO) {

            val stringRequest = JsonObjectRequest(
                Request.Method.GET, urlGetLeagues, null,
                { response ->
                    try {
                        val resultArray = response.getJSONArray("leagues")
                        for (i in 0 until resultArray.length()) {
                            var jo = resultArray.getJSONObject(i)
                            var league_id = jo.getString("leagueID")
                            var league_name = jo.getString("name")

                            idListLeagues.add(league_id)
                            nameListLegues.add(league_name)

                        }
                        Log.i("myLeagues", "Result = $nameListLegues")


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