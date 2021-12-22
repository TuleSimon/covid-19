package com.thee.horrorcorian.covid19tracker.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.thee.horrorcorian.covid19tracker.model.cases


class dbHandler(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE="CREATE TABLE $TABLE_NAME($ROW_DATE TEXT UNIQUE PRIMARY KEY, $ROW_DECEASED TEXT, $ROW_RECOVERED TEXT, $ROW_INFECTED TEXT," +
                "$ROW_ACTIVE_CASES TEXT, $ROW_TESTED TEXT )"
        db!!.execSQL(CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun readprevCases(): cases {

        var db:SQLiteDatabase=readableDatabase
        var SQL="SELECT * FROM $TABLE_NAME"
        var cursor:Cursor=db.rawQuery(SQL,null)
        var case=cases()

        if(cursor.moveToLast()){
            cursor.moveToLast()
               cursor.moveToPrevious()
               case=cases()
               case.Date = cursor.getString(cursor.getColumnIndex(ROW_DATE))
               case.deceased = cursor.getString(cursor.getColumnIndex(ROW_DECEASED))
               case.tested = cursor.getString(cursor.getColumnIndex(ROW_TESTED))
               case.activecases = cursor.getString(cursor.getColumnIndex(ROW_ACTIVE_CASES))
               case.infected = cursor.getString(cursor.getColumnIndex(ROW_INFECTED))
               case.recovered = cursor.getString(cursor.getColumnIndex(ROW_RECOVERED))


            }

        return case
    }

    fun readCase():ArrayList<cases>{
        var db:SQLiteDatabase=readableDatabase
        var SQL="SELECT * FROM $TABLE_NAME"
        var cursor:Cursor=db.rawQuery(SQL,null)
        var caseList:ArrayList<cases> =ArrayList()

        if(cursor.moveToFirst()){
            cursor.moveToFirst()
            do{
                var case=cases()
                case.Date=cursor.getString(cursor.getColumnIndex(ROW_DATE))
                    case.deceased=cursor.getString(cursor.getColumnIndex(ROW_DECEASED))
                    case.tested=cursor.getString(cursor.getColumnIndex(ROW_TESTED))
                    case.activecases=cursor.getString(cursor.getColumnIndex(ROW_ACTIVE_CASES))
                    case.infected=cursor.getString(cursor.getColumnIndex(ROW_INFECTED))
                    case.recovered=cursor.getString(cursor.getColumnIndex(ROW_RECOVERED))
                    caseList.add(case)
            }
            while(cursor.moveToNext())
        }
        cursor.close()
        return caseList
    }


    fun readACase(date:String):cases{
        var db:SQLiteDatabase=readableDatabase
        var cursor:Cursor=db.query(TABLE_NAME, arrayOf(
            ROW_ACTIVE_CASES, ROW_DECEASED, ROW_DATE, ROW_INFECTED, ROW_RECOVERED, ROW_TESTED), ROW_DATE+" = ?",
            arrayOf(date),null,null,null)
        var case=cases()
        if(cursor.moveToFirst()){
            cursor.moveToFirst()
                case.Date=cursor.getString(cursor.getColumnIndex(ROW_DATE))
                case.deceased=cursor.getString(cursor.getColumnIndex(ROW_DECEASED))
                case.tested=cursor.getString(cursor.getColumnIndex(ROW_TESTED))
                case.activecases=cursor.getString(cursor.getColumnIndex(ROW_ACTIVE_CASES))
                case.infected=cursor.getString(cursor.getColumnIndex(ROW_INFECTED))
                case.recovered=cursor.getString(cursor.getColumnIndex(ROW_RECOVERED))
        }
        cursor.close()
        return case
    }


    fun insertCase(case:cases){
        var db:SQLiteDatabase=writableDatabase
        var content=ContentValues()
        content.put(ROW_RECOVERED,case.recovered)
        content.put(ROW_INFECTED,case.infected)
        content.put(ROW_ACTIVE_CASES, case.activecases)
        content.put(ROW_TESTED,case.tested)
        content.put(ROW_DATE,case.Date)
        content.put(ROW_DECEASED,case.deceased)
        db.insert(TABLE_NAME,null,content)
        db.update(TABLE_NAME, content, ROW_DATE+" =?", arrayOf(case.Date))

    }

    fun update(case:cases){
        var db:SQLiteDatabase=writableDatabase
        var content=ContentValues()
        content.put(ROW_RECOVERED,case.recovered)
        content.put(ROW_INFECTED,case.infected)
        content.put(ROW_ACTIVE_CASES, case.activecases)
        content.put(ROW_TESTED,case.tested)
        content.put(ROW_DATE,case.Date)
        content.put(ROW_DECEASED,case.deceased)
        db.update(TABLE_NAME, content, ROW_DATE+" =?", arrayOf(case.Date))
    }

    fun deleteCase(string:String){
        var db:SQLiteDatabase=writableDatabase
        db.delete(TABLE_NAME,"$ROW_DATE =?", arrayOf(string))
    }

}