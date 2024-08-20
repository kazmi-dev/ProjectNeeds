import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class DownloadManager(
    private val context: Context,
) {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    private var title: String = "Downloading..."
    private var description: String = "File is being downloading"
    private var destination = Environment.DIRECTORY_DOWNLOADS


    fun setTitle(title: String) {
        this@DownloadManager.title = title
    }

    fun setDescription(description: String) {
        this@DownloadManager.description = description
    }

    fun setDestination(destination: String) {
        this@DownloadManager.destination = destination
    }

    private fun enqueueDownloadRequest(
        fileName: String,
        fileUrl: String = System.currentTimeMillis().toString(),
    ): Long {
        //set request
        val request = DownloadManager.Request(fileUrl.toUri())

        request.apply {
            setTitle(title)
            setDescription(description)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            setDestinationInExternalFilesDir(context, destination, fileName)
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        }

        return downloadManager.enqueue(request)
    }


    suspend fun downloadFile(
        fileName: String,
        fileUrl: String = System.currentTimeMillis().toString()
    ): Long {
        val downloadId = withContext(Dispatchers.IO) {
            enqueueDownloadRequest(fileName, fileUrl)
        }
        return downloadId
    }


    suspend fun monitorDownloadProgress(
        downloadId: Long,
        callBack: (progress: Int) -> Unit,
    ) {
        //create query with download id
        val query = DownloadManager.Query().setFilterById(downloadId)

        while (true) {
            withContext(Dispatchers.IO) {
                downloadManager.query(query).use { cursor ->
                    if (cursor.moveToFirst()) {
                        val bytesDownloaded =
                            cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        val bytesTotal =
                            cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                        if (bytesTotal > 0) {
                            val progress = (bytesDownloaded * 100L / bytesTotal).toInt()
                            callBack(progress)
                        }

                        val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                            return@withContext
                        }
                    }
                }
            }
            delay(500)
        }

    }


}
