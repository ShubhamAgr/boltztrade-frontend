package com.boltztrade.app.callbacks

interface StrategyCardTouchCallback {
    fun onSelectFirstIndicator(position:Int)
    fun onSelectSecondIndicator(position: Int)
    fun onSelectComparisonOperator(position: Int)
    fun onSelectLogicalOperator(position: Int)
}