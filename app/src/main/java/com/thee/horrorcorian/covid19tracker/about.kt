package com.thee.horrorcorian.covid19tracker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.thee.horrorcorian.covid19tracker.R

class about : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    fun dial(view: View){
        var number="+2347011840621"
        var intent=Intent(Intent.ACTION_DIAL)
        intent.data =(Uri.parse("tel:" + number))
        startActivity(intent)
    }

    fun openFaceBook(view:View){
        var link="https://Facebook.com/TheeHorrorcorian"
        var intent=Intent(Intent.ACTION_VIEW)
        intent.data=Uri.parse(link)
        startActivity(intent)
    }

    fun openYoutube(view:View){
        var link="https://Youtube.com/channel/UCAWrbuer4AN0xX9gN-FcneQ"
        var intent=Intent(Intent.ACTION_VIEW)
        intent.data=Uri.parse(link)
        startActivity(intent)
    }
}
