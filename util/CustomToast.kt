import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

class ToastUtil {
    companion object {

        private var toast: Toast? = null

        //Context Specific
        fun Context.shortToast(msg: String) {
            toast?.cancel()
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
            toast?.show()
        }

        fun Context.longToast(msg: String) {
            toast?.cancel()
            toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
            toast?.show()
        }

        //Fragment Specific
        fun Fragment.shortToast(msg: String) {
            try {
                requireContext().shortToast(msg)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun Fragment.longToast(msg: String) {
            try {
                requireContext().longToast(msg)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
