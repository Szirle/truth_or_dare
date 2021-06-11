package com.szymonlukiewicz.truthordare

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

class GameActivity : AppCompatActivity() {
    var players = ArrayList<String>()
    private var truthsArrayList = ArrayList<String>()
    private var daresArrayList = ArrayList<String>()

    private lateinit var bottleImageView: ImageView
    private lateinit var truthCardView: CardView
    private lateinit var dareCardView: CardView
    private lateinit var titleTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var alertDialog: AlertDialog

    private var rotatedDegrees = 0.0
    private var numberOfPlayers: Int? = null


    fun onBottleClick(view: View?) {
        val rd = Random()
        val randomDegrees = rd.nextInt(720) + 720

        val interpolator = AccelerateDecelerateInterpolator()
        val animation: Animation = RotateAnimation(rotatedDegrees.toFloat(), (rotatedDegrees.toInt() + randomDegrees).toFloat(), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        animation.duration = 1000
        animation.interpolator = interpolator
        animation.repeatCount = 0
        animation.isFillEnabled = true
        animation.fillAfter = true

//      CALCULATING CHOSEN PLAYER INDEX
        rotatedDegrees += randomDegrees
        rotatedDegrees %= 360
        val chosenPlayerNumber = rotatedDegrees / (360 / numberOfPlayers!!)
        val chosenPlayerIndex = ceil(chosenPlayerNumber).toInt()  - 1

        animation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                createPopUp(players[chosenPlayerIndex])
            }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        bottleImageView.startAnimation(animation)
    }

    fun onTruthClick(view: View?) {
        val random = Random()
        val randomIndex = random.nextInt(truthsArrayList.size)
        titleTextView.text = getString(R.string.truth)
        questionTextView.text = truthsArrayList[randomIndex]
        questionTextView.visibility = View.VISIBLE
        titleTextView.visibility = View.VISIBLE
        dareCardView.visibility = View.INVISIBLE
        truthCardView.visibility = View.INVISIBLE
    }

    fun onDareClick(view: View?) {
        val random = Random()
        val randomIndex = random.nextInt(daresArrayList.size)
        titleTextView.text = getString(R.string.dare)
        questionTextView.text = daresArrayList[randomIndex]
        questionTextView.visibility = View.VISIBLE
        titleTextView.visibility = View.VISIBLE
        dareCardView.visibility = View.INVISIBLE
        truthCardView.visibility = View.INVISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        bottleImageView = findViewById(R.id.bottleImageView)
        bottleImageView.bringToFront()

        val intent = intent
        players = intent.getSerializableExtra("players") as ArrayList<String>
        numberOfPlayers = players.size

//      RETRIEVING DATA FROM LOCAL SQL DATABASE
//        val data = truthsDatabaseHelper!!.data
//        while (data.moveToNext()) {
//            truthsArrayList.add(data.getString(1))
//        }
//        val data2 = daresDatabaseHelper!!.data
//        while (data2.moveToNext()) {
//            daresArrayList.add(data2.getString(1))
//        }
        setupChart()
    }

    private fun setupChart() {
        val pieEntries: MutableList<PieEntry> = ArrayList()
        for (player in players) {
            pieEntries.add(PieEntry(1F, player))
        }
        val pieDataSet = PieDataSet(pieEntries, "1234")
        pieDataSet.setColors(resources.getColor(R.color.colorPrimary))
        pieDataSet.setDrawValues(false)
        pieDataSet.sliceSpace = 4f
        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(40f)
        val pieChart = findViewById<PieChart>(R.id.chart)
        pieChart.setTouchEnabled(false)
        pieChart.setHoleColor(resources.getColor(R.color.colorTransparent))
        pieChart.setEntryLabelTextSize(20f)
        val typeface = Typeface.create("montserrat", Typeface.BOLD)
        pieChart.setEntryLabelTypeface(typeface)
        pieChart.isDrawHoleEnabled = false
        pieChart.data = pieData
        pieChart.invalidate()
    }

    private fun createPopUp(name: String) {
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.truth_or_dare_pop_up, viewGroup, false)
        alertDialog = AlertDialog.Builder(this)
                .setView(dialogView).create()
        alertDialog.show()
        truthCardView = dialogView.findViewById(R.id.truthCardView)
        dareCardView = dialogView.findViewById(R.id.dareCardView)
        titleTextView = dialogView.findViewById(R.id.titleTextView)
        nameTextView = dialogView.findViewById(R.id.nameTextView)
        questionTextView = dialogView.findViewById(R.id.questionTextView)
        nameTextView.text = name
    }
}