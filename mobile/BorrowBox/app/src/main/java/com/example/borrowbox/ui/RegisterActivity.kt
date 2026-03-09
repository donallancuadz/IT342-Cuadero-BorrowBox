package com.example.borrowbox.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.borrowbox.R
import com.example.borrowbox.api.ApiClient
import com.example.borrowbox.model.RegisterRequest
import com.example.borrowbox.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val fullName = findViewById<EditText>(R.id.etFullName)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val request = RegisterRequest(
                fullName.text.toString(),
                email.text.toString(),
                password.text.toString()
            )

            ApiClient.apiService.register(request)
                .enqueue(object : Callback<RegisterResponse> {

                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {

                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registered successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()

                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registration failed: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {

                        Toast.makeText(
                            this@RegisterActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()

                    }

                })
        }
    }
}