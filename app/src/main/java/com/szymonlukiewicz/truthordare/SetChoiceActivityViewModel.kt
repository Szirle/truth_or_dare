package com.szymonlukiewicz.truthordare

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SetChoiceActivityViewModel : ViewModel() {

    private var databaseManager: DatabaseManager? = null
    private lateinit var context: Context

    private val initMutableLiveData = MutableLiveData<InitDataState>()
    private val addMutableLiveData = MutableLiveData<AddDeleteState>()
//    private val deleteMutableLiveData = MutableLiveData<AddDeleteState>()

    var initViewResponse: LiveData<InitDataState> = initMutableLiveData
    var addViewResponse: LiveData<AddDeleteState> = addMutableLiveData
//    var deleteViewResponse: LiveData<AddDeleteState> = deleteMutableLiveData


    private fun deserialize(content: String): List<String> {
        return listOf(*content.split(Companion.ARRAY_DIVIDER.toRegex()).toTypedArray())
    }

    fun init(context: Context){
        this.context = context
        databaseManager = DatabaseManager(context)

        getInitData()
    }

//    fun deleteSet(deletingFrom: String, position: Int) {}

    fun addSet(name: String) {
        addMutableLiveData.postValue(
            AddDeleteState(
                isInProgress = true,
                showError = false,
                error = null
            )
        )

        val result = databaseManager!!.addNewSet(name)

        if (result){
            addMutableLiveData.postValue(
                AddDeleteState(
                    isInProgress = false,
                    showError = false,
                    error = null
                )
            )
        }else{
            addMutableLiveData.postValue(
                AddDeleteState(
                    isInProgress = false,
                    showError = true,
                    error = "adding error"
                )
            )
        }
    }

    fun getInitData() {
        initMutableLiveData.postValue(
            InitDataState(
                isInProgress = true,
                showError = false,
                error = null,
                setDataList = null
            )
        )

        val setList: ArrayList<SetData> = ArrayList()
        val data = databaseManager!!.data

        while (data.moveToNext()) {
            val truthsArrayList = if (data.getString(2) != null){
                ArrayList(deserialize(data.getString(2)))
            }else{
                null
            }

            val daresArrayList = if (data.getString(3) != null){
                ArrayList(deserialize(data.getString(3)))
            }else{
                null
            }

            setList.add(SetData(data.getString(0), data.getString(1), truthsArrayList, daresArrayList))
        }

        if (setList.size > 0){
            initMutableLiveData.postValue(
                InitDataState(
                    isInProgress = false,
                    showError = false,
                    error = null,
                    setDataList = setList
                )
            )
        }else{
            initMutableLiveData.postValue(
                InitDataState(
                    isInProgress = false,
                    showError = true,
                    error = "no sets found",
                    setDataList = null
                )
            )
        }
    }

    class InitDataState(
        var isInProgress: Boolean,
        var showError: Boolean,
        var error: String?,
        var setDataList: ArrayList<SetData>?
    )
    class SetData(
        var set_id: String,
        var set_name: String,
        var truths_list: ArrayList<String>?,
        var dares_list: ArrayList<String>?
    )

    class AddDeleteState(var isInProgress: Boolean, var showError: Boolean, var error: String?)


    companion object {
        var ARRAY_DIVIDER = "_,_"
    }
}