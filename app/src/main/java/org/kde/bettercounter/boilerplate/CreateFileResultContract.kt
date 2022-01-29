package org.kde.bettercounter.boilerplate;

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

data class CreateFileParams(
        val fileMimeType: String,
        val suggestedFileName: String
)

class CreateFileResultContract : ActivityResultContract<CreateFileParams, Uri?>() {

        override fun createIntent(context: Context, data: CreateFileParams): Intent =
                Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                setTypeAndNormalize(data.fileMimeType)
                putExtra(Intent.EXTRA_TITLE, data.suggestedFileName)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? = when (resultCode) {
                Activity.RESULT_OK -> intent?.data
                else -> null
        }
}