package com.lucasmontano.jakewharton.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RepoData(@PrimaryKey val id: String) : RealmObject() {

    var name: String? = null
    var description: String? = null
    var htmlUrl: String? = null
    var language: String? = null
}