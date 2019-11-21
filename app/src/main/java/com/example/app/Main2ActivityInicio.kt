package com.example.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Main2ActivityInicio : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var nombreInicio:TextView
    private lateinit var emailInicio:TextView
    private lateinit var latInicio:TextView
    private lateinit var longInicio:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_inicio)
        auth = FirebaseAuth.getInstance()
        nombreInicio = findViewById(R.id.textViewNombreInicio)
        emailInicio = findViewById(R.id.textViewEmailInicio)
        latInicio = findViewById(R.id.textViewLatInicio)
        longInicio = findViewById(R.id.textViewLongInicio)

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser?.displayName != null){
            nombreInicio.text = currentUser.displayName
        } else {
            nombreInicio.text = "Sin nombre"
        }
        emailInicio.text = currentUser!!.email
    }

    private fun capturarUsuario(){
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            /*val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
            */
        }
    }
}
