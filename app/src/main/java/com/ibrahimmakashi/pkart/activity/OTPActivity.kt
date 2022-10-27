package com.ibrahimmakashi.pkart.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.ibrahimmakashi.pkart.MainActivity
import com.ibrahimmakashi.pkart.R
import com.ibrahimmakashi.pkart.databinding.ActivityOtpactivityBinding

class OTPActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOtpactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.button3.setOnClickListener {
            if (binding.userOTP.text!!.isEmpty())
                Toast.makeText(this, "Please provide OTP", Toast.LENGTH_SHORT).show()
            else{
                if(binding.userOTP.length() ==6) {
                    verifyUser(binding.userOTP.text.toString())
                }else{
                    Toast.makeText(this, "Please fill complete 6 digits of OTP", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private lateinit var builder : AlertDialog
    private fun verifyUser(otp: String) {
         builder = AlertDialog.Builder(this)
            .setTitle("Loading...")
            .setMessage("Please wait")
            .setCancelable(false)
            .create()
        builder.show()

        val credential = PhoneAuthProvider.getCredential(
            intent.getStringExtra("verificationId")!!, otp)

        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                  /*  val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
                    val editor = preferences.edit()

                    editor.putString("number", intent.getStringExtra("number")!!)
                    editor.apply()
                    */
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
    }

}