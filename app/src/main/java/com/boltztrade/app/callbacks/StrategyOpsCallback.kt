package com.boltztrade.app.callbacks

interface StrategyOpsCallback {

    fun backtestResult(position: Int)
    fun backtest(position: Int)
    fun deploy(position: Int)
    fun delete(position: Int)
    fun edit(position: Int)
    fun enter(position: Int)
    fun exit(position: Int)
    fun snooze(position: Int)
}