package com.example.counter_app

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.lang.reflect.Method

class MainActivity() : AppCompatActivity(), Parcelable {

    var url = "https://apiopenai.com/v1/images/generations"
    lateinit var queryTV: TextView
    lateinit var imageView: ImageView
    lateinit var queryEdt: TextInputEditText
    lateinit var loadingPB: ProgressBar
    lateinit var noDataRL: RelativeLayout
    lateinit var dataRL: RelativeLayout

    constructor(parcel: Parcel) : this() {
        url = parcel.readString().toString()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        queryTV = findViewById(R.id.idTVQuery)
        imageView = findViewById(R.id.idTVImage)
        queryEdt = findViewById(R.id.idEdtQuery)
        loadingPB = findViewById(R.id.idPBLoading)
        noDataRL = findViewById(R.id.idNoDataLayout)
        dataRL = findViewById(R.id.idRLData)



        queryEdt.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEND) {
                if (queryEdt.text.toString().isNotEmpty()) {
                    queryTV.text = queryEdt.text.toString()
                    getReponse(queryEdt.text.toString())
                } else {
                    Toast.makeText(
                        applicationContext,
                        "please enter your query",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@setOnEditorActionListener true
            }
            false


        }
    }

    private fun getReponse(query: String) {
        queryEdt.setText("")
        loadingPB.visibility = View.VISIBLE

        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val jsonObject: JSONObject? = JSONObject()
        jsonObject?.put("prompt", query)
        jsonObject?.put("n", 1)
        jsonObject?.put("size", "256x256")
        val postRequest: JsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, jsonObject, Response.Listener { response ->

                noDataRL.visibility = View.GONE
                dataRL.visibility = View.VISIBLE
            },
                Response.ErrorListener { error ->
                    Toast.makeText(
                        applicationContext,
                        "fail to generate image.. ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Content-Type"] = "application/json"
                    params["Authorization"] = "Bearer nfsnf"
                    return params
                }
            }
        postRequest.setRetryPolicy(object : RetryPolicy {

            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            override fun retry(error: VolleyError?) {

            }

        })
        queue.add(postRequest)

    }



