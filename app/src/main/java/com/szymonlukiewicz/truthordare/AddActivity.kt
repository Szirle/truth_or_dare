package com.szymonlukiewicz.truthordare

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_activity.*


class AddActivity : AppCompatActivity(), SingleTextViewWithXAdapter.SingleTextViewWithXClickListener {
    private var daresList: ArrayList<String>? = ArrayList()
    private var truthsList: ArrayList<String>? = ArrayList()
    private lateinit var viewModel: AddActivityViewModel

    private lateinit var adapter: SingleTextViewWithXAdapter
    private lateinit var contentEdiText: EditText
    private lateinit var emptyListErrorTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var constraintLayout: ConstraintLayout


    private var displayed = "dares_list"


    fun onAddClick(view: View?){
        viewModel.addTruthOrDare(displayed, contentEditText.text.toString())
        contentEdiText.setText("")
    }

    fun onTruthClick(view: View?) {
        if (displayed == "dares_list"){
            val slideFromRight = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_from_right)
            val slideOutLeft = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_out_left)
            slideOutLeft.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    contentEdiText.hint = getString(R.string.truth)
                    if(truthsList != null){
                        emptyListErrorTextView.visibility = View.INVISIBLE
                        adapter.loadData(truthsList)
                        recyclerView.visibility = View.VISIBLE
                    }else{
                        emptyListErrorTextView.visibility = View.VISIBLE
                        recyclerView.visibility = View.INVISIBLE
                    }
                    constraintLayout.startAnimation(slideFromRight)
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
            constraintLayout.startAnimation(slideOutLeft)
            displayed = "truths_list"
        }
    }

    fun onDareClick(view: View?) {
        if (displayed == "truths_list"){
            val slideFromLeft = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_from_left)
            val slideOutRight = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_out_right)
            slideOutRight.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    contentEdiText.hint = (getString(R.string.dare))
                    if(daresList != null){
                        emptyListErrorTextView.visibility = View.INVISIBLE
                        adapter.loadData(daresList)
                        recyclerView.visibility = View.VISIBLE
                    }else{
                        emptyListErrorTextView.visibility = View.VISIBLE
                        recyclerView.visibility = View.INVISIBLE
                    }
                    constraintLayout.startAnimation(slideFromLeft)
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
            constraintLayout.startAnimation(slideOutRight)
            displayed = "dares_list"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_activity)

        val setId = intent.getStringExtra("set_id")!!

        contentEdiText = findViewById(R.id.contentEditText)
        recyclerView = findViewById(R.id.addDeleteActivityRecyclerView)
        constraintLayout = findViewById(R.id.constraintLayout)
        emptyListErrorTextView = findViewById(R.id.emptyListErrorTextView)

//      VIEWMODEL SETUP
        viewModel = ViewModelProvider(this).get(AddActivityViewModel::class.java)
        viewModel.addViewResponse.observe(this, { addState ->
            if (addState.isInProgress) {
//                SHOW PROGRESS BAR

            } else if (addState.showError) {
                //                    HIDE PROGRESS BAR
                val toast = Toast.makeText(this, addState.error, Toast.LENGTH_LONG)
                toast.show()
            } else {
                viewModel.getInitData()
            }
        })

        viewModel.deleteViewResponse.observe(this, { deleteState ->
            if (deleteState.isInProgress) {
//                SHOW PROGRESS BAR

            } else if (deleteState.showError) {
//                    HIDE PROGRESS BAR
                val toast = Toast.makeText(this, deleteState.error, Toast.LENGTH_LONG)
                toast.show()
            } else {
                viewModel.getInitData()
            }
        })

        viewModel.initViewResponse.observe(this, { initDataState ->
            if (!initDataState.isInProgress) {
                if (initDataState.showError) {
//                  HIDE PROGRESS BAR
                    toastMessage(initDataState.error)
                } else {
//                  HIDE PROGRESS BAR
                    daresList = initDataState.setData?.dares_list
                    truthsList = initDataState.setData?.truths_list
                    adapter = SingleTextViewWithXAdapter(this, this)
                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
                    if(displayed == "dares_list"){
                        if(daresList != null){
                            adapter.loadData(daresList)
                        }else{
                            emptyListErrorTextView.visibility = View.VISIBLE
                        }
                    }else{
                        if(truthsList != null){
                            adapter.loadData(truthsList)
                        }else{
                            emptyListErrorTextView.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
        viewModel.init(this, setId)
    }

    private fun toastMessage(message: String?) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onSingleTextViewWithXClick(position: Int) {
        viewModel.deleteTruthOrDare(displayed, position)
    }
}