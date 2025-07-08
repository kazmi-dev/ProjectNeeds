import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.kazmi.dev.gameborse.utils.KeyboardUtils.showKeyboard

object KeyboardUtils {

    //Context Specific
    fun Context.hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Context.showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // For Android 9 and above, you need to use specific behavior to show the keyboard in some cases.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } else {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    //Fragment Specific
    fun Fragment.showKeyboard(view: View){
        requireContext().showKeyboard(view)
    }
    fun Fragment.hideKeyboard(view: View){
        requireContext().hideKeyboard(view)
    }

    //Activity Specific
    fun Activity.showSoftKeyboard(view: View){
        this.showKeyboard(view)
    }
    fun Activity.hideSoftKeyboard(view: View){
        this.hideKeyboard(view)
    }

    //keyboardVisibility
    fun View.observeKeyboardChanges(
        onKeyboardShown: (height: Int)-> Unit,
        onKeyboardHidden: () -> Unit,
    ){
        var isKeyboardVisible = false

        viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            getWindowVisibleDisplayFrame(rect)

            val screenHeight = rootView.height
            val keyboardHeight = screenHeight - rect.bottom
            val isNowVisible = keyboardHeight > screenHeight * 0.15

            if (isNowVisible && !isKeyboardVisible) {
                isKeyboardVisible = true
                onKeyboardShown(keyboardHeight)
            } else if (!isNowVisible && isKeyboardVisible) {
                isKeyboardVisible = false
                onKeyboardHidden()
            }
        }

    }

    //Scroll edittext fix in nested scrolls
    @SuppressLint("ClickableViewAccessibility")
    fun EditText.fixNestedScroll(){
        this.setOnTouchListener { v, event ->
            if(v.hasFocus()){
                v.parent.requestDisallowInterceptTouchEvent(true)
                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL){
                    v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }
        this.movementMethod = ScrollingMovementMethod.getInstance()
    }

}
