package com.example.mvvmproject.GoogleMapsLocations

import android.content.Context
import android.location.Address
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmproject.R

class LocationRecyclerAdapter(val context: Context, val list: ArrayList<Address>) : RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolderclss>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderclss {
        val view= LayoutInflater.from(context).inflate(R.layout.locationitemlist,parent,false)
        return ViewHolderclss(view)
    }

    override fun onBindViewHolder(holder: ViewHolderclss, position: Int) {

       // holder.titletextview.text=list.get(position).subLocality
        val ob=list.get(position)
        val str=ob.countryName+" \nlocatity "+ob.locality+"\nlatitute "+ob.latitude+"\nlongitude "+ob.longitude+" "+"\nlocality "+ob.locality
        holder.titletextview.text=str
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class ViewHolderclss(view: View) : RecyclerView.ViewHolder(view)
    {
        val titletextview=view.findViewById<TextView>(R.id.locationtitle)

    }
}