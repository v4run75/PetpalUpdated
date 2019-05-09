package com.crossapps.petpal

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        text.setOnClickListener {
//            Navigation.createNavigateOnClickListener(R.id.profileFragment, null)
//        }

        text.setOnClickListener { view1 ->
            Navigation.findNavController(view1).navigate(R.id.profileFragment)
        }
    }
}