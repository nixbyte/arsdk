package pro.gid.sdk

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

object ARSdk {
    fun startARApplication(context: Context) {
        val intent = Intent(context, LahtaActivity::class.java)
        startActivity(context,intent,null)
    }
}