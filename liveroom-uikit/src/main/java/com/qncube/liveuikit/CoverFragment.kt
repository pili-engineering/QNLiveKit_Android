package com.qncube.liveuikit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.qncube.liveroomcore.QLiveClient
import com.qncube.uikitcore.KitContext
import kotlinx.android.synthetic.main.kit_fragment_cover.*

class CoverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.kit_fragment_cover, container, false)
    }
}