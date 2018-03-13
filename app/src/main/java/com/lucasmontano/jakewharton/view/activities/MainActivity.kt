package com.lucasmontano.jakewharton.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.lucasmontano.jakewharton.R
import com.lucasmontano.jakewharton.view.fragments.NoPermissionFragment
import com.lucasmontano.jakewharton.view.fragments.RepoListFragment

private val repoListFragment = RepoListFragment.newInstance()
private val noPermissionFragment = NoPermissionFragment.newInstance()

class MainActivity: BaseActivity(), RepoListFragment.OnListFragmentInteractionListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  /**
   * If permission is no granted we need to ask user.
   */
  override fun permissionNotGranted() {

    if ( ! noPermissionFragment.isAdded) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.content, noPermissionFragment)
          .commitAllowingStateLoss()
    }
  }

  /**
   * If permissions is granted we can call {@see RepoListFragment}
   */
  override fun permissionGranted() {

    if ( ! repoListFragment.isAdded) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.content, repoListFragment)
          .commitAllowingStateLoss()
    }
  }

  /**
   * On Repo clicked.
   *
   * @param url Repo Html Url
   */
  override fun onExploreRepo(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(browserIntent)
  }
}
