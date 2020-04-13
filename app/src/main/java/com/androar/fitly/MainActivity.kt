package com.androar.fitly

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.provider.CallLog.Calls.*
import android.provider.ContactsContract
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androar.fitly.agora.Constants
import com.androar.fitly.agora.DialerActivity
import com.androar.fitly.utils.RtcUtils
import io.agora.rtm.ErrorInfo
import io.agora.rtm.ResultCallback
import io.agora.rtm.RtmClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseCallActivity() {

    private val PERMISSION_REQ_ID = 27
    private val REQUESTED_PERMISSIONS =
        arrayOf<String>(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS)
    private val LOG_TAG = MainActivity::class.java.simpleName
    var selectUsers: ArrayList<ContactsContract.Contacts>? = null
    var phones: Cursor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadDummyData()
        populateActivities();

        if (checkSelfPermission(
                REQUESTED_PERMISSIONS[0],
                PERMISSION_REQ_ID
            ) && (checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID))
        ) {
            val rvContacts = findViewById(R.id.rvPhoneList) as RecyclerView
            rvContacts.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            showContacts();
        }

        val bg = findViewById<TextView>(R.id.tvBG)
        //startCall()

            startActivity(Intent(this,DialerActivity::class.java))


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
                } else {
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
        val phones = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )
        var name: String = "Your friend"
        while (phones!!.moveToNext()) {
            val phoneNumber = phones.getString(phones.getColumnIndex(CallLog.Calls.NUMBER))
            try {
                name = phones.getString(phones.getColumnIndex(CallLog.Calls.CACHED_NAME))
            } catch (e: Exception) {
                Log.e("Phone log error", e.message.toString())
                name = "Your friend"
            }


            val contactModel = PhoneListClass(name, phoneNumber)
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

    fun contactExists(number: String?): Boolean {
        /// number is the phone number
        val lookupUri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        val mPhoneNumberProjection =
            arrayOf<String>(
                ContactsContract.PhoneLookup._ID,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.DISPLAY_NAME
            )
        val cur: Cursor? =
            contentResolver.query(lookupUri, mPhoneNumberProjection, null, null, null)
        try {
            if (cur!!.moveToFirst()) {
                return true
            }
        } finally {
            cur?.close()
        }
        return false
    }


    private fun populateActivities() {
        val recyclerView = findViewById(R.id.rvActivities) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)


        val arrayList = ArrayList<RecyclerItemActivities>()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://goforbg.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)
        val call = api.getActivities()
        call.enqueue(object : Callback<List<RecyclerItemActivities>> {
            override fun onResponse(
                call: Call<List<RecyclerItemActivities>>,
                response: Response<List<RecyclerItemActivities>>
            ) {
                val activitesList = response.body()!!
                for (i in activitesList.indices) {
                    arrayList.add(
                        RecyclerItemActivities(
                            activitesList[i].n,
                            activitesList[i].t,
                            activitesList[i].i,
                            activitesList[i].e,
                            activitesList[i].v,
                            activitesList[i].d
                        )
                    )
                }
                val adapter = RecyclerItemActivitesAdapter(arrayList)
                recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<List<RecyclerItemActivities>>, t: Throwable) {
                Log.d("RetrofitTest", t.toString())

                Toast.makeText(applicationContext, "Your internet is moo!", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }

    fun loadDummyData() {
        val recyclerView = findViewById(R.id.rvActivities) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val arrayList = ArrayList<RecyclerItemActivities>()
        arrayList.add(RecyclerItemActivities("", "", "", "", "", ""))
        arrayList.add(RecyclerItemActivities("", "", "", "", "", ""))
        arrayList.add(RecyclerItemActivities("", "", "", "", "", ""))
        val adapter = RecyclerItemActivitesAdapter(arrayList)
        recyclerView.adapter = adapter
    }


    private fun startCall() {
        val number: Int = 2222
        val peer = number.toString()
        val peerSet: MutableSet<String> =
            HashSet()
        peerSet.add(peer)

        rtmClient()!!.queryPeersOnlineStatus(peerSet,
            object :
                ResultCallback<Map<String?, Boolean?>> {
                override fun onSuccess(statusMap: Map<String?, Boolean?>) {
                    val bOnline = statusMap[peer]
                    if (bOnline != null && bOnline) {
                        val uid: String =
                            java.lang.String.valueOf(application().config().getUserId())
                        val channel = RtcUtils.channelName(uid, peer)
                        gotoCallingInterface(peer, channel, Constants.ROLE_CALLER)
                    } else {
                      Log.d("Big", "Provlem")
                    }
                }

                override fun onFailure(errorInfo: ErrorInfo) {
                    Log.d("Biggest", "Provlem")
                }
            })
    }


    override fun rtmClient(): RtmClient? {
        return application().rtmClient()
    }
}
