package com.example.borrowbox.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.borrowbox.R
import com.example.borrowbox.api.ApiClient
import com.example.borrowbox.model.BorrowRequestCreateRequest
import com.example.borrowbox.model.BorrowRequestResponse
import com.example.borrowbox.model.Item
import com.example.borrowbox.storage.TokenManager
import com.example.borrowbox.ui.adapter.ItemsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemsActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager
    private lateinit var token: String
    private lateinit var adapter: ItemsAdapter
    private var allItems: List<Item> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        tokenManager = TokenManager(this)
        val savedToken = tokenManager.getToken()
        if (savedToken == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        token = savedToken

        setupRecyclerView()
        setupSearch()
        setupBottomNav()
        loadItems()
    }

    private fun setupRecyclerView() {
        val rv = findViewById<RecyclerView>(R.id.rvItems)
        adapter = ItemsAdapter(emptyList()) { item -> requestBorrow(item) }
        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = adapter
    }

    private fun setupSearch() {
        findViewById<EditText>(R.id.etSearch).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString(), allItems)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_items
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_items -> true
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

    private fun loadItems() {
        ApiClient.apiService.getAllItems("Bearer $token")
            .enqueue(object : Callback<List<Item>> {
                override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                    if (response.isSuccessful && response.body() != null) {
                        allItems = response.body()!!
                        adapter.filter("", allItems)
                        val tvEmpty = findViewById<TextView>(R.id.tvNoItems)
                        val rv = findViewById<RecyclerView>(R.id.rvItems)
                        if (allItems.isEmpty()) {
                            rv.visibility = View.GONE
                            tvEmpty.visibility = View.VISIBLE
                        } else {
                            rv.visibility = View.VISIBLE
                            tvEmpty.visibility = View.GONE
                        }
                    }
                }
                override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                    Toast.makeText(this@ItemsActivity,
                        "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun requestBorrow(item: Item) {
        ApiClient.apiService.createRequest(
            "Bearer $token",
            BorrowRequestCreateRequest(item.id)
        ).enqueue(object : Callback<BorrowRequestResponse> {
            override fun onResponse(call: Call<BorrowRequestResponse>, response: Response<BorrowRequestResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ItemsActivity,
                        "Request submitted for ${item.name}!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ItemsActivity,
                        "Failed to request: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<BorrowRequestResponse>, t: Throwable) {
                Toast.makeText(this@ItemsActivity,
                    "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}