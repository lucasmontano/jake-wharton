package com.lucasmontano.jakewharton.view.activities

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.lucasmontano.jakewharton.view.interfaces.RequestPermissionView

abstract class BaseActivity : AppCompatActivity(), RequestPermissionView {

  abstract fun permissionNotGranted()
  abstract fun permissionGranted()

  /**
   * OnResume we need to check the permissions.
   */
  public override fun onResume() {
    super.onResume()

    val accessNetworkStatePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
    if (accessNetworkStatePerm == PackageManager.PERMISSION_GRANTED) {
      permissionGranted()
    } else {
      permissionNotGranted()
    }
  }

  /**
   * Request access to NETWORK_STATE.
   */
  override fun requestPermission() {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_NETWORK_STATE), PERMISSIONS_REQUEST)
  }

  /**
   * On Permission Result: Keep asking if not granted.
   *
   * @TODO handle a not granted permission and workaround without that.
   */
  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    when (requestCode) {
      PERMISSIONS_REQUEST -> {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          permissionGranted()
        } else {
          permissionNotGranted()
        }
        return
      }
    }
  }

  companion object {
    private const val PERMISSIONS_REQUEST = 0x01
  }
}