package com.crossapps.petpal.Login


import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.google.gson.Gson
import com.crossapps.petpal.MainActivity
import com.crossapps.petpal.R
import com.crossapps.petpal.Register.Register
import com.crossapps.petpal.Server.Request.LoginRequest
import com.crossapps.petpal.Server.Response.LoginResponse
import com.crossapps.petpal.Server.TCApi
import com.crossapps.petpal.Util.*
import com.webpulse.trafficcontrol.Server.RetrofitAPI
import kotlinx.android.synthetic.main.activity_email_login.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    var appCompatActivity:AppCompatActivity?=this
    var utilityofActivity:UtilityofActivity?=null
    var helper: Helper? = null
    var flag = true
    var loginResponse: LoginResponse? = null
    var tcApi: TCApi? = null
    var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_email_login)

        context = this

        tcApi = RetrofitAPI.client.create<TCApi>(TCApi::class.java)

        utilityofActivity = UtilityofActivity(appCompatActivity!!)

        login_email.setOnClickListener {
            if (validate(editTextEmail.text.toString(), editTextPassword.text.toString())) {
                loginUser(editTextEmail.text.toString(), editTextPassword.text.toString())
            }
        }

        register.setOnClickListener {
            startActivity(Intent(appCompatActivity, Register::class.java))
        }



    }

    fun validate(emailText: String, passwordText: String): Boolean {


        flag = true

        if (TextUtils.isEmpty(emailText)) {
            editTextEmail.error = "Enter Email"
            flag = false
        }
        if (TextUtils.isEmpty(passwordText)) {
            editTextPassword.error = "Enter Password"
            flag = false
        }

        return flag

    }

    private fun loginUser(emailText: String, passwordText: String) {

        utilityofActivity!!.showProgressDialog()

        val loginRequest = LoginRequest(emailText, passwordText)
        val call = tcApi?.callLoginApi(loginRequest)

        call?.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: retrofit2.Call<LoginResponse>, response: Response<LoginResponse>) {
                utilityofActivity!!.dismissProgressDialog()
                try {
                    loginResponse = response.body()


                    if (loginResponse!!.success) {


//                        if (loginResponse!!.data.isVerified == "1") {
                            PrefernceFile.getInstance(context!!).setString(Constant.isLogin, "true")
                            PrefernceFile.getInstance(context!!).setString(Constant.PREF_KEY_USER_DATA, (Gson().toJson(loginResponse!!.data)))
                            val intent = Intent(this@Login, MainActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
//                        } else {
//                            val bundle = Bundle()
//                            bundle.putString("userId", loginResponse!!.data.userId)
//                            val intent = Intent(this@Login, VerifyOTP::class.java)
//                            intent.putExtras(bundle)
//                            startActivity(intent)
//                        }
                    } else {
                        utilityofActivity!!.toast(loginResponse!!.message)
                    }
                } catch (e: Exception) {
                    utilityofActivity!!.toast("Something went wrong, Please try again.")
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                call.cancel()
                Logger.e("Error",t.localizedMessage)
                utilityofActivity!!.dismissProgressDialog()
                utilityofActivity!!.toast("Failed, Please Check Your Internet Connection")
            }
        })
    }




}
