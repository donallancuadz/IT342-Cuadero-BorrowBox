package com.example.borrowbox.features.auth

import com.example.borrowbox.features.dashboard.DashboardActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.borrowbox.R
import com.example.borrowbox.shared.api.ApiClient
import com.example.borrowbox.shared.model.LoginRequest
import com.example.borrowbox.shared.model.LoginResponse
import com.example.borrowbox.shared.storage.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail    = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin   = findViewById<Button>(R.id.btnLogin)
        val tvError    = findViewById<TextView>(R.id.tvError)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        val tokenManager = TokenManager(this)

        // If already logged in, skip to dashboard
        if (tokenManager.getToken() != null) {
            goToDashboard()
            return
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            tvError.visibility = View.GONE

            if (email.isEmpty() || password.isEmpty()) {
                showError(tvError, "Please fill in all fields.")
                return@setOnClickListener
            }

            btnLogin.isEnabled = false
            btnLogin.text = "Signing in..."

            ApiClient.apiService.login(LoginRequest(email, password))
                .enqueue(object : Callback<LoginResponse> {

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        btnLogin.isEnabled = true
                        btnLogin.text = "LOGIN"

                        if (response.isSuccessful && response.body() != null) {
                            tokenManager.saveToken(response.body()!!.token)
                            goToDashboard()
                        } else {
                            showError(tvError, "Invalid email or password.")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        btnLogin.isEnabled = true
                        btnLogin.text = "LOGIN"
                        showError(tvError, "Connection error. Is the server running?")
                    }
                })
        }
    }

    private fun showError(tv: TextView, message: String) {
        tv.text = message
        tv.visibility = View.VISIBLE
    }

    private fun goToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}