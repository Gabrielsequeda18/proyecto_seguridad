package com.example.app

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main2_registro.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class Main2ActivityRegistro : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailRegistrar: EditText
    private lateinit var pass1: EditText
    private lateinit var pass2: EditText
    private lateinit var registrar: Button
    private val keySecret:String = "gabriel guillermo sequed"
    private lateinit var secret: SecretKeySpec
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_registro)
        auth = FirebaseAuth.getInstance()
        emailRegistrar = findViewById(R.id.editTextEmailRegistrar)
        pass1 = findViewById(R.id.editTextPassUnoRegistro)
        pass2 = findViewById(R.id.editTextPassDosRegistro)
        registrar = findViewById(R.id.buttonRegistrar)

        // Database
        database = FirebaseDatabase.getInstance()
        myRef = database.reference

        registrar.setOnClickListener {
            val clave1 = pass1.text.toString()
            val clave2 = pass2.text.toString()
            when {
                clave1.length < 6 -> {
                    Toast.makeText(baseContext, "Error la contraseñas debe tener  min 6",
                        Toast.LENGTH_SHORT).show()
                }
                clave1 != clave2 -> {
                    Toast.makeText(baseContext, "Error al escribir las contraseñas",
                        Toast.LENGTH_SHORT).show()
                }
                else -> {
                    crearCuenta(emailRegistrar.text.toString(), String.encrypt(clave1))
                }
            }
        }

        textViewLogin.setOnClickListener {
            intefazLogin()
        }
    }

    private fun intefazInicio(){
        startActivity(Intent(this, Main2ActivityInicio::class.java))
    }

    private fun intefazLogin(){
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun crearCuenta(email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val getUser = auth.currentUser
                    val user = User(getUser!!.uid, getUser.email)
                    myRef.child("user").child(user.UID!!).setValue(user)
                    intefazInicio()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }


    private fun generateKey(): SecretKey? {
        return SecretKeySpec(keySecret.toByteArray(), "AES").also { secret = it }
    }

    private fun encryptMsg(message: String, secret: SecretKey?): ByteArray? {
        var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        return cipher.doFinal(message.toByteArray(charset("UTF-8")))
    }

    private fun decryptMsg(cipherText: ByteArray?, secret: SecretKey?): String? {
        var cipher: Cipher? = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher?.init(Cipher.DECRYPT_MODE, secret)
        return String(cipher!!.doFinal(cipherText), charset("UTF-8"))
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
        const val TAG = "mensaje log"
    }
}
