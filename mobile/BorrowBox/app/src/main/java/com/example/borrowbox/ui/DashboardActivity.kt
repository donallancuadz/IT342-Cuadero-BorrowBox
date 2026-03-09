package com.example.borrowbox.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.borrowbox.R
import com.example.borrowbox.api.ApiClient
import com.example.borrowbox.model.MeResponse
import com.example.borrowbox.storage.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tvUserInfo = findViewById<TextView>(R.id.tvUserInfo)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val tokenManager = TokenManager(this)
        val token = tokenManager.getToken()

        if (token == null) {
            Toast.makeText(this, "No token found. Please login.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        ApiClient.apiService.getMe("Bearer $token")
            .enqueue(object : Callback<MeResponse> {

                override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()!!
                        tvUserInfo.text = """
                            ID: ${user.id}
                            Name: ${user.fullName}
                            Email: ${user.email}
                        """.trimIndent()
                    } else {
                        Toast.makeText(
                            this@DashboardActivity,
                            "Failed to load profile: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<MeResponse>, t: Throwable) {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        btnLogout.setOnClickListener {
            tokenManager.clearToken()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}