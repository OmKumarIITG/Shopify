package com.example.preorder_app.viewmodels.clothes

import com.example.preorder_app.util.Item

data class ClothesUIState(
    var clothes: MutableList<Item> = mutableListOf(),
)
