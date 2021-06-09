package com.szymonlukiewicz.truthordare
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.szymonlukiewicz.truthordare.SingleTextViewAdapter.SingleTextViewClickListener
import java.util.*


class MainActivity : AppCompatActivity(), SingleTextViewClickListener {
    private var players = ArrayList<String>()

    private lateinit var adapter: SingleTextViewAdapter
    private lateinit var playerNameEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var  constraintLayout: ConstraintLayout

    private var defConstraintSet = ConstraintSet()
    private var keyboardConstraintSet = ConstraintSet()


    fun onAddPlayerClick(view: View?) {
        players.add(playerNameEditText.text.toString())
        playerNameEditText.setText("")
        adapter.loadData(players)
    }

    fun onStartTheGameClick(view: View?) {
        val intent = Intent(applicationContext, GameActivity::class.java)
        intent.putExtra("players", players)
        startActivity(intent)
    }

    fun onAddToListClick(view: View?) {
        val intent = Intent(applicationContext, AddActivity::class.java)
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        constraintLayout = findViewById(R.id.constraintLayout)
        playerNameEditText = findViewById(R.id.playerNameEditText)
        recyclerView = findViewById(R.id.recyclerView)
        defConstraintSet.clone(constraintLayout)
        keyboardConstraintSet.clone(this, R.layout.activity_main_keyboard_layout)

//      MANAGING KEYBOARD ACTIONS
        constraintLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            constraintLayout.getWindowVisibleDisplayFrame(r)
            val screenHeight = constraintLayout.rootView.height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                slideLayoutUp()
            } else {
                slideLayoutDown()
            }
        }

        adapter = SingleTextViewAdapter(this, this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun slideLayoutDown() {
        TransitionManager.endTransitions(constraintLayout)
        TransitionManager.beginDelayedTransition(constraintLayout)
        defConstraintSet.applyTo(constraintLayout)
    }

    private fun slideLayoutUp() {
        TransitionManager.endTransitions(constraintLayout)
        TransitionManager.beginDelayedTransition(constraintLayout)
        keyboardConstraintSet.applyTo(constraintLayout)
    }

    override fun onSingleTextViewClick(position: Int) {}
}