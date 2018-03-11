package com.lucasmontano.jakewharton.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.lucasmontano.jakewharton.JakeApp
import com.lucasmontano.jakewharton.R
import com.lucasmontano.jakewharton.data.RepoData
import com.lucasmontano.jakewharton.view.fragments.RepoListFragment

class MainActivity : AppCompatActivity(), RepoListFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (applicationContext as JakeApp).component.inject(this)

        supportFragmentManager.beginTransaction().replace(R.id.content, RepoListFragment.newInstance()).commitAllowingStateLoss()
    }

    override fun onListFragmentInteraction(item: RepoData) {

    }
}
