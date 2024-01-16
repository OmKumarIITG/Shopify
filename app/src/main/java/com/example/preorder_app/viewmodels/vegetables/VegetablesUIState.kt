package com.example.preorder_app.viewmodels.vegetables

import com.example.preorder_app.util.Item

data class VegetablesUIState(
    var vegetables: MutableList<Item> = mutableListOf(),
)
