package com.example.app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View;
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var usuario: EditText
    private lateinit var clave: EditText
    private val keySecret:String = "gabriel guillermo sequed"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        usuario = findViewById(R.id.editTextUsuario)
        clave = findViewById(R.id.editTextPassword)

        buttonIniciarSesion.setOnClickListener {
            iniciar(usuario.text.toString(), String.encrypt(clave.text.toString()))
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

    private fun guardarUsuarioSharedPreferences(user: User){
        val preference: SharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val  editor: SharedPreferences.Editor = preference.edit()
        editor.putString("uid", user.UID)
        editor.putString("email", user.email)
        editor.commit()
    }

    private fun iniciar(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    val usuario = User(user!!.uid,  user!!.email)
                    guardarUsuarioSharedPreferences(usuario)
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

    private fun String.Companion.encrypt(password: String): String {
        val secretKeySpec = SecretKeySpec(keySecret.toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = password.toCharArray()
        for (i in charArray.indices) {
            iv[i] = charArray[i].toByte()
        }
        val ivParameterSPec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSPec)
        val encryptedValue = cipher.doFinal()
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }

    companion object {
        const val TAG = "mensaje log login"
    }
}
