package com.musict.budgetexpensemanagerhelp

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig
import com.google.android.gms.auth.api.credentials.CredentialRequest
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsOptions
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.auth.api.credentials.IdentityProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.drive.Drive
import com.google.android.gms.drive.DriveApi
import com.google.android.gms.drive.DriveClient
import com.google.android.gms.drive.DriveFile
import com.google.android.gms.drive.DriveId
import com.google.android.gms.drive.MetadataChangeSet
import com.google.android.gms.drive.OpenFileActivityBuilder
import com.google.android.gms.drive.OpenFileActivityOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.musict.budgetexpensemanagerhelp.R
import com.musict.budgetexpensemanagerhelp.sqlite.SqlLiteDataHelper

private val OpenFileActivityOptions.intentSender: IntentSender
    get() {
        return intentSender
    }


class SettingActivity : AppCompatActivity() {

    private lateinit var dbHelper: SqlLiteDataHelper
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleApiClient: GoogleApiClient

    private val REQUEST_CODE_RESOLUTION = 1
    private val RC_SIGN_IN = 123

    private lateinit var driveClient: DriveClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        dbHelper = SqlLiteDataHelper(this)
        initViews()

        val clearButton = findViewById<CardView>(R.id.clearButton)
        clearButton.setOnClickListener {
            showConfirmationDialog()
        }

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Drive.SCOPE_FILE)
            .build()

        val cardView = findViewById<LinearLayout>(R.id.linetakedrive)
        cardView.setOnClickListener {
            signInToDrive()
        }

        val restre = findViewById<LinearLayout>(R.id.linerestoredrive)
        restre.setOnClickListener {
            restoreBackupFromDrive()
        }
    }

    private fun initViews() {
        val backButton = findViewById<ImageView>(R.id.imgback)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showConfirmationDialog() {
        val preservedCategory = "category_value_to_preserve"
        val preservedMode = "mode_value_to_preserve"

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Are you sure you want to delete?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                clearData()
                dialog.dismiss()
                showToast("Deleted Successfully...")
                // Perform any additional actions after data is cleared
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Confirmation")
        alert.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearData() {
        val writableDatabase = dbHelper.writableDatabase

        val preservedCategory = "category_value_to_preserve"
        val preservedMode = "mode_value_to_preserve"

        val selection = "category <> ? OR mode <> ?"
        val selectionArgs = arrayOf(preservedCategory, preservedMode)

        writableDatabase.delete("StorageTb", selection, selectionArgs)

        // Add any additional actions or notifications here after data is cleared

        writableDatabase.close()
    }

    private fun signInToDrive() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(Auth.CREDENTIALS_API)
            .addApi(Drive.API)
            .build()
        googleApiClient.connect()

        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun restoreBackupFromDrive() {
        val credential = CredentialsOptions.Builder()
            .forceEnableSaveDialog()
            .build()

        val hintRequest = HintRequest.Builder()
            .setHintPickerConfig(
                CredentialPickerConfig.Builder()
                    .setShowCancelButton(true)
                    .build()
            )
            .setEmailAddressIdentifierSupported(true)
            .setAccountTypes(IdentityProviders.GOOGLE)
            .build()

        val intent = Auth.CredentialsApi.getHintPickerIntent(
            googleApiClient, hintRequest
        )
        startIntentSenderForResult(
            intent.intentSender, REQUEST_CODE_RESOLUTION, null, 0, 0, 0, null
        )
    }

    private fun takeDriveBackup(account: GoogleSignInAccount) {
        val driveResourceClient = Drive.getDriveResourceClient(this, account)

        val backupContents = "This is my backup data" // Replace with your actual backup data

        val changeSet = MetadataChangeSet.Builder()
            .setTitle("Backup.txt")
            .setMimeType("text/plain")
            .setStarred(true)
            .build()

        // Replace "your_folder_id" with the real folder ID where you want to create the file
        val folderId = "your_folder_id"

        val folder = DriveId.decodeFromString(folderId).asDriveFolder()
        driveResourceClient.createContents()
            .continueWithTask { createContentsTask ->
                val contents = createContentsTask.result
                contents.outputStream.use { outputStream ->
                    outputStream.write(backupContents.toByteArray())
                }

                // Create the file within the specified folder
                driveResourceClient.createFile(folder, changeSet, contents)
            }
            .addOnSuccessListener {
                showToast("Drive backup created successfully")
            }
            .addOnFailureListener {
                showToast("Failed to create drive backup")
                it.printStackTrace()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_RESOLUTION) {
            if (resultCode == Activity.RESULT_OK) {
                val credential = data?.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
                credential?.let {
                    val resultCallback: ResultCallback<Status> = object : ResultCallback<Status> {
                        override fun onResult(status: Status) {
                            if (status.isSuccess) {
                                // Move the code for opening the file activity here
                                val intent = OpenFileActivityOptions.Builder()
                                    .setMimeType(arrayOf("application/json").toMutableList())
                                    .build()

                                startIntentSenderForResult(
                                    intent.intentSender,
                                    REQUEST_CODE_RESOLUTION,
                                    null,
                                    0,
                                    0,
                                    0,
                                    null
                                )
                            } else {
                                showToast("Failed to authenticate")
                            }
                        }
                    }

                    // Request credentials using the Credentials API
                    val credentialsClient = Credentials.getClient(this)
                    credentialsClient.save(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Handle successful credential saving
                            } else {
                                showToast("Failed to save credentials")
                            }

                        }
                }
            } else {
                showToast("Failed to authenticate")
            }
        }

        if (requestCode == REQUEST_CODE_RESOLUTION) {
            if (resultCode == Activity.RESULT_OK) {
                val driveId = data?.getParcelableExtra<DriveId>(
                    OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID
                )

                driveId?.let {
                    // Initialize the driveClient here if it's not already initialized
                    if (!::driveClient.isInitialized) {
                        driveClient = Drive.getDriveClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
                    }

                    // Restore backup logic here
                    // Use driveFile to read the backup file and restore the data
                }
            }
        }

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    takeDriveBackup(it)
                }
            } catch (e: ApiException) {
                showToast("Failed to sign in to Google Drive")
                e.printStackTrace()
            }
        }
    }

}



