package com.lucasmontano.jakewharton.networking

data class RepoResponse(val id: String) {

    var name: String? = null
    var description: String? = null
    var htmlUrl: String? = null
    var language: String? = null
}