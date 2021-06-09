package com.szymonlukiewicz.truthordare

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddActivityViewModel : ViewModel() {

    private val initMutableLiveData = MutableLiveData<InitDataState>()
    private val addMutableLiveData = MutableLiveData<AddDeleteState>()
    private val deleteMutableLiveData = MutableLiveData<AddDeleteState>()

    var initViewResponse: LiveData<InitDataState> = initMutableLiveData
    var addViewResponse: LiveData<AddDeleteState> = addMutableLiveData
    var deleteViewResponse: LiveData<AddDeleteState> = deleteMutableLiveData

    fun deleteTruthOrDare(deletingFrom: String, position: Int) {
        deleteMutableLiveData.postValue(AddDeleteState(isInProgress = true, showError = false, error = null))

    }

    fun addTruthOrDare(addingTo: String, contents: String) {
        addMutableLiveData.postValue(AddDeleteState(isInProgress = true, showError = false, error = null))

    }

    fun init(setID: String) {
        initMutableLiveData.postValue(InitDataState(isInProgress = true, showError = false, error = null, setData = null))
        val truthsList: ArrayList<String> = ArrayList()
        truthsList.add("Czy perughsoiduhgoisurr osiuerghoisurgh oseirughsoirugh osidrughoiuregh soirughoiruh osiurrghoisug")
        truthsList.add("Czy perughsoiduhgoisurr osiuerghoisurgh oseirughsoirugh osidrughoiuregh soirughoiruh osiurrghoisug")
        truthsList.add("Czy perughsoiduhgoisurr osiuerghoisurgh oseirughsoirugh osidrughoiuregh soirughoiruh osiurrghoisug")
        truthsList.add("Czy perughsoiduhgoisurr osiuerghoisurgh oseirughsoirugh osidrughoiuregh soirughoiruh osiurrghoisug")
        truthsList.add("Czy perughsoiduhgoisurr osiuerghoisurgh oseirughsoirugh osidrughoiuregh soirughoiruh osiurrghoisug")

        val daresList: ArrayList<String> = ArrayList()
        daresList.add("urgauhdrgsuigsd giugdufguifg uhfgudfuigfi nvbxmcnvbmxcnbv mxnbvmcnvbxmcn mncg")
        daresList.add("urgauhdrgsuigsd giugdufguifg uhfgudfuigfi nvbxmcnvbmxcnbv mxnbvmcnvbxmcn mncg")
        daresList.add("urgauhdrgsuigsd giugdufguifg uhfgudfuigfi nvbxmcnvbmxcnbv mxnbvmcnvbxmcn mncg")
        daresList.add("urgauhdrgsuigsd giugdufguifg uhfgudfuigfi nvbxmcnvbmxcnbv mxnbvmcnvbxmcn mncg")
        daresList.add("urgauhdrgsuigsd giugdufguifg uhfgudfuigfi nvbxmcnvbmxcnbv mxnbvmcnvbxmcn mncg")


        initMutableLiveData.postValue(InitDataState(isInProgress = false, showError = false, error = null,
                SetData("0", "3kipa", truthsList, daresList)))
    }

    class InitDataState(var isInProgress: Boolean, var showError: Boolean, var error: String?, var setData: SetData?)
    class SetData(var set_id: String, var set_name: String, var truths_list: ArrayList<String>, var dares_list: ArrayList<String>)
    class AddDeleteState(var isInProgress: Boolean, var showError: Boolean, var error: String?)
}