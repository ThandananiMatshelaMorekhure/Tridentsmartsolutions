package com.donation.tridentsmartsolutions

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Date

class RequestFormActivity : AppCompatActivity() {

    // UI Components
    private lateinit var tvServiceType: android.widget.TextView
    private lateinit var etProblemDescription: TextInputEditText
    private lateinit var spinnerUrgency: Spinner
    private lateinit var btnSelectDate: Button
    private lateinit var spinnerContactPreference: Spinner
    private lateinit var btnSubmitRequest: Button

    // Navigation
    private lateinit var navHome: MaterialCardView
    private lateinit var navRequest: MaterialCardView
    private lateinit var navAccount: MaterialCardView

    // Selected date
    private var selectedDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_form)

        initializeViews()
        setupSpinners()
        setupClickListeners()
        setServiceTypeFromIntent()
    }

    private fun initializeViews() {
        tvServiceType = findViewById(R.id.tv_service_type)
        etProblemDescription = findViewById(R.id.et_problem_description)
        spinnerUrgency = findViewById(R.id.spinner_urgency)
        btnSelectDate = findViewById(R.id.btn_select_date)
        spinnerContactPreference = findViewById(R.id.spinner_contact_preference)
        btnSubmitRequest = findViewById(R.id.btn_submit_request)

        // Navigation
        navHome = findViewById(R.id.nav_home)
        navRequest = findViewById(R.id.nav_request)
        navAccount = findViewById(R.id.nav_account)
    }

    private fun setupSpinners() {
        // Urgency spinner
        val urgencyOptions = arrayOf("Low - Within 1 week", "Medium - Within 3 days", "High - Within 24 hours", "Emergency - ASAP")
        val urgencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, urgencyOptions)
        urgencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerUrgency.adapter = urgencyAdapter

        // Contact preference spinner
        val contactOptions = arrayOf("Phone Call", "SMS", "Email", "WhatsApp")
        val contactAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, contactOptions)
        contactAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerContactPreference.adapter = contactAdapter
    }

    private fun setupClickListeners() {
        btnSelectDate.setOnClickListener {
            showDatePicker()
        }

        btnSubmitRequest.setOnClickListener {
            submitServiceRequest()
        }

        // Navigation listeners
        navHome.setOnClickListener {
            navigateToHome()
        }

        navRequest.setOnClickListener {
            navigateToRequestPage()
        }

        navAccount.setOnClickListener {
            navigateToProfile()
        }
    }

    private fun setServiceTypeFromIntent() {
        val serviceType = intent.getStringExtra(RequestPage.EXTRA_SERVICE_TYPE)
        val serviceName = when (serviceType) {
            RequestPage.SERVICE_TYPE_PLUMBING -> "Plumbing"
            RequestPage.SERVICE_TYPE_SECURITY -> "Security"
            RequestPage.SERVICE_TYPE_EMERGENCY -> "Emergency"
            else -> "Service"
        }
        tvServiceType.text = "$serviceName Request"

        // Auto-select urgency for emergency requests
        if (serviceType == RequestPage.SERVICE_TYPE_EMERGENCY) {
            spinnerUrgency.setSelection(3) // Emergency option
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }.time

            btnSelectDate.text = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
        }, year, month, day)

        // Set minimum date to today
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }

    private fun submitServiceRequest() {
        val problemDescription = etProblemDescription.text.toString().trim()
//        val urgency = spinnerUrgency.selectedItem.toString()
//        val contactPreference = spinnerContactPreference.selectedItem.toString()

        if (problemDescription.isEmpty()) {
            etProblemDescription.error = "Please describe the problem"
            etProblemDescription.requestFocus()
            return
        }

        if (selectedDate == null) {
            Toast.makeText(this, "Please select a preferred date", Toast.LENGTH_SHORT).show()
            return
        }

        // TODO: Implement actual request submission to Firebase
        // For now, just show success message
        Toast.makeText(this, "Service request submitted successfully!", Toast.LENGTH_LONG).show()

        // Navigate back to request page
        navigateToRequestPage()
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRequestPage() {
        val intent = Intent(this, RequestPage::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToProfile() {
        val intent = Intent(this, ProfilePage::class.java)
        startActivity(intent)
        finish()
    }
}