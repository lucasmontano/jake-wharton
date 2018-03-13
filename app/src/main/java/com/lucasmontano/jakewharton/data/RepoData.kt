package com.lucasmontano.jakewharton.data

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RepoData: RealmObject() {

  @PrimaryKey
  lateinit var id: String

  open var name: String? = null

  open var description: String? = null

  open var language: String? = null

  @SerializedName("html_url")
  open var htmlUrl: String? = null
}