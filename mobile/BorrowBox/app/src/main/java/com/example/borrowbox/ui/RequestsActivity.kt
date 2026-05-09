package com.example.borrowbox.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.borrowbox.R
import com.example.borrowbox.api.ApiClient
import com.example.borrowbox.model.BorrowRequestResponse
import com.example.borrowbox.storage.TokenManager
import com.example.borrowbox.ui.adapter.RequestsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestsActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)

        tokenManager = TokenManager(this)
        val savedToken = tokenManager.getToken()
        if (savedToken == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        token = savedToken

        setupBottomNav()
        loadRequests()
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_requests
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_items -> {
                    startActivity(Intent(this, ItemsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_requests -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadRequests() {
        ApiClient.apiService.getMyRequests("Bearer $token")
            .enqueue(object : Callback<List<BorrowRequestResponse>> {
                override fun onResponse(
                    call: Call<List<BorrowRequestResponse>>,
                    response: Response<List<BorrowRequestResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val requests = response.body()!!
                        val rv = findViewById<RecyclerView>(R.id.rvRequests)
                        val tvEmpty = findViewById<TextView>(R.id.tvNoRequests)

                        if (requests.isEmpty()) {
                            rv.visibility = View.GONE
                            tvEmpty.visibility = View.VISIBLE
                        } else {
                            rv.visibility = View.VISIBLE
                            tvEmpty.visibility = View.GONE
                            rv.layoutManager = LinearLayoutManager(this@RequestsActivity)
                            rv.adapter = RequestsAdapter(requests)
                        }
                    } else {
                        Toast.makeText(this@RequestsActivity,
                            "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<BorrowRequestResponse>>, t: Throwable) {
                    Toast.makeText(this@RequestsActivity,
                        "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}