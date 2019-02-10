package com.thedeveloperworldisyours.architecture.git.source

open class DataSourceException(message: String? = null) : Exception(message)

class RemoteDataNotFoundException : DataSourceException("Data not found in remote data source")
