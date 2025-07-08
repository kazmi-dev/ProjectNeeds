import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class PhotoPickerUtil(
    private val context: Context,
    caller: ActivityResultCaller,
    private val mediaType: PickerMediaType,
    maxMediaItems: Int = 1,
    isMultipleMedia: Boolean = false,
    onMediaSelected: (List<Uri>) -> Unit
) {

    private var isPhotoPickerVisible = false

    private val photoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest> =
        if (isMultipleMedia) {
            val maxItems = if (maxMediaItems > 50) 50 else maxMediaItems
            caller.registerForActivityResult(
                ActivityResultContracts.PickMultipleVisualMedia(
                    maxItems
                )
            ) {
                onMediaSelected(it)
            }
        } else {
            caller.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                isPhotoPickerVisible = false
                onMediaSelected(listOfNotNull(it))
            }
        }

    private val openDocumentLauncher: ActivityResultLauncher<Intent> =
        caller.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

    fun launchPicker() {
        isPhotoPickerVisible = true
        when {
            SDK_INT >= VERSION_CODES.TIRAMISU -> {
                handleByPhotoPicker()
            }

            SDK_INT >= VERSION_CODES.R -> {
                if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(context)) {
                    handleByPhotoPicker()
                } else {
                    handleMediaByActionOpenDocument()
                }
            }
        }
    }

    private fun handleMediaByActionOpenDocument() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = getMediaType(mediaType)
        }
        openDocumentLauncher.launch(intent)
    }

    private fun getMediaType(mediaType: PickerMediaType): String? {
        return when (mediaType) {
            PickerMediaType.IMAGES -> {
                "images/*"
            }

            PickerMediaType.VIDEO -> {
                "video/*"
            }

            PickerMediaType.IMAGES_VIDEOS -> {
                "*/*"
            }
        }
    }

    private fun handleByPhotoPicker() {
        val mediaType = when (mediaType) {
            PickerMediaType.IMAGES -> ActivityResultContracts.PickVisualMedia.ImageOnly
            PickerMediaType.VIDEO -> ActivityResultContracts.PickVisualMedia.VideoOnly
            PickerMediaType.IMAGES_VIDEOS -> ActivityResultContracts.PickVisualMedia.ImageAndVideo
        }
        photoPickerLauncher.launch(PickVisualMediaRequest(mediaType))
    }

    fun isPhotoPickerVisible() = isPhotoPickerVisible
}
