package com.example.borrowbox.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.borrowbox.R
import com.example.borrowbox.api.ApiClient
import com.example.borrowbox.model.LoginRequest
import com.example.borrowbox.model.LoginResponse
import com.example.borrowbox.storage.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        val tokenManager = TokenManager(this)

        btnLogin.setOnClickListener {

            val request = LoginRequest(
                email.text.toString(),
                password.text.toString()
            )

            ApiClient.apiService.login(request)
                .enqueue(object : Callback<LoginResponse> {

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val loginResponse = response.body()!!

                            tokenManager.saveToken(loginResponse.token)

                            Toast.makeText(
                                this@LoginActivity,
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(
                                Intent(this@LoginActivity, DashboardActivity::class.java)
                            )
                            finish()

                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Login failed: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }
}