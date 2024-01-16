package com.example.preorder_app.viewmodels.clothes

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.empty.datastore.PreferenceDataStoreHelper
import com.example.preorder_app.util.Item
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ClothesVM : ViewModel(){

    private val TAG = ClothesVM::class.simpleName

    var clothesUIState =  mutableStateOf(ClothesUIState())

    var loading = mutableStateOf(false)

    init{
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase(){
        viewModelScope.launch {
            loading.value = true

            try{
                val db = Firebase.firestore //creating database
                db.collection("Clothes")
                    .get()
                    .addOnSuccessListener {items->
                            for(item in items){
                                val obj = item.toObject<Item>()
                                clothesUIState.value.clothes.add(obj)
                            }
                        loading.value=false
                    }
                    .addOnFailureListener{
                        loading.value=false
                    }
            }catch(e:Exception){
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