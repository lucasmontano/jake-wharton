package com.lucasmontano.jakewharton

import com.lucasmontano.jakewharton.data.RepoData
import org.powermock.api.mockito.PowerMockito

import io.realm.Realm

import org.powermock.api.mockito.PowerMockito.mockStatic
import org.powermock.api.mockito.PowerMockito.`when`

object MockSupport {

  fun mockRealm(): Realm {

    mockStatic(Realm::class.java)

    val mockRealm = PowerMockito.mock<Realm>(Realm::class.java)

    `when`(mockRealm.createObject(RepoData::class.java)).thenReturn(RepoData())
    `when`(Realm.getDefaultInstance()).thenReturn(mockRealm)

    return mockRealm
  }
}