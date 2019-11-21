package com.example.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View;
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var usuario: EditText
    private lateinit var clave: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        usuario = findViewById(R.id.editTextUsuario)
        clave = findViewById(R.id.editTextPassword)

        buttonIniciarSesion.setOnClickListener {
            iniciar(usuario.text.toString(), clave.text.toString())
        }

        textViewRegistrar.setOnClickListener {
            val intent:Intent = Intent(this,Main2ActivityRegistro::class.java)
            startActivity(intent)
        }
    }

    private fun intefazInicio(){
        val intent:Intent = Intent(this,Main2ActivityInicio::class.java)
        startActivity(intent)
    }

    private fun iniciar(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    intefazInicio()
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    companion object {
        const val TAG = "mensaje log login"
    }
}



