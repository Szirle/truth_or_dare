package com.szymonlukiewicz.truthordare

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AddActivity : AppCompatActivity(), SingleTextViewWithXAdapter.SingleTextViewWithXClickListener {
    private var truthsDatabaseHelper: TruthsDatabaseHelper? = null
    private var daresDatabaseHelper: DaresDatabaseHelper? = null
    private lateinit var viewModel: AddActivityViewModel

    private lateinit var adapter: SingleTextViewWithXAdapter
    private lateinit var contentEdiText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var constraintLayout: ConstraintLayout


    private var isDareDisplayed = true


    fun onTruthClick(view: View?) {
//        val insertData = truthsDatabaseHelper!!.addData(contentEdiText.text.toString())
//        contentEdiText.setText("")
//        if (insertData) {
//            toastMessage(getString(R.string.success_message))
//        } else {
//            toastMessage(getString(R.string.error_message))
//        }
        if (isDareDisplayed){
            val slideFromRight = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_from_right)
            val slideOutLeft = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_out_left)
            slideOutLeft.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    constraintLayout.startAnimation(slideFromRight)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            constraintLayout.startAnimation(slideOutLeft)
            isDareDisplayed = false
        }
    }

    fun onDareClick(view: View?) {
//        val insertData = daresDatabaseHelper!!.addData(contentEdiText.text.toString())
//        contentEdiText.setText("")
//        if (insertData) {
//            toastMessage(getString(R.string.success_message))
//        } else {
//            toastMessage(getString(R.string.error_message))
//        }
        if (!isDareDisplayed){

            isDareDisplayed = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_activity)

        contentEdiText = findViewById(R.id.contentEditText)
        recyclerView = findViewById(R.id.addDeleteActivityRecyclerView)
        constraintLayout = findViewById(R.id.constraintLayout)

//      DATABASE SETUP
        truthsDatabaseHelper = TruthsDatabaseHelper(this)
        daresDatabaseHelper = DaresDatabaseHelper(this)

//      VIEWMODEL SETUP
        viewModel = ViewModelProvider(this).get(AddActivityViewModel::class.java)
        viewModel.deleteViewResponse.observe(this, { addState ->
            if (addState.isInProgress) {

            } else if (addState.showError) {
                val toast = Toast.makeText(this, addState.error, Toast.LENGTH_LONG)
                toast.show()
            } else {
                val toast = Toast.makeText(this, getString(R.string.success_message), Toast.LENGTH_LONG)
                toast.show()
            }
        })

        viewModel.initViewResponse.observe(this, { initDataState ->
            if (!initDataState.isInProgress) {
                if (initDataState.showError) {
                    val toast = Toast.makeText(this, initDataState.error, Toast.LENGTH_LONG)
                    toast.show()
                } else {
                    adapter = SingleTextViewWithXAdapter(this, this)
                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
                    adapter.loadData(initDataState.setData?.dares_list)
                }
            }
        })
        viewModel.init("ID")
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSingleTextViewWithXClick(position: Int) {
        viewModel.deleteTruthOrDare("truth_list", position)
    }
}