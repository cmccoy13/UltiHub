package com.example.christopher.ultihub

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class LiveGameFragment : Fragment() {

    companion object {
        fun newInstance(): LiveGameFragment {
            return LiveGameFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_live_game, container, false)
    }
}