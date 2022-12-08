package com.example.livefootball

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

import android.view.View
import android.view.ViewGroup

import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import java.lang.Exception
import android.graphics.Color
import android.util.Log
import android.widget.PopupWindow

import android.view.Gravity
import android.view.View.OnTouchListener
import com.android.volley.RetryPolicy
import com.example.livefootball.fragments.EVENTS_BASE_URL
import com.example.livefootball.fragments.LeaderboardFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SensorThread(val cont:Context){

    private var mShaker: Sensor? = null

    init{
        val thread = Thread{
            try{
                mShaker = Sensor(this, cont)
                mShaker!!.setOnShakeListener(object : Sensor.OnShakeListener {
                    override fun onShake() {
                        Log.i("Sensor", "Shake detected")

                        Toast.makeText(cont, "Updating standing...", Toast.LENGTH_LONG).show() //display the response on screen
                        val fragment: Fragment = LeaderboardFragment()
                        val fragmentManager: FragmentManager = ( cont as AppCompatActivity).getSupportFragmentManager()
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.bottom_navigation, fragment)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }
                })

            } catch (e:Exception){ e.printStackTrace() }
        }

        thread.start()
    }

}
