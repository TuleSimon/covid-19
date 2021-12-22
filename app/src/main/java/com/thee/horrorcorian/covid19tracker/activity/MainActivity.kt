package com.thee.horrorcorian.covid19tracker.activity

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.transition.Visibility
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.thee.horrorcorian.covid19tracker.R
import com.thee.horrorcorian.covid19tracker.about
import com.thee.horrorcorian.covid19tracker.data.dbHandler
import com.thee.horrorcorian.covid19tracker.data.jsonobj
import com.thee.horrorcorian.covid19tracker.data.maincaselist
import com.thee.horrorcorian.covid19tracker.data.recyclerAdapter
import com.thee.horrorcorian.covid19tracker.model.Regcases
import com.thee.horrorcorian.covid19tracker.model.cases
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    var piechart:PieChart?=null
    var barChart:BarChart?=null

    var volleyRequest: RequestQueue?=null
    val link="https://api.apify.com/v2/key-value-stores/Eb694wt67UxjdSGbc/records/LATEST?disableRedirect=true"
    var textview: TextView?=null
    var infected:String?=null
    var tested:String?=null
    var recovered:String?=null
    var deceased:String?=null
    var activeCases:String?=null
    val country="Nigeria"
    var date=Calendar.getInstance()
    var list:ArrayList<cases>?=null
    var db: dbHandler?=null
    var prevcase: cases?=null
    var responeded=false
    var values:ArrayList<PieEntry> = ArrayList()
    var values2:ArrayList<BarEntry> = ArrayList()
    var recyclerAdapter: recyclerAdapter?=null
    var caselist:ArrayList<Regcases> ?=null
    var layoutmanage: RecyclerView.LayoutManager ?=null
    var dateArray:ArrayList<String> ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        db= dbHandler(this)

        setSupportActionBar(toolbarmain)

        caselist= maincaselist

        recyclerAdapter = recyclerAdapter(this,caselist!!)
        layoutmanage= LinearLayoutManager(this)
        recyclerviewofregion. layoutManager=layoutmanage
        recyclerviewofregion.adapter = recyclerAdapter



         piechart=piechartid
        piechart?.description?.isEnabled=false
        piechart?.setUsePercentValues(false)
        piechart?.setExtraOffsets(6f,-2f,0f,5f)
        piechart?.setEntryLabelColor(Color.TRANSPARENT)
        piechart?.visibility= View.INVISIBLE
        piechart?.dragDecelerationFrictionCoef=0.95f

        piechart?.isDrawHoleEnabled=true
        piechart?.setHoleColor(Color.DKGRAY)
        piechart?.transparentCircleRadius=61f

        barChart=barchartid
        barChart?.description?.isEnabled=false

        getJsonObject()
        //var dateArray:Array<String> ?=null
        list=db?.readCase()
        dateArray = ArrayList<String>()
        getDates(dateArray!!)


      //  prevcase=db?.readprevCases()!!





        //getString(link)


        val prevcase=db?.readprevCases()
        if(prevcase!=null){

            val added =(deceased?.replace(",","")!!.toInt())?.minus(prevcase.deceased!!.replace(",","")!!.toInt()!!)
            var infectadd =(infected?.replace(",","")!!.toInt())?.minus(prevcase.infected?.replace(",","")!!.toInt()!!)
            var recovadd =(recovered?.replace(",","")!!.toInt())?.minus(prevcase.recovered?.replace(",","")!!.toInt()!!)
            var testdd =(tested?.replace(",","")!!.toInt())?.minus(prevcase.tested?.replace(",","")!!.toInt()!!)
            if(added!=null){
                deceasedvalue.text= "$deceased (+$added)"
                deceasedvalue2.text=added.toString()
                infectedvalue2.text=infectadd.toString()
                infectedvalue.text="$infected (+$infectadd)"
                caseslabel.text="As of ${formatDate(date)}\n $infectadd NEW CASES OF  COVID-19 \nHAS BEEN DISCOVERED IN NIGERIA"
                recoveredvalue2.text=recovadd.toString()
                recoveredvalue.text="$recovered (+$recovadd)"
                testedvalue.text="$tested (+$testdd)"

            }
        }

         spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
             print("")
            }

           override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            var newcase= db?.readACase(dateArray?.get(position)!!)
            if(newcase?.Date!=null) {
                infected = newcase?.infected
                tested = newcase?.tested
                recovered = newcase?.recovered
                deceased = newcase?.deceased
                activeCases = newcase?.activecases
                piechart?.visibility = View.INVISIBLE
                barChart?.visibility = View.INVISIBLE
                populate()
                horizontalScrollView2.scrollTo(showcases2.left, 0)
            }
            }

        }


        bottomNavigationView.setOnNavigationItemSelectedListener(object: BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
               if(item.itemId== R.id.dash){
                  // aboutLayout.visibility= View.INVISIBLE
                  regionframe.visibility= View.GONE
                   infoScroll.visibility=View.GONE

                   //fragmentcontainer.visibility = View.GONE
               }
                else if(item.itemId==R.id.region){
                 //  aboutLayout.visibility= View.INVISIBLE
                   infoScroll.visibility=View.GONE
                   regionframe.visibility= View.VISIBLE

               }
                else{
                  // aboutLayout.visibility= View.INVISIBLE
                   infoScroll.visibility=View.VISIBLE
                   regionframe.visibility= View.GONE
               }
                return true
            }

        })

    }


    fun getJsonObject(){
       // var jsonObjectreq= JsonObjectRequest(
         //   Request.Method.GET,url,
           // Response.Listener {
             //       response: JSONObject ->
                try{

                    infected = jsonobj?.getString("infected")
                    tested = jsonobj?.getString("tested")
                    recovered = jsonobj?.getString("recovered")
                    deceased = jsonobj?.getString("deceased")
                    activeCases=jsonobj?.getString("activeCases")

                   populate()
                    responeded=true
                }
                catch(error: JSONException){
                    error.printStackTrace()
                    Toast.makeText(this,error.message, Toast.LENGTH_LONG).show()
                }
            //},
            //Response.ErrorListener {
              //      error: VolleyError? ->
                //try{
                  //  Toast.makeText(this,error?.message, Toast.LENGTH_LONG).show()
                //}
                //catch(error:VolleyError){
                  //  error.printStackTrace()
                //}
            //})
        //volleyRequest?.add(jsonObjectreq)

    }


    fun formatDate(calendar: Calendar):String{
        return SimpleDateFormat("MMMM d,y", Locale.ENGLISH).format(calendar.time)

    }

    fun getDates(dateArray:ArrayList<String>){
        var index = 0
        list?.reverse()
        if(list?.size!!>0){
        for(i in list!!.iterator()){
                dateArray.add(i.Date!!)

        }
        }
        else{
            dateArray[0]="May 5,2020"
            dateArray[1]="May 6,2020"
        }
        val adapter= ArrayAdapter(this, R.layout.spinner_item, dateArray)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter=adapter
    }


    fun populate(){
        infectedvalue.text=infected
        testedvalue.text=tested
        recoveredvalue.text=recovered
        deceasedvalue.text=deceased
        activecasesvalue.text=activeCases

        var infectedfloat=infected?.replace(",","")!!.toFloat()
        values.clear()
        values2.clear()
        values.add(PieEntry(recovered?.replace(",","")!!.toFloat()!!,"Recovered"))
        values.add(PieEntry(infectedfloat!!,"Infected"))
        values.add(PieEntry( tested?.replace(",","")!!.toFloat()!!,"Tested"))
        values.add(PieEntry(deceased?.replace(",","")!!.toFloat()!!,"Deaths"))
        values.add(PieEntry(activeCases?.replace(",","")!!.toFloat()!!,"Active Cases"))

        values2.add(BarEntry(1f,recovered?.replace(",","")!!.toFloat()!!))
        values2.add(BarEntry(2f,infected?.replace(",","")!!.toFloat()!!))
        values2.add(BarEntry(3f,deceased?.replace(",","")!!.toFloat()!!))
        values2.add(BarEntry(4f,tested?.replace(",","")!!.toFloat()!!))
        values2.add(BarEntry(5f,activeCases?.replace(",","")!!.toFloat()!!))


        var dataset = PieDataSet(values,"Nig. Covid-19 Cases")
        dataset.sliceSpace=1f
        dataset.selectionShift=10f
        dataset.colors=ColorTemplate.COLORFUL_COLORS .toMutableList()

        var piedata=PieData(dataset)
        piedata.setValueTextColor(Color.BLACK)
        piedata.setValueTextSize(10f)

        piechart?.data=piedata
        var leg =piechart?.legend
        leg?.textColor=Color.WHITE
        leg?.verticalAlignment=Legend.LegendVerticalAlignment.CENTER
        leg?.horizontalAlignment=Legend.LegendHorizontalAlignment.RIGHT
        leg?.orientation=Legend.LegendOrientation.VERTICAL
        leg?.textSize=12f
        piechart?.refreshDrawableState()
        piechart?.visibility= View.VISIBLE
        piechart?.animateY(1000,Easing.EaseInBounce)

        var bardataset=BarDataSet(values2,"Nigeria cases")
        bardataset.colors=ColorTemplate.COLORFUL_COLORS.toMutableList()
        bardataset.setDrawValues(true)
        bardataset.valueTextColor=Color.WHITE
        piechart?.notifyDataSetChanged()


        var data=BarData(bardataset)

        barChart?.data=data
        barChart?.refreshDrawableState()
        barChart?.visibility= View.VISIBLE
        barChart?.animateX(1000,Easing.EaseInBounce)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.aboutme) {
            //aboutLayout.visibility= View.VISIBLE
            var intent= Intent(this, about()::class.java)
            startActivity(intent)

        }

        return true
    }

}
