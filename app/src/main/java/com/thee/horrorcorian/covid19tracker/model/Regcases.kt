package com.thee.horrorcorian.covid19tracker.model

class Regcases() {
    var deceased:String?=null
    var recovered:String?=null
    var labconfirmcases:String?=null
    var admissioncases:String ?=null
    var state:String?=null

    constructor(deceased:String,recovered:String,labconfirmcases:String,state:String, admissioncases:String):this(){
        this.deceased=deceased
        this.recovered=recovered
        this.labconfirmcases=labconfirmcases
        this.state=state
        this.admissioncases = admissioncases

    }
}