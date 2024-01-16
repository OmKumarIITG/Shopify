package com.example.preorder_app.viewmodels.electronics

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.empty.datastore.PreferenceDataStoreHelper
import com.example.preorder_app.util.Item
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ElectronicsVM : ViewModel(){

    private val TAG = ElectronicsVM::class.simpleName

    var electronicsUIState =  mutableStateOf(ElectronicsUIState())

    var loading = mutableStateOf(false)

    init{
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase(){
        viewModelScope.launch {
            loading.value = true

            try{
                val db = Firebase.firestore //creating database
                db.collection("Electronics")
                    .get()
                    .addOnSuccessListener {items->
                            for(item in items){
                                val obj = item.toObject<Item>()
//                                Log.d(TAG,"obj details ${obj.toString()}")
                                if(obj.id=="t"){
                                    electronicsUIState.value.electronicsCartTV.add(obj)
                                }else if(obj.id=="r"){
                                    electronicsUIState.value.electronicsCartRef.add(obj)
                                }else if(obj.id=="l"){
                                    electronicsUIState.value.electronicsCartLap.add(obj)
                                }
                            }
                        Log.d(TAG,electronicsUIState.value.electronicsCartTV.toString())

                        loading.value=false
                    }
                    .addOnFailureListener{
                        Log.d(TAG,"inside failure listener")
                        loading.value=false
                    }
            }catch(e:Exception){
                Log.d(TAG,"inside catch")
                loading.value=false
            }
        }
    }

    fun PreorderClicked(item: Item,dataStoreHelper: PreferenceDataStoreHelper,username:String){
        runBlocking {
            val state = dataStoreHelper.containsKey(stringPreferencesKey( item.name+"$$$"+item.price))
            if(state){
                dataStoreHelper.removePreference(stringPreferencesKey( item.name+"$$$"+item.price))
            }else{
                dataStoreHelper.putPreference(stringPreferencesKey( item.name+"$$$"+item.price),username)
            }
        }
    }

    fun itemState(item: Item, dataStoreHelper: PreferenceDataStoreHelper, username:String):Boolean{
        return runBlocking {
            dataStoreHelper.containsKey(stringPreferencesKey( item.name+"$$$"+item.price))
        }
    }

}