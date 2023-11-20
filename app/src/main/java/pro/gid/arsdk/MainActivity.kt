package pro.gid.arsdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pro.gid.sdk.ARSdk


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARSdk.startARApplication(this)
    }
}
