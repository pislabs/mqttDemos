package com.pislabs.mqtt.odapp.core.app.module

import android.app.Application

abstract class BaseModule<T> {
    protected var mApplication: Application

    constructor(application: Application) {
        this.mApplication = application
    }

    /**
     * 初始化
     */
    fun init(): T {
        onInit()

        return this as T
    }

    protected open fun onInit() {}
}