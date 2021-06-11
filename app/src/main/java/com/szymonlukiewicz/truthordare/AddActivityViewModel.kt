package com.szymonlukiewicz.truthordare

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AddActivityViewModel : ViewModel() {

    private var databaseManager: DatabaseManager? = null
    private lateinit var context: Context
    private lateinit var setID: String
    private lateinit var currentSet: SetData

    private val initMutableLiveData = MutableLiveData<InitDataState>()
    private val addMutableLiveData = MutableLiveData<AddDeleteState>()
    private val deleteMutableLiveData = MutableLiveData<AddDeleteState>()

    var initViewResponse: LiveData<InitDataState> = initMutableLiveData
    var addViewResponse: LiveData<AddDeleteState> = addMutableLiveData
    var deleteViewResponse: LiveData<AddDeleteState> = deleteMutableLiveData

    var ARRAY_DIVIDER = "_,_"

    fun serialize(content: ArrayList<String>): String? {
        return TextUtils.join(ARRAY_DIVIDER, content)
    }

    fun deserialize(content: String): List<String> {
        return listOf(*content.split(ARRAY_DIVIDER.toRegex()).toTypedArray())
    }

    fun init(context: Context, setID: String){
        this.context = context
        this.setID = setID

        getInitData()
    }

    fun deleteTruthOrDare(column: String, position: Int) {
        deleteMutableLiveData.postValue(
            AddDeleteState(
                isInProgress = true,
                showError = false,
                error = null
            )
        )

        val showError:Boolean
        var contentsList = if (column == "dares_list"){
            currentSet.dares_list
        }else{
            currentSet.truths_list
        }
        if (contentsList!!.size == 1){
            showError = !databaseManager!!.updateTruthsOrDaresList(set_id = setID, column = column, content = null)
        }else{
            contentsList.removeAt(position)
            showError = !databaseManager!!.updateTruthsOrDaresList(set_id = setID, column = column, content = serialize(contentsList))
        }

        deleteMutableLiveData.postValue(
            AddDeleteState(
                isInProgress = false,
                showError = showError,
                error = null
            )
        )
    }

    fun addTruthOrDare(column: String, contents: String) {
        addMutableLiveData.postValue(
            AddDeleteState(
                isInProgress = true,
                showError = false,
                error = null
            )
        )

        var contentsList: ArrayList<String>? = if (column == "dares_list"){
            currentSet.dares_list
        }else{
            currentSet.truths_list
        }

        if (contentsList == null){
            contentsList = ArrayList()
        }

        contentsList.add(contents)

        val showError = !databaseManager!!.updateTruthsOrDaresList(set_id = setID, column = column, content = serialize(contentsList))

        addMutableLiveData.postValue(
            AddDeleteState(
                isInProgress = false,
                showError = showError,
                error = null
            )
        )
    }

    fun getInitData() {
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

        currentSet = SetData(
            set_id = data.getString(0),
            set_name = data.getString(1),
            truths_list = truthsArrayList,
            dares_list = daresArrayList)

        initMutableLiveData.postValue(
            InitDataState(
                isInProgress = false,
                showError = false,
                error = null,
                setData = currentSet
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
    class AddDeleteState(var isInProgress: Boolean, var showError: Boolean, var error: String?)
}