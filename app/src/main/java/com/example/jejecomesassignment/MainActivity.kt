package com.example.businesscardapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.jejecomesassignment.databinding.ActivityMainBinding
import com.example.jejecomesassignment.domain.model.BusinessCard
import com.example.jejecomesassignment.presentation.viewModel.BusinessCardViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: BusinessCardViewModel by viewModel()
    private val dynamicFields = mutableMapOf<String, EditText>()

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val bitmap = it.data?.extras?.get("data") as? Bitmap
        bitmap?.let { photo -> processImage(photo) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userEmail = intent.getStringExtra("userEmail")
        if (!userEmail.isNullOrEmpty()) {
            viewModel.setUserEmail(userEmail)
        }
        // Hide Edit title initially
        binding.tvEditTitle.visibility = View.GONE

        // Show default demo card
        binding.tvDemoTitle.text = "Demo Business Card"
        showDemoCard()

        // Enable edge-to-edge UI
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }

        // Ask for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
        }

        binding.btnCapture.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        }

        binding.btnSaveCard.setOnClickListener {
            val name = dynamicFields["Name"]?.text?.toString()?.trim().orEmpty()
            val phone = dynamicFields["Phone"]?.text?.toString()?.trim().orEmpty()
            val email = dynamicFields["Email"]?.text?.toString()?.trim().orEmpty()
            val company = dynamicFields["Company"]?.text?.toString()?.trim().orEmpty()
            val address = dynamicFields["Address"]?.text?.toString()?.trim().orEmpty()

            if (name.isBlank() || phone.isBlank() || email.isBlank()) {
                Toast.makeText(this, "Name, Phone, and Email are required.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val card = BusinessCard(
                name = name,
                phone = phone,
                email = email,
                company = company,
                address = address
            )

            viewModel.saveCard(card)
        }
    }

        private fun processImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { result ->
                val lines = result.text.lines()
                val container = binding.containerFields
                container.removeAllViews()
                container.addView(binding.tvDemoTitle)
                container.addView(binding.cardBusiness)

                // ✅ Show the XML-defined edit title
                binding.tvEditTitle.visibility = View.VISIBLE
                container.addView(binding.tvEditTitle)

                dynamicFields.clear()
                val fieldMap = mutableMapOf<String, String>()

                for (line in lines) {
                    val text = line.trim()
                    when {
                        text.contains("@") && !fieldMap.containsKey("Email") ->
                            fieldMap["Email"] = text.removePrefix("Email:").trim()

                        text.matches(Regex(".*\\d{10,}.*")) && !fieldMap.containsKey("Phone") ->
                            fieldMap["Phone"] = text.removePrefix("Phone:").trim()

                        Regex("(?i)(pvt|ltd|inc|tech|solutions|company)").containsMatchIn(text) &&
                                !fieldMap.containsKey("Company") ->
                            fieldMap["Company"] = text.removePrefix("Company:").trim()

                        Regex("(?i)(street|road|nagar|colony|sector|area|lane|korea)").containsMatchIn(text) &&
                                !fieldMap.containsKey("Address") ->
                            fieldMap["Address"] = text.removePrefix("Address:").trim()

                        fieldMap["Name"].isNullOrEmpty() && text.split(" ").size in 2..3 ->
                            fieldMap["Name"] = text.removePrefix("Name:").trim()
                    }
                }

                val allLabels = listOf("Name", "Phone", "Email", "Company", "Address")
                for (label in allLabels) {
                    val value = fieldMap[label] ?: "N/A"
                    addDynamicEditText(label, value)
                }

                binding.tvName.text = fieldMap["Name"] ?: "N/A"
                binding.tvPhone.text = "Phone: ${fieldMap["Phone"] ?: "N/A"}"
                binding.tvEmail.text = "Email: ${fieldMap["Email"] ?: "N/A"}"
                binding.tvCompany.text = "Company: ${fieldMap["Company"] ?: "N/A"}"
                binding.tvAddress.text = "Address: ${fieldMap["Address"] ?: "N/A"}"

                binding.tvDemoTitle.text = "Your Business Card"
                container.addView(binding.btnCapture)
                container.addView(binding.btnSaveCard)
                binding.cardBusiness.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                Toast.makeText(this, "OCR failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addDynamicEditText(label: String, value: String) {
        val container = binding.containerFields

        val textView = TextView(this).apply {
            text = label
            textSize = 16f
            setTextColor(Color.WHITE)
        }

        val editText = EditText(this).apply {
            hint = label
            setText(value)
            textSize = 16f
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        editText.addTextChangedListener {
            when (label) {
                "Name" -> binding.tvName.text = it.toString().ifBlank { "Name: N/A" }
                "Phone" -> binding.tvPhone.text = "Phone: ${it.toString().ifBlank { "N/A" }}"
                "Email" -> binding.tvEmail.text = "Email: ${it.toString().ifBlank { "N/A" }}"
                "Company" -> binding.tvCompany.text = "Company: ${it.toString().ifBlank { "N/A" }}"
                "Address" -> binding.tvAddress.text = "Address: ${it.toString().ifBlank { "N/A" }}"
            }
        }

        container.addView(textView)
        container.addView(editText)
        dynamicFields[label] = editText
    }

    private fun observeViewModel() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.saveResult.collect {
                val msg = if (it == true) "Saved!" else "Save failed!"
                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDemoCard() {
        binding.tvName.text = "Akbar Ikram"
        binding.tvPhone.text = "Phone: 9876543210"
        binding.tvEmail.text = "Email: akbar@gmail.com"
        binding.tvCompany.text = "Company: AkbarTech"
        binding.tvAddress.text = "Address: N/A"
        binding.cardBusiness.visibility = View.VISIBLE
    }
}
