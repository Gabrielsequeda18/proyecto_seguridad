package com.example.app

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth

class Main2ActivityInicio : AppCompatActivity(),
    GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
    LocationListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var nombreInicio:TextView
    private lateinit var emailInicio:TextView
    private lateinit var latInicio:TextView
    private lateinit var longInicio:TextView
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_inicio)
        auth = FirebaseAuth.getInstance()
        nombreInicio = findViewById(R.id.textViewNombreInicio)
        emailInicio = findViewById(R.id.textViewEmailInicio)
        latInicio = findViewById(R.id.textViewLatInicio)
        longInicio = findViewById(R.id.textViewLongInicio)
        locationRequest = LocationRequest()

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addConnectionCallbacks(this)
            .addApi(LocationServices.API)
            .build()


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

    private fun updateLocation(){
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = UPDATE_INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
    }

    private fun updateUI(loc: Location?) {
        if (loc != null) {
            latInicio.text  = "Latitude: ${loc.latitude}"
            longInicio.text = "Longitude: ${loc.longitude}"
        } else {
            latInicio.text  = "Latitude: (unknown)"
            longInicio.text = "Longitude: (unknown)"
        }
    }

    companion object {
        const val LOG_TAG = "Mensaje"
        const val PETITION_PERMIT_LOCATION = 101
        const val UPDATE_INTERVAL: Long = 500
        const val FASTEST_INTERVAL: Long = 500
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.
        Log.e(LOG_TAG, "Error grave al conectar con Google Play Services");
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PETITION_PERMIT_LOCATION) {
            if (grantResults.size == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) { //Permiso concedido
                val location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                updateUI(location)
            } else { //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.
                Log.e(LOG_TAG, "Permiso denegado")
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        //Conectado correctamente a Google Play Services
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PETITION_PERMIT_LOCATION
            )
        } else {
            updateLocation()
            val location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
            updateUI(location)
        }
        //startLocationUpdate()
    }

    override fun onConnectionSuspended(p0: Int) {
        //Se ha interrumpido la conexión con Google Play Services
        Log.e(LOG_TAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    override fun onLocationChanged(location: Location?) {
        updateLocation()
        updateUI(location)
    }


}
