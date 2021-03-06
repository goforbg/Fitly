package com.androar.fitly.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androar.fitly.network.Api
import com.androar.fitly.R
import com.androar.fitly.ui.adapter.RecyclerHomepageAdapter
import com.androar.fitly.model.ExcercisesListClass
import com.androar.fitly.model.PhoneListClass
import com.androar.fitly.onboarding.OnboardingActivity
import com.androar.fitly.ui.activity.MainActivity
import com.androar.fitly.util.AppPreferences
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {


    private val PERMISSION_REQ_ID = 27
    private val REQUESTED_PERMISSIONS = arrayOf<String>(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS)
    private val LOG_TAG = MainActivity::class.java.simpleName
    var selectUsers: ArrayList<ContactsContract.Contacts>? = null
    var phones: Cursor? = null
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=" + "AAAAHDRmp7s:APA91bGx8ODUNDSSlCEtCObiYsbXT5DJFQ2f9xVwC-ENiPahArDtyxGDJGEY1bgKMQlLHDTu-hWwWMSzhNmHgLMg31VTRXtHkupNr3f9xRgl4NwH5ssG_NaqXJmoYGFeIlK3N-xdq_AA"
    private val contentType = "application/json"
    var contactModelArrayList = arrayListOf<PhoneListClass>()
    var excercisesArrayList = ArrayList<ExcercisesListClass>()



    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.context)
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            com.android.volley.Response.Listener<JSONObject> { response ->
                Log.i("TAG", "onResponse: $response")
            },
            com.android.volley.Response.ErrorListener {
                Toast.makeText(context!!, "Request error", Toast.LENGTH_LONG).show()
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


    fun checkSelfPermission(permission: String?, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                context!!,
                permission!!
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
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
        val projection = arrayOf(
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION
        )
        val phones = activity!!.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )
        var name: String = "Your friend"
        contactModelArrayList.add(
            PhoneListClass(
                "Your Trainer",
                "OG",
                "contact"
            )
        )

        var count = 0
        while (phones!!.moveToNext() && count<50) {
            val phoneNumber = phones.getString(phones.getColumnIndex(CallLog.Calls.NUMBER))
            try {
                name = phones.getString(phones.getColumnIndex(CallLog.Calls.CACHED_NAME))
            } catch (e: Exception) {
                Log.e("Phone log error", e.message.toString())
                name = "Your friend"
            }
            val contactModel = PhoneListClass(
                name,
                phoneNumber,
                "contact"
            )
            if (!contactModelArrayList!!.contains(contactModel) && !name.equals("")) {
                contactModelArrayList!!.add(contactModel)
            }
            count++
        }
        phones.close()
        val rvContacts = view!!.findViewById(R.id.rvPhoneList) as RecyclerView
        val recyclerHomepageAdapter =
            RecyclerHomepageAdapter(
                activity!!,
                contactModelArrayList!!,
                excercisesArrayList
            )
        rvContacts!!.adapter = recyclerHomepageAdapter
        populateActivities(recyclerHomepageAdapter)


    }

    private fun populateActivities(adapter : RecyclerHomepageAdapter) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://goforbg.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)
        val call = api.getActivities()
        call.enqueue(object : Callback<List<ExcercisesListClass>> {
            override fun onResponse(
                call: Call<List<ExcercisesListClass>>,
                response: Response<List<ExcercisesListClass>>
            ) {
                val excercisesList = response.body()!!
                for (i in excercisesList.indices) {
                    excercisesArrayList.add(
                        ExcercisesListClass(
                            excercisesList[i].n,
                            excercisesList[i].t,
                            excercisesList[i].i,
                            excercisesList[i].e,
                            excercisesList[i].v,
                            excercisesList[i].d
                        )
                    )
                }
                contactModelArrayList.add(0,
                    PhoneListClass(
                        "video",
                        "video",
                        "video"
                    )
                )
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<ExcercisesListClass>>, t: Throwable) {
                Log.d("RetrofitTest", t.toString())

                Toast.makeText(context, "Your internet is moo!", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //loadDummyData()
        FirebaseApp.initializeApp(context!!);
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/bg")

        if (checkSelfPermission(
                REQUESTED_PERMISSIONS[0],
                PERMISSION_REQ_ID
            ) && (checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID))
        ) {
            val rvContacts = view!!.findViewById(R.id.rvPhoneList) as RecyclerView
            rvContacts.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            showContacts();
        }

        //Sending notification using Firebase
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

        val onboardingSeen = AppPreferences(activity)
            .getBoolean(getString(R.string.onboarding_seen), false)
        if (!onboardingSeen) {
            startActivity(Intent(activity, OnboardingActivity::class.java))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        return view
    }

}
