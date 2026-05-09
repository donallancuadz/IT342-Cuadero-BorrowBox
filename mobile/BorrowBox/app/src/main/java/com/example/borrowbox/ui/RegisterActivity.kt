package com.example.borrowbox.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

        val etFullName        = findViewById<EditText>(R.id.etFullName)
        val etStudentId       = findViewById<EditText>(R.id.etStudentId)
        val etEmail           = findViewById<EditText>(R.id.etEmail)
        val etPassword        = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister       = findViewById<Button>(R.id.btnRegister)
        val tvError           = findViewById<TextView>(R.id.tvError)
        val tvLogin           = findViewById<TextView>(R.id.tvLogin)

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            val fullName        = etFullName.text.toString().trim()
            val studentId       = etStudentId.text.toString().trim()
            val email           = etEmail.text.toString().trim()
            val password        = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            tvError.visibility = View.GONE

            // Validation
            if (fullName.isEmpty() || studentId.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showError(tvError, "Please fill in all fields.")
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                showError(tvError, "Passwords do not match.")
                return@setOnClickListener
            }
            if (password.length < 6) {
                showError(tvError, "Password must be at least 6 characters.")
                return@setOnClickListener
            }

            btnRegister.isEnabled = false
            btnRegister.text = "Creating account..."

            ApiClient.apiService.register(
                RegisterRequest(fullName, email, password, confirmPassword, studentId)
            ).enqueue(object : Callback<RegisterResponse> {

                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    btnRegister.isEnabled = true
                    btnRegister.text = "CREATE ACCOUNT"

                    if (response.isSuccessful) {
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                            putExtra("registered", true)
                        })
                        finish()
                    } else {
                        showError(tvError, "Registration failed. Email may already be in use.")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    btnRegister.isEnabled = true
                    btnRegister.text = "CREATE ACCOUNT"
                    showError(tvError, "Connection error. Is the server running?")
                }
            })
        }
    }

    private fun showError(tv: TextView, message: String) {
        tv.text = message
        tv.visibility = View.VISIBLE
    }
}