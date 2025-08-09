package com.project.resqfood.presentation.login.Screens
//
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.MenuItem
//import android.window.OnBackInvokedDispatcher
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.widget.Toolbar
//import androidx.compose.material3.Snackbar
//import androidx.compose.material3.SnackbarData
//import com.google.android.material.snackbar.Snackbar
//import com.mappls.sdk.maps.Mappls
//import com.mappls.sdk.maps.MapView
//import com.mappls.sdk.maps.MapplsMap
//import com.mappls.sdk.maps.OnMapReadyCallback
//import com.mappls.sdk.maps.annotations.MarkerOptions
//import com.mappls.sdk.maps.camera.CameraPosition
//import com.mappls.sdk.maps.geometry.LatLng
//import com.mappls.sdk.services.account.MapplsAccountManager
//import com.project.resqfood.R
//import com.project.resqfood.presentation.MainActivity
//
//
//class MapScreen : AppCompatActivity(), OnMapReadyCallback {
//    private lateinit var mapView: MapView
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d("MapScreen", "onCreate")
//        MapplsAccountManager.getInstance().apply {
//            ///https://apis.mappls.com/console/#/app/project/ (head on to this website and generate the api keys to put here)
//            restAPIKey = "Your restAPI key here"
//            mapSDKKey = "Your mapSDKKey here"
//            atlasClientId = "Your atlasClientId Key here"
//            atlasClientSecret = "Your atlasClientSecret Key here"
//        }
//        Mappls.getInstance(applicationContext)
//        setContentView(R.layout.activity_map_screen2)
//
//
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//
//
//        mapView = findViewById(R.id.map_view)
//        mapView.getMapAsync(this)
//
//
//    }
//
//
//    override fun onMapReady(mapplsMap: MapplsMap) {
//        val cameraPosition = CameraPosition.Builder()
//            .target(LatLng(22.5744, 88.3629))
//            .zoom(5.0)
//            .tilt(0.0)
//            .build()
//        mapplsMap.cameraPosition = cameraPosition
//
//
//        val listLatLng = listOf(
//            LatLng(22.5677, 88.3703), // Hope Foundation Orphanage
//            LatLng(22.5575, 88.3527), // All Bengal Women's Union
//            LatLng(22.5783, 88.3635), // Don Bosco Ashalayam
//            LatLng(22.5626, 88.3645), // Mother Teresa's Missionaries of Charity
//            LatLng(22.5541, 88.3578), // Asha Niketan Orphanage
//            LatLng(22.5563, 88.3415), // Anandan Orphanage
//            LatLng(22.5317, 88.3296), // Nav Jeevan Orphanage
//            LatLng(22.5751, 88.3633), // Shishu Bhavan
//            LatLng(22.5823, 88.4174), // SOS Children's Village
//            LatLng(22.5674, 88.3693), // Loretto Day School Sealdah
//            LatLng(22.4563, 88.3015), // Child In Need Institute (CINI)
//            LatLng(22.5143, 88.4037)  // Ashray Akruti
//        )
//
//
//        val titles = listOf(
//            "Hope Foundation Orphanage",
//            "All Bengal Women's Union",
//            "Don Bosco Ashalayam",
//            "Mother Teresa's Missionaries of Charity",
//            "Asha Niketan Orphanage",
//            "Anandan Orphanage",
//            "Nav Jeevan Orphanage",
//            "Shishu Bhavan",
//            "SOS Children's Village",
//            "Loretto Day School Sealdah",
//            "Child In Need Institute (CINI)",
//            "Ashray Akruti"
//        )
//
//
//        val snippets = listOf(
//            "Hope Foundation Orphanage",
//            "All Bengal Women's Union",
//            "Don Bosco Ashalayam",
//            "Mother Teresa's Missionaries of Charity",
//            "Asha Niketan Orphanage",
//            "Anandan Orphanage",
//            "Nav Jeevan Orphanage",
//            "Shishu Bhavan",
//            "SOS Children's Village",
//            "Loretto Day School Sealdah",
//            "Child In Need Institute (CINI)",
//            "Ashray Akruti"
//        )
//
//
//        val markerSet = MarkerSet(
//            latLngList = listLatLng,
//            title = titles,
//            snippet = snippets
//        )
//
//
//        for (i in markerSet.latLngList.indices) {
//            val markerOptions = MarkerOptions()
//                .position(markerSet.latLngList[i])
//                .title(markerSet.title[i])
//                .snippet(markerSet.snippet[i])
//            mapplsMap.addMarker(markerOptions)
//        }
//    }
//
//
//    // This is for handling the back button press
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
//                val intent = Intent(this, MainActivity::class.java).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                }
//                startActivity(intent)
//                finish()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//
//
//
//    // Lifecycle methods to manage MapView
//    override fun onStart() {
//        super.onStart()
//        mapView.onStart()
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        mapView.onResume()
//    }
//
//
//    override fun onPause() {
//        super.onPause()
//        mapView.onPause()
//    }
//
//
//    override fun onStop() {
//        super.onStop()
//        mapView.onStop()
//        finish()
//    }
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mapView.onDestroy()
//        finish()
//    }
//
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        mapView.onLowMemory()
//    }
//
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        mapView.onSaveInstanceState(outState)
//    }
//
//
//    override fun onMapError(p0: Int, p1: String?) {
//        Snackbar.make(findViewById(R.id.map_view), "Error: $p1", Snackbar.LENGTH_SHORT).show()
//    }
//}
//
//
///**
//Populate the data using the below data class
// **/
//data class MarkerSet(
//    var latLngList: List<LatLng> = ArrayList(),
//    var title: List<String> = ArrayList(),
//    var snippet: List<String> = ArrayList()
//)
//
