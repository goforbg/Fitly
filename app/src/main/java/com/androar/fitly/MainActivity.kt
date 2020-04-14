package com.androar.fitly

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Boolean.getBoolean


class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQ_ID = 27
    private val REQUESTED_PERMISSIONS =
        arrayOf<String>(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS)
    private val LOG_TAG = MainActivity::class.java.simpleName
    var selectUsers: ArrayList<ContactsContract.Contacts>? = null
    var phones: Cursor? = null
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAHDRmp7s:APA91bGx8ODUNDSSlCEtCObiYsbXT5DJFQ2f9xVwC-ENiPahArDtyxGDJGEY1bgKMQlLHDTu-hWwWMSzhNmHgLMg31VTRXtHkupNr3f9xRgl4NwH5ssG_NaqXJmoYGFeIlK3N-xdq_AA"
    private val contentType = "application/json"

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            com.android.volley.Response.Listener<JSONObject> { response ->
                Log.i("TAG", "onResponse: $response")
            },
            com.android.volley.Response.ErrorListener {
                Toast.makeText(this@MainActivity, "Request error", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onErrorResponse: Didn't work")
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadDummyData()
        populateActivities();
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/bg")

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
        bg.setOnClickListener {
            val intent = Intent(this, VideoCallActivity::class.java)
            intent.putExtra("roomID", "test")
            startActivity(intent)

            //Sending notification
//            val topic = "/topics/bg" //topic has to match what the receiver subscribed to
//            val notification = JSONObject()
//            val notifcationBody = JSONObject()
//            try {
//                notifcationBody.put("title", "Enter_title")
//                notifcationBody.put("message", "test")   //Enter your notification message
//                notification.put("to", topic)
//                notification.put("roomID","test")
//                notification.put("data", notifcationBody)
//                Log.e("TAG", "try")
//            } catch (e: JSONException) {
//                Log.e("TAG", "onCreate: " + e.message)
//            }
//
//            sendNotification(notification)
        }


        val onboardingSeen = AppPreferences(this).getBoolean(getString(com.androar.fitly.R.string.onboarding_seen), false)
        if (!onboardingSeen) {
            startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
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


}
