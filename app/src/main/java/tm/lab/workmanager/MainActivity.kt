package tm.lab.workmanager

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.await
import androidx.work.workDataOf
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import kotlinx.coroutines.withContext
import tm.lab.workmanager.MainActivity
import tm.lab.workmanager.ui.theme.WorkManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val ctx = applicationContext
                    MainScreen(ctx)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreen(ctx: Context, modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        var wynik: MutableState<Int> = remember { mutableStateOf(0) }
        val d1 = Data.Builder()
        var lco  = LocalLifecycleOwner.current
        d1.putInt("DANA1", wynik.value)

        Text(
            text = "wynik = ${wynik.value}"
        )
        Button(onClick = {
            wynik.value = -1
            val data = MyWorker.run(ctx)
            data.observe(lco) { workInfo ->
                wynik.value = workInfo.progress.getInt(MyWorker.TAG, -1)
                if (workInfo.state.isFinished) {
                    wynik.value = workInfo.outputData.getInt(MyWorker.TAG, -2)
                }
            }
        })
        {
            Text("Start")
        }
    }
}

