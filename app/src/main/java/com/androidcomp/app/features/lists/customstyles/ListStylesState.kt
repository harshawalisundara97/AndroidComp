package com.androidcomp.app.features.lists.customstyles

data class SwipeItem(val id: Int, val label: String)
data class ExpandableRow(val id: Int, val title: String, val detail: String)
data class ReorderItem(val id: Int, val label: String)

data class ListStylesState(
    val swipeItems: List<SwipeItem> = listOf(
        SwipeItem(1, "Grocery Run"),
        SwipeItem(2, "Team Standup"),
        SwipeItem(3, "Pay Rent")
    ),
    val expandedRowId: Int? = null,
    val reorderItems: List<ReorderItem> = listOf(
        ReorderItem(1, "Wireframes"),
        ReorderItem(2, "Visual Design"),
        ReorderItem(3, "Prototype"),
        ReorderItem(4, "Handoff")
    )
)
