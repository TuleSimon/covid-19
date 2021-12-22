package com.thee.horrorcorian.covid19tracker.data

import com.thee.horrorcorian.covid19tracker.model.Regcases
import org.json.JSONObject

val DATABASE_NAME:String="covid19.db"
val DATABASE_VERSION:Int=1
val TABLE_NAME:String="cases"

val ROW_INFECTED="infected"
val ROW_RECOVERED="recovered"
val ROW_DECEASED="deceased"
val ROW_TESTED="tested"
val ROW_DATE="date"
val ROW_ACTIVE_CASES="active_cases"

var jsonobj:JSONObject?=null
var maincaselist:ArrayList<Regcases> ?=null



