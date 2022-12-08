package com.example.livefootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.livefootball.fragments.ProfileFragment
import com.example.livefootball.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth

import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_profile.*

import com.example.livefootball.fragments.EventsFragment
import com.example.livefootball.fragments.FavoriteFragment


class ProfileActivity : AppCompatActivity() {

    // firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        val eventsFragment = EventsFragment()
        val profileFragment = ProfileFragment()
        val homeFragment = HomeFragment()
        val favoriteFragment = FavoriteFragment()

        bottom_navigation.setSelectedItemId(R.id.ic_home)
        makeCurrentFragment(homeFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_profile -> makeCurrentFragment(profileFragment)
                R.id.ic_events -> makeCurrentFragment(eventsFragment)
                R.id.ic_favorite -> makeCurrentFragment(favoriteFragment)

            }
            true
        }
        SensorThread(this)

    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }


    private fun checkUser() {
        // get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            // user not logged in
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            // user logged in
            var email = firebaseUser.email
            var name = firebaseUser.displayName


            userMail = email.toString()
            userId = firebaseUser.uid
            userName = name.toString()

            Toast.makeText(
                this,
                " Welcome $name",
                Toast.LENGTH_SHORT
            ).show()

        }
    }
}