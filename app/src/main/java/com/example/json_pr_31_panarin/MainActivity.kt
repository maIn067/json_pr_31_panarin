package com.example.json_pr_31_panarin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.google.gson.Gson
class MainActivity : AppCompatActivity() {
    private val gson = Gson()
    private var jsonString = ""
    private lateinit var nameEdit: AppCompatEditText
    private lateinit var priceEdit: AppCompatEditText
    private lateinit var tagsEdit: AppCompatEditText
    private lateinit var jsonText: AppCompatTextView
    private lateinit var resultText: AppCompatTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nameEdit = findViewById(R.id.name_edit)
        priceEdit = findViewById(R.id.price_edit)
        tagsEdit = findViewById(R.id.tags_edit)
        jsonText = findViewById(R.id.json_text)
        resultText = findViewById(R.id.result_text)
        val serializeButton: AppCompatButton = findViewById(R.id.serialize_button)
        val deserializeButton: AppCompatButton = findViewById(R.id.deserialize_button)
        val product = Product(
            "Программирование на Kotlin",
            1500.0,
            listOf("учебник", "программирование", "android")
        )
        nameEdit.setText(product.name)
        priceEdit.setText(product.price.toString())
        tagsEdit.setText(product.tags.joinToString(", "))
        serialize(product)
        serializeButton.setOnClickListener {
            val name = nameEdit.text.toString().trim()
            val price = priceEdit.text.toString().toDoubleOrNull()
            if (name.isEmpty() || price == null) {
                Toast.makeText(this, "Заполните название и цену", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val tags = tagsEdit.text.toString()
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            serialize(Product(name, price, tags))
        }
        deserializeButton.setOnClickListener {
            deserialize(jsonString)
        }
    }
    private fun serialize(product: Product) {
        jsonString = gson.toJson(product)
        jsonText.text = jsonString
        resultText.text = ""
        Log.d("JsonApp", "Сериализация: $jsonString")
    }
    private fun deserialize(json: String) {
        try {
            val product = gson.fromJson(json, Product::class.java)
            val text = "Название: ${product.name}\n" +
                    "Цена: ${product.price}\n" +
                    "Теги: ${product.tags.joinToString(", ")}"
            resultText.text = text
            Log.d("JsonApp", "Десериализация: $text")
        } catch (e: Exception) {
            resultText.text = "Ошибка: ${e.message}"
            Log.e("JsonApp", "Ошибка разбора JSON", e)
        }
    }
}