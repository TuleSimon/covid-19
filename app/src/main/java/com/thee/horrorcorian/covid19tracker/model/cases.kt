package com.thee.horrorcorian.covid19tracker.model

class cases() {
    var deceased:String?=null
    var recovered:String?=null
    var infected:String?=null
    var activecases:String?=null
    var tested:String?=null
    var Date:String?=null
    constructor(deceased:String,recovered:String,infected:String,activecases:String,tested:String,Date:String):this(){
        this.deceased=deceased
        this.recovered=recovered
        this.infected=infected
        this.activecases=activecases
        this.tested=tested
        this.Date=Date
    }
}