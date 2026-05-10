package com.example.borrowbox.features.dashboard

import com.example.borrowbox.features.auth.LoginActivity
import com.example.borrowbox.features.items.ItemsActivity
import com.example.borrowbox.features.requests.RequestsActivity
import com.example.borrowbox.features.profile.ProfileActivity
import com.example.borrowbox.features.requests.RecentRequestAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.borrowbox.R
import com.example.borrowbox.shared.api.ApiClient
import com.example.borrowbox.shared.model.DashboardStats
import com.example.borrowbox.shared.model.MeResponse
import com.example.borrowbox.shared.storage.TokenManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        tokenManager = TokenManager(this)
        val savedToken = tokenManager.getToken()

        if (savedToken == null) {
            redirectToLogin()
            return
        }
        token = savedToken

        loadUserInfo()
        loadDashboardStats()
        setupBottomNav()
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_dashboard
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> true // already here
                R.id.nav_items -> {
                    startActivity(Intent(this, ItemsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_requests -> {
                    startActivity(Intent(this, RequestsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadUserInfo() {
        ApiClient.apiService.getMe("Bearer $token")
            .enqueue(object : Callback<MeResponse> {
                override fun onResponse(call: Call<MeResponse>, response: Response<MeResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()!!
                        findViewById<TextView>(R.id.tvFullName).text = user.fullName
                        findViewById<TextView>(R.id.tvEmail).text = user.email
                        val initials = user.fullName
                            .split(" ")
                            .mapNotNull { it.firstOrNull()?.toString() }
                            .take(2)
                            .joinToString("")
                            .uppercase()
                        findViewById<TextView>(R.id.tvAvatar).text = initials
                    }
                }
                override fun onFailure(call: Call<MeResponse>, t: Throwable) {}
            })
    }

    private fun loadDashboardStats() {
        ApiClient.apiService.getDashboard("Bearer $token")
            .enqueue(object : Callback<DashboardStats> {
                override fun onResponse(call: Call<DashboardStats>, response: Response<DashboardStats>) {
                    if (response.isSuccessful && response.body() != null) {
                        val stats = response.body()!!
                        bindStats(stats)
                    } else {
                        Toast.makeText(this@DashboardActivity,
                            "Failed to load dashboard: ${response.code()}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<DashboardStats>, t: Throwable) {
                    Toast.makeText(this@DashboardActivity,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun bindStats(stats: DashboardStats) {
        findViewById<TextView>(R.id.tvActiveBorrows).text = stats.activeBorrows.toString()
        findViewById<TextView>(R.id.tvPendingRequests).text = stats.pendingRequests.toString()
        findViewById<TextView>(R.id.tvReturnedItems).text = stats.returnedItems.toString()

        val rv = findViewById<RecyclerView>(R.id.rvRecentRequests)
        val tvEmpty = findViewById<TextView>(R.id.tvNoRequests)

        if (stats.recentRequests.isEmpty()) {
            rv.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        } else {
            rv.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            rv.layoutManager = LinearLayoutManager(this)
            rv.adapter = RecentRequestAdapter(stats.recentRequests)
        }
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}