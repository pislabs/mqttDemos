package com.pislabs.mqtt.odapp.core.network.di

import javax.inject.Qualifier

/**
 * 文件上传专用的 OkHttpClient 限定符
 *
 * @author Joker.X
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FileUploadQualifier
