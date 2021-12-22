package com.thee.horrorcorian.covid19tracker.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.thee.horrorcorian.covid19tracker.R
import com.thee.horrorcorian.covid19tracker.data.dbHandler
import com.thee.horrorcorian.covid19tracker.data.jsonobj
import com.thee.horrorcorian.covid19tracker.data.maincaselist
import com.thee.horrorcorian.covid19tracker.model.Regcases
import com.thee.horrorcorian.covid19tracker.model.cases
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class splashActivity : Activity() {



    var volleyRequest: RequestQueue?=null
    val link="https://api.apify.com/v2/key-value-stores/Eb694wt67UxjdSGbc/records/LATEST?disableRedirect=true"
    val link2 = "https://api.apify.com/v2/datasets/ccY329O0ng68poTiX/items?format=json&clean=1"
    var date=Calendar.getInstance()
    var list:ArrayList<cases>?=null
    var db: dbHandler?=null
    var prevcase: cases?=null
    var responeded=false
    var dialogbuilder: AlertDialog.Builder ?=null
    var dialog:AlertDialog ?=null
    var caselist:ArrayList<Regcases> ?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        caselist= ArrayList()
        db= dbHandler(this)
        list=db?.readCase()
        var case=cases()
        case.tested="18536"
        case.infected= "2558"
        case.recovered= "400"
        case.deceased= "87"
        case.activecases= "2071"
        case.Date="Test Date, 2020"
        db?.insertCase(case)
       // db?.deleteCase("May 6, 2020")


        volleyRequest= Volley.newRequestQueue(this)
        connect()

        var trials=0

        volleyRequest ?.addRequestFinishedListener<String> {
            if(responeded){
                Toast.makeText(this,"Sucessful",Toast.LENGTH_LONG).show()
                var intent: Intent = Intent(this, MainActivity()::class.java)
                startActivity(intent)
                finish()
            }
            else{
                if(trials<6){
                //getString(link)

                    getJsonObject(link)

                volleyRequest?.start()
                trials+=1
                }
                else{
                    volleyRequest?.stop()
                    showDialog()
                    trials=0
                }

            }
        }

    }


    fun getJsonObject(url:String){
        var jsonObjectreq= JsonObjectRequest(
            Request.Method.GET,url,
            Response.Listener {
                    response: JSONObject ->
                try{
                    jsonobj=response
                    var case= cases()
                    case.Date=formatDate(date)
                    case.infected=response.getString("infected")
                    case.tested= response.getString("tested")
                    case.recovered= response.getString("recovered")
                    case.deceased= response.getString("deceased")
                    case.activecases=response.getString("activeCases")

                    db?.insertCase(case)

                    val regionarray=response.getJSONArray("regions")
                    for(i in 0..regionarray.length() -1){
                        var regioncase=Regcases()
                        var obj=regionarray.getJSONObject(i)
                        regioncase.admissioncases= obj.getString("onAdmissionCases")
                        regioncase.deceased= obj.getString("deaths")
                        regioncase.labconfirmcases= obj.getString("labConfirmedCases")
                        regioncase.recovered = obj.getString("discharged")
                        regioncase.state= obj.getString("region").toUpperCase()
                        caselist?.add(regioncase)
                    }
                    maincaselist = caselist

                    responeded=true

                }
                catch(error: JSONException){
                    error.printStackTrace()
                }
            },
            Response.ErrorListener {
                    error: VolleyError? ->
                try{

                }
                catch(error: VolleyError){
                    error.printStackTrace()
                }
            })

        volleyRequest?.add(jsonObjectreq)

    }


    fun formatDate(calendar: Calendar):String{
        return SimpleDateFormat("MMMM d,y", Locale.ENGLISH).format(calendar.time)

    }

    fun showDialog(){
        var view=layoutInflater.inflate(R.layout.popup,null)
        var retry = view.findViewById<Button>(R.id.retryid)
        var exit = view.findViewById<Button>(R.id.exitid)

        dialogbuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogbuilder?.create()
        dialog?.show()
        retry.setOnClickListener {
            connect()
            dialog?.hide()
         }
        exit.setOnClickListener {
            finishAndRemoveTask()
            System.exit(0)
        }

    }

    fun connect(){
        var connectmanager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkinfo = connectmanager.activeNetworkInfo
        if(networkinfo!=null && networkinfo.isConnected){

                getJsonObject(link)

            volleyRequest?.start()
        }
        else{
            showDialog()
        }
    }

    private fun getJsonArray(url: String) {
        var jsonArrayreq= JsonArrayRequest(Request.Method.GET,url, Response.Listener {
            response: JSONArray ->
                try{
                    Toast.makeText(this,"yeah loaded",Toast.LENGTH_SHORT).show()
                    var last= response.getJSONObject(response.length()-2)
                    var case= cases()
                    var calenda=Calendar.getInstance()
                    calenda.add(Calendar.DAY_OF_YEAR,-1)
                    case.Date= last.getString("lastUpdatedAtApify")
                    case.infected=last.getString("infected")
                    case.tested= last.getString("tested")
                    case.recovered= last.getString("recovered")
                    case.deceased= last.getString("deceased")
                    case.activecases=last.getString("activeCases")
                    db?.insertCase(case)
                    getJsonObject(link)

                }
                catch(error:JSONException){

                }
        }, Response.ErrorListener {

        })

        volleyRequest?.add(jsonArrayreq)

    }
}
