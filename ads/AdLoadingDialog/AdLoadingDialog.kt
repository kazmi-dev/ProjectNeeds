import android.app.AlertDialog
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdLoadingDialog {
    companion object{

        fun showAdLoadingDialog(context: Context, time: Int = 10, callback:(AlertDialog)->Unit){

            val adLoadingDialog: AlertDialog = AlertDialog.Builder(context).create()
            adLoadingDialog.apply {
                setMessage("Ad Loading...")

                GlobalScope.launch(Dispatchers.Main) {
                    delay((time * 100).toLong())
                    if (isShowing){
                        callback(adLoadingDialog)
                        dismiss()
                    }
                }

                setCancelable(false)
                setCanceledOnTouchOutside(false)
                show()
            }

        }

    }
}
