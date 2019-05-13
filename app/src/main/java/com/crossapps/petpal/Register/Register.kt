package com.crossapps.petpal.Register


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import com.crossapps.petpal.MainActivity
import com.crossapps.petpal.R
import com.crossapps.petpal.Server.Request.RegisterRequest
import com.crossapps.petpal.Server.Response.RegisterResponse
import com.crossapps.petpal.Server.TCApi
import com.crossapps.petpal.Util.*
import com.google.gson.Gson
import com.webpulse.trafficcontrol.Server.RetrofitAPI
import kotlinx.android.synthetic.main.activity_email_login.editTextEmail
import kotlinx.android.synthetic.main.activity_email_login.editTextPassword
import kotlinx.android.synthetic.main.activity_email_login.register
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {

    var appCompatActivity: AppCompatActivity? = this
    var utilityofActivity: UtilityofActivity? = null
    var helper: Helper? = null
    var tcApi: TCApi? = null
    var context: Context? = null
    var registerResponse: RegisterResponse? = null
    var flag = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_register)


        context = this

        tcApi = RetrofitAPI.client.create<TCApi>(TCApi::class.java)

        utilityofActivity = UtilityofActivity(appCompatActivity!!)

        register.setOnClickListener {

            if (validate(
                    editTextName.text.toString(),
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString(),
                    mobile.text.toString()
                )
            ) {
                registerUser(
                    editTextName.text.toString(),
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString(),
                    mobile.text.toString()
                )
            }


        }


    }


    fun validate(nameText:String,emailText: String, passwordText: String, mobileNoText: String): Boolean {


        flag = true

        if (TextUtils.isEmpty(nameText)) {
            editTextEmail.error = "Enter Name"
            flag = false
        }
        if (TextUtils.isEmpty(emailText)) {
            editTextEmail.error = "Enter Email"
            flag = false
        }
        if (TextUtils.isEmpty(passwordText)) {
            editTextPassword.error = "Enter Password"
            flag = false
        }
        if (TextUtils.isEmpty(mobileNoText)) {
            mobile.error = "Enter Mobile No"
            flag = false
        }


        return flag

    }


    private fun registerUser(
        nameText: String,
        emailText: String,
        passwordText: String,
        mobileNoText: String
    ) {

        utilityofActivity!!.showProgressDialog()

        val registerRequest =
            RegisterRequest(nameText,emailText, passwordText, mobileNoText)
        val call = tcApi?.callRegisterApi(registerRequest)


        call?.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: retrofit2.Call<RegisterResponse>, response: Response<RegisterResponse>) {
                utilityofActivity!!.dismissProgressDialog()
                try {

                    registerResponse = response.body()

                    if (registerResponse!!.success) {


                        PrefernceFile.getInstance(context!!).setString(Constant.isLogin, "true")
                        PrefernceFile.getInstance(context!!).setString(Constant.PREF_KEY_USER_DATA, (Gson().toJson(registerResponse!!.data)))
                        val intent = Intent(this@Register, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()

                    } else {
                        utilityofActivity!!.toast(registerResponse!!.message)
                    }
                } catch (e: java.lang.Exception) {
                    utilityofActivity!!.toast("Something went wrong, Please try again.")
                }

            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                call.cancel()
                Logger.e("Error: ",t.localizedMessage)
                utilityofActivity!!.dismissProgressDialog()
                utilityofActivity!!.toast("Failed, Please Check Your Internet Connection")
            }
        })
    }


}
