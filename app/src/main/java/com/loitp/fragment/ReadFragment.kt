package com.loitp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.loitp.R

class ReadFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frm_chap, container, false)
    }

    companion object {
        fun newInstance(): ReadFragment {
            return ReadFragment()
        }
    }
}