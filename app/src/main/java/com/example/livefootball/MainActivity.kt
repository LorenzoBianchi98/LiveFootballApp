package com.example.livefootball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.livefootball.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

const val BASE_URL = "https://api.currentsapi.services/"

class MainActivity : AppCompatActivity() {

    // view binding
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    // constants
    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // configure the Google Signin
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // don't worry if shows in red , will be resolved when build first time
            .requestEmail() // we only need email from google account
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        // Google SignIn Button , Click to begin Google SignIn
        binding.googleSignInBtn.setOnClickListener {
            // begin Google Signin
            Log.d(TAG, "onCreate : begin Google SignIn")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null) {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult (requestCode: Int, resultCode: Int, data: Intent? ) {
        super.onActivityResult ( requestCode, resultCode, data)
        // Result returned from Launching the Intent from GoogleSignInApi.getSignInIntent ( ... ) ;
        if ( requestCode == RC_SIGN_IN ) {
            Log.d(TAG, " onActivityResult : Google SignIn intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google SignIn success , now auth with firebase
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            }
            catch ( e : Exception ) {
                // failed Google Signin
                Log.d(TAG, " onActivityResult : $ ( e.message ) ")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount ( account : GoogleSignInAccount? ) {
            Log.d(TAG, " firebaseAuthWithGoogleAccount : begin firebase auth with google account ")
            val credential = GoogleAuthProvider.getCredential( account!!.idToken , null)
            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener { authResult ->
                    // login success
                    Log.d(TAG, " firebaseAuthwithGoogleAccount : Logged in as ")
                    // get loggedIn user
                    val firebaseUser = firebaseAuth.currentUser
                    val uid = firebaseUser!!.uid
                    val email = firebaseUser.email
                    Log.d(TAG, " firebaseAuthwithGoogleAccount: $uid")
                    Log.d(TAG, " firebaseAuthWithGoogleAccount: $email")

                    // check if user is new or existing
                    if (authResult.additionalUserInfo!!.isNewUser) {
                        // user is new - Account created
                        Log.d(TAG, " firebaseAuthWithGoogleAccount : Account created ... \n$email ")
                        Toast.makeText(
                            this@MainActivity,
                            " Account created ... \n$email ",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // existing user - LoggedIn
                        Log.d(TAG, " firebaseAuthWithGoogleAccount : Existing user ... \n$email ")
                        Toast.makeText(
                            this@MainActivity,
                            " Logged in ... \n$email ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    // start profile activity
                    startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                        // login failed
                        Log.d(TAG ," firebaseAuthwithGoogleAccount : Loggin Failed due to s ( e.message ) " )
                        Toast.makeText(this@MainActivity, " Loggin Failed due to $ { e.message ) " , Toast.LENGTH_SHORT ).show()
                }
        }

}