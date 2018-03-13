package com.lucasmontano.jakewharton.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucasmontano.jakewharton.R
import com.lucasmontano.jakewharton.view.interfaces.RequestPermissionView
import kotlinx.android.synthetic.main.fragment_no_permission.button_allow

class NoPermissionFragment : Fragment() {

    private var mListener: RequestPermissionView? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_allow.setOnClickListener { mListener?.requestPermission() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_no_permission, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RequestPermissionView) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement RequestPermissionView")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    companion object {

        fun newInstance(): NoPermissionFragment {
            return  NoPermissionFragment()
        }
    }
}
