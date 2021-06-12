package com.szymonlukiewicz.truthordare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SetChoiceActivity : AppCompatActivity(),  SetAdapter.SetClickListener{

    private lateinit var viewModel: SetChoiceActivityViewModel
    private lateinit var setNameEditText: EditText
    private lateinit var setListErrorTextView: TextView
    private lateinit var setListConstraintLayout: ConstraintLayout
    private lateinit var progressBar: ProgressBar

    private lateinit var setList: ArrayList<SetChoiceActivityViewModel.SetData>
    private lateinit var adapter: SetAdapter


    fun onAddClick(view: View) {
        if (setNameEditText.text.toString().isNotEmpty()){
            viewModel.addSet(setNameEditText.text.toString())
            setNameEditText.setText("")
        }else{
            Toast.makeText(this, getString(R.string.empty_edit_text_error_message), Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_choice)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView2)
        setNameEditText = findViewById(R.id.setNameEditText)
        setListConstraintLayout = findViewById(R.id.setListConstraintLayout)
        setListErrorTextView = findViewById(R.id.setListErrorTextView)
        progressBar = findViewById(R.id.progressBar)

//      VIEWMODEL SETUP
        viewModel = ViewModelProvider(this).get(SetChoiceActivityViewModel::class.java)

        viewModel.initViewResponse.observe(this, {initDataState ->
            if(!initDataState.isInProgress) {
                if (initDataState.showError) {
                    hideProgressBar()
                    if(initDataState.error == "no sets found"){
                        setListErrorTextView.visibility = View.VISIBLE
                        setListConstraintLayout.visibility = View.INVISIBLE
                    }else{
                        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_LONG).show()
                    }
                } else {
                    hideProgressBar()
                    setListErrorTextView.visibility = View.INVISIBLE
                    setList = initDataState.setDataList!!
                    adapter = SetAdapter(this, this)
                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
                    adapter.loadData(ArrayList(setList))
                }
            }
        })

        viewModel.addViewResponse.observe(this, {addState ->
            when {
                addState.isInProgress -> {
                    showProgressBar()
                }
                addState.showError -> {
                    hideProgressBar()
                    Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_LONG).show()
                }
                else -> {
                    viewModel.getInitData()
                }
            }
        })

        viewModel.init(this)
    }

    override fun onSetCLick(position: Int) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("set_id", setList[position].set_id)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
    }


    private fun showProgressBar(){
        setListConstraintLayout.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        setListConstraintLayout.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }
}