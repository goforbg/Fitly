package com.androar.fitly

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.provider.CallLog.Calls.*
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQ_ID = 27
    private val REQUESTED_PERMISSIONS = arrayOf<String>(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS)
    private val LOG_TAG = MainActivity::class.java.simpleName
    var selectUsers: ArrayList<ContactsContract.Contacts>? = null
    var phones: Cursor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById(R.id.rvActivities) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val activites = ArrayList<RecyclerItemActivities>()
        //adding some dummy data to the list
        activites.add(RecyclerItemActivities("Belal Khan", "Ranchi Jharkhand"))
        activites.add(RecyclerItemActivities("Ramiz Khan", "Ranchi Jharkhand"))
        activites.add(RecyclerItemActivities("Faiz Khan", "Ranchi Jharkhand"))
        activites.add(RecyclerItemActivities("Yashar Khan", "Ranchi Jharkhand"))
        val adapter = RecyclerItemActivitesAdapter(activites)
        recyclerView.adapter = adapter

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) && (checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID))) {
            val rvContacts = findViewById(R.id.rvPhoneList) as RecyclerView
            rvContacts.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

            showContacts();
        }


    }

    fun checkSelfPermission(permission: String?, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission!!
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                REQUESTED_PERMISSIONS,
                requestCode
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>, grantResults: IntArray
    ) {
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode)
        when (requestCode) {
            PERMISSION_REQ_ID -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(
                        LOG_TAG,
                        "Need permissions " + Manifest.permission.RECORD_AUDIO.toString() + "/" + Manifest.permission.CAMERA
                    )
                }
                else {
                    Log.i(LOG_TAG, "Permission has been granted by user")
                }
                // if permission granted, initialize the engine
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun showContacts() {
        var contactModelArrayList = ArrayList<PhoneListClass>()
        val projection = arrayOf(CACHED_NAME, NUMBER, TYPE, DATE, DURATION)
        val phones = contentResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DATE + " DESC")
        while (phones!!.moveToNext()) {
            val name =  phones.getString(phones.getColumnIndex(CallLog.Calls.CACHED_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(CallLog.Calls.NUMBER))
            val contactModel = PhoneListClass(name , phoneNumber)
            if (!contactModelArrayList!!.contains(contactModel) && !name.equals("")) {
                contactModelArrayList!!.add(contactModel)
            }
            Log.d("name>>", name + "  " + phoneNumber)
        }
        phones.close()
        val rvContacts = findViewById(R.id.rvPhoneList) as RecyclerView
        val customAdapter = RecyclerPhoneListAdapter(contactModelArrayList!!)
        rvContacts!!.adapter = customAdapter

    }


}
