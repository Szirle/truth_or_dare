package com.szymonlukiewicz.truthordare

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class GameActivityViewModel : ViewModel() {

    private var databaseManager: DatabaseManager? = null

    private val initMutableLiveData = MutableLiveData<InitDataState>()

    var initViewResponse: LiveData<InitDataState> = initMutableLiveData

    var ARRAY_DIVIDER = "_,_"

    fun deserialize(content: String): List<String> {
        return listOf(*content.split(ARRAY_DIVIDER.toRegex()).toTypedArray())
    }


    fun getInitData(setID: String, context: Context) {
        initMutableLiveData.postValue(
            InitDataState(
                isInProgress = true,
                showError = false,
                error = null,
                setData = null
            )
        )

        databaseManager = DatabaseManager(context)

        val data = databaseManager!!.getSet(setID)
        data!!.moveToNext()
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

        if (daresArrayList == null || truthsArrayList == null){
            initMutableLiveData.postValue(
                InitDataState(
                    isInProgress = false,
                    showError = true,
                    error = "empty list",
                    setData = null
                )
            )
            return
        }

        initMutableLiveData.postValue(
            InitDataState(
                isInProgress = false,
                showError = false,
                error = null,
                SetData(
                    set_id = data.getString(0),
                    set_name = data.getString(1),
                    truths_list = truthsArrayList,
                    dares_list = daresArrayList)
            )
        )
    }

    class InitDataState(
        var isInProgress: Boolean,
        var showError: Boolean,
        var error: String?,
        var setData: SetData?
    )
    class SetData(
        var set_id: String,
        var set_name: String,
        var truths_list: ArrayList<String>?,
        var dares_list: ArrayList<String>?
    )
}