package com.example.borrowbox.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.borrowbox.R
import com.example.borrowbox.api.ApiClient
import com.example.borrowbox.model.MeResponse
import com.example.borrowbox.storage.TokenManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var token: String

    private lateinit var tvAvatar: TextView
    private lateinit var tvFullName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvInfoName: TextView
    private lateinit var tvInfoEmail: TextView

    private var currentFullName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tokenManager = TokenManager(this)
        val savedToken = tokenManager.getToken()
        if (savedToken == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        token = savedToken

        tvAvatar    = findViewById(R.id.tvAvatar)
        tvFullName  = findViewById(R.id.tvFullName)
        tvEmail     = findViewById(R.id.tvEmail)
        tvInfoName  = findViewById(R.id.tvInfoName)
        tvInfoEmail = findViewById(R.id.tvInfoEmail)

        setupBottomNav()
        loadProfile()

        // Edit Profile button → open bottom sheet
        findViewById<Button>(R.id.btnEditProfile).setOnClickListener {
            val sheet = EditProfileBottomSheet.newInstance(currentFullName)
            sheet.onProfileUpdated = { newName, newEmail ->
                // Update UI immediately without re-fetching
                bindProfile(newName, newEmail)
            }
            sheet.show(supportFragmentManager, "EditProfile")
        }

        // Logout
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            tokenManager.clearToken()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadProfile() {
        ApiClient.apiService.getMe("Bearer $token")
            .enqueue(object : Callback<MeResponse> {
                override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()!!
                        bindProfile(user.fullName, user.email)
                    }
                }
                override fun onFailure(call: Call<MeResponse>, t: Throwable) {}
            })
    }

    private fun bindProfile(fullName: String, email: String) {
        currentFullName = fullName
        val initials = fullName
            .split(" ")
            .mapNotNull { it.firstOrNull()?.toString() }
            .take(2)
            .joinToString("")
            .uppercase()

        tvAvatar.text    = initials
        tvFullName.text  = fullName
        tvEmail.text     = email
        tvInfoName.text  = fullName
        tvInfoEmail.text = email
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_profile
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish(); true
                }
                R.id.nav_items -> {
                    startActivity(Intent(this, ItemsActivity::class.java))
                    finish(); true
                }
                R.id.nav_requests -> {
                    startActivity(Intent(this, RequestsActivity::class.java))
                    finish(); true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }
    }
}