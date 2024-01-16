package com.example.preorder_app.viewmodels.electronics

import com.example.preorder_app.util.Item

data class ElectronicsUIState(
    var electronicsCartTV: MutableList<Item> = mutableListOf(),
    var electronicsCartRef: MutableList<Item> = mutableListOf(),
    var electronicsCartLap: MutableList<Item> = mutableListOf()
)
