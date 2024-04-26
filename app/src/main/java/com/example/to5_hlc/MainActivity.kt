package com.example.to5_hlc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.to5_hlc.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.SphericalUtil

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding: ActivityMainBinding
    private lateinit var map: GoogleMap

    private var polylineOptions = PolylineOptions()
    private lateinit var puntoInicial: LatLng
    private lateinit var markerOptions: MarkerOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyToolbar().show(this)
        createFragment()
    }

    private fun createFragment(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contextual, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.item_metros_cuadrados){
            calcularArea()

        } else if (item.itemId == R.id.item_perimetro){
            calcularPerimetro()

        } else if (item.itemId == R.id.item_borrar){
            borrarMarcas()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapLongClickListener { punto -> primerMarcador(punto) }
        map.setOnMapClickListener { punto -> addPolyline(punto) }
        createMarker()
    }

    private fun primerMarcador(punto: LatLng){
        val latitud = punto.latitude
        val longitud = punto.longitude

        puntoInicial = LatLng(latitud, longitud)
        map.clear()

        if(!polylineOptions.points.isEmpty()){
            polylineOptions.points.clear()
            polylineOptions.add(puntoInicial).color(ContextCompat.getColor(this, R.color.red))
            markerOptions = MarkerOptions().position(puntoInicial).title("Origen ($latitud, $longitud")

        } else {
            polylineOptions.add(puntoInicial).color(ContextCompat.getColor(this, R.color.red))
            markerOptions = MarkerOptions().position(puntoInicial).title("Origen ($latitud, $longitud")
        }

        val polyline = map.addPolyline(polylineOptions)
        polyline.startCap = RoundCap()
        map.addMarker(markerOptions)
        Toast.makeText(this, "Coordenadas ($latitud, $longitud) agregadas correctamente", Toast.LENGTH_SHORT).show()
    }

    private fun addPolyline(punto: LatLng){
        val latitud = punto.latitude
        val longitud = punto.longitude

        if(!polylineOptions.points.isEmpty()){
            polylineOptions.add(LatLng(latitud, longitud)).color(ContextCompat.getColor(this, R.color.red))
            map.addPolyline(polylineOptions)
        } else {
            Toast.makeText(this, "Utiliza una pulsación larga para indicar el punto inicial", Toast.LENGTH_SHORT).show()
        }

    }

    private fun createMarker(){
        val coordinates = LatLng(40.419173113350965,-3.705976009368897)
        val marker = MarkerOptions().position(coordinates).title("Playa")
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )
    }

    private fun calcularArea(){

        if(polylineOptions.points.size < 4){
            Toast.makeText(this, "Debes seleccionar al menos 3 puntos para calcular el área", Toast.LENGTH_SHORT).show()
        } else {
            polylineOptions.add(puntoInicial).color(ContextCompat.getColor(this, R.color.red))
            map.addPolyline(polylineOptions)
            val area = SphericalUtil.computeArea(polylineOptions.points)
            Toast.makeText(this, "Área: ${String.format("%.2f", area)} metros", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calcularPerimetro(){

        if(polylineOptions.points.size < 2){
            Toast.makeText(this, "Debes seleccionar al menos 2 puntos para calcular el perímetro", Toast.LENGTH_SHORT).show()
        } else {
            polylineOptions.add(puntoInicial).color(ContextCompat.getColor(this, R.color.red))
            map.addPolyline(polylineOptions)
            val perimetro = SphericalUtil.computeLength(polylineOptions.points) / 2
            Toast.makeText(this, "Perímetro: ${String.format("%.2f", perimetro)} metros", Toast.LENGTH_SHORT).show()
        }
    }

    private fun borrarMarcas(){
        polylineOptions.points.clear()
        map.clear()
    }
}