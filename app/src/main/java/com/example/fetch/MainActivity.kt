package com.example.fetch

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fetch.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch data from API
        fetchItems()
    }

    private fun fetchItems() {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        apiService.fetchItems().enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    val items = response.body()?.filter { !it.name.isNullOrBlank() }
                    val groupedItems = items?.groupBy { it.listId } ?: emptyMap()

                    // Set up adapter
                    val adapter = ItemAdapter(groupedItems)
                    recyclerView.adapter = adapter
                } else {
                    showError("Failed to fetch data")
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                showError("An error occurred: ${t.message}")
            }
        })
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

