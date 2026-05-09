package com.example.borrowbox.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.borrowbox.R
import com.example.borrowbox.api.ApiClient
import com.example.borrowbox.model.MeResponse
import com.example.borrowbox.model.UpdateProfileRequest
import com.example.borrowbox.storage.TokenManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileBottomSheet : BottomSheetDialogFragment() {

    var onProfileUpdated: ((String, String) -> Unit)? = null

    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_edit_profile, container, false)

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog
        dialog?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        dialog?.behavior?.skipCollapsed = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenManager = TokenManager(requireContext())
        val token = tokenManager.getToken() ?: return

        val etFullName        = view.findViewById<EditText>(R.id.etFullName)
        val etCurrentPassword = view.findViewById<EditText>(R.id.etCurrentPassword)
        val etNewPassword     = view.findViewById<EditText>(R.id.etNewPassword)
        val tvError           = view.findViewById<TextView>(R.id.tvError)
        val btnSave           = view.findViewById<Button>(R.id.btnSave)
        val btnCancel         = view.findViewById<Button>(R.id.btnCancel)

        arguments?.getString(ARG_FULL_NAME)?.let {
            etFullName.setText(it)
        }

        btnCancel.setOnClickListener { dismiss() }

        btnSave.setOnClickListener {
            val newName     = etFullName.text.toString().trim()
            val currentPass = etCurrentPassword.text.toString()
            val newPass     = etNewPassword.text.toString()

            if (newName.isBlank()) {
                showError(tvError, "Full name cannot be empty.")
                return@setOnClickListener
            }
            if (newPass.isNotBlank() && currentPass.isBlank()) {
                showError(tvError, "Enter your current password to set a new one.")
                return@setOnClickListener
            }

            tvError.visibility = View.GONE
            btnSave.isEnabled = false
            btnSave.text = "Saving..."

            val request = UpdateProfileRequest(
                fullName        = newName,
                currentPassword = currentPass.ifBlank { null },
                newPassword     = newPass.ifBlank { null }
            )

            ApiClient.apiService.updateMe("Bearer $token", request)
                .enqueue(object : Callback<MeResponse> {
                    override fun onResponse(
                        call: Call<MeResponse>,
                        response: Response<MeResponse>
                    ) {
                        btnSave.isEnabled = true
                        btnSave.text = "Save Changes"

                        if (response.isSuccessful && response.body() != null) {
                            val updated = response.body()!!
                            Toast.makeText(
                                requireContext(),
                                "Profile updated!",
                                Toast.LENGTH_SHORT
                            ).show()
                            onProfileUpdated?.invoke(updated.fullName, updated.email)
                            dismiss()
                        } else {
                            showError(tvError, when (response.code()) {
                                400 -> "Current password is incorrect."
                                else -> "Failed to update profile (${response.code()})."
                            })
                        }
                    }

                    override fun onFailure(call: Call<MeResponse>, t: Throwable) {
                        btnSave.isEnabled = true
                        btnSave.text = "Save Changes"
                        showError(tvError, "Network error: ${t.message}")
                    }
                })
        }
    }

    private fun showError(tv: TextView, msg: String) {
        tv.text = msg
        tv.visibility = View.VISIBLE
    }

    companion object {
        private const val ARG_FULL_NAME = "full_name"

        fun newInstance(currentName: String): EditProfileBottomSheet {
            return EditProfileBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_FULL_NAME, currentName)
                }
            }
        }
    }
}