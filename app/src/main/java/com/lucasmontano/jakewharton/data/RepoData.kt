package com.lucasmontano.jakewharton.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RepoData : RealmObject() {

    @PrimaryKey
    lateinit var id: String

    open var name: String? = null
    open var description: String? = null
    open var htmlUrl: String? = null
    open var language: String? = null
}