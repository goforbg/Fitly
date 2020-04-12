package com.androar.fitly

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Movie
import android.os.Bundle
import android.provider.CallLog
import android.provider.CallLog.Calls.*
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQ_ID = 27
    private val REQUESTED_PERMISSIONS = arrayOf<String>(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS)
    private val LOG_TAG = MainActivity::class.java.simpleName
    var selectUsers: ArrayList<ContactsContract.Contacts>? = null
    var phones: Cursor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadDummyData()
        populateActivities();

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
            override fun onResponse(call: Call<List<RecyclerItemActivities>>, response: Response<List<RecyclerItemActivities>>) {
                val activitesList = response.body()!!
                for (i in activitesList.indices) {
                    arrayList.add(RecyclerItemActivities(activitesList[i].n, activitesList[i].t, activitesList[i].i, activitesList[i].e, activitesList[i].v, activitesList[i].d))
                }
                val adapter = RecyclerItemActivitesAdapter(arrayList)
                recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<List<RecyclerItemActivities>>, t: Throwable) {
                Log.d("RetrofitTest", t.toString())

                Toast.makeText(applicationContext, "Your internet is moo!", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun loadDummyData() {
        val recyclerView = findViewById(R.id.rvActivities) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val arrayList = ArrayList<RecyclerItemActivities>()
        arrayList.add(RecyclerItemActivities("","","","","",""))
        arrayList.add(RecyclerItemActivities("","","","","",""))
        arrayList.add(RecyclerItemActivities("","","","","",""))
        val adapter = RecyclerItemActivitesAdapter(arrayList)
        recyclerView.adapter = adapter
    }


}
