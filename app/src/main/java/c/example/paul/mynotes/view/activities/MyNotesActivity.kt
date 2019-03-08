package c.example.paul.mynotes.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import c.example.paul.mynotes.R
import c.example.paul.mynotes.WorkSendBackgroud
import c.example.paul.mynotes.helper.FragmentTools.replaceFragment
import c.example.paul.mynotes.view.fragments.AddNoteFragment
import c.example.paul.mynotes.view.fragments.DisplayNoteFragment
import kotlinx.android.synthetic.main.activity_mynotes.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import java.util.concurrent.TimeUnit


class MyNotesActivity : AppCompatActivity(), AnkoLogger {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workmanager()
        setContentView(R.layout.activity_mynotes)
        replaceFragment(DisplayNoteFragment.instance(), supportFragmentManager, R.id.notesContainer)

        addCanvas.setOnClickListener {

            startActivity<Paint>()
        }


        addNotes.setOnClickListener {


            replaceFragment(AddNoteFragment.instance(), supportFragmentManager, R.id.notesContainer)

        }

    }

    private fun workmanager() {
        val constraint=Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val sendNoteData=PeriodicWorkRequest.Builder(
            WorkSendBackgroud::class.java,15,TimeUnit.MINUTES)
            .setConstraints(constraint)
            .build()

        val workManager=WorkManager.getInstance()
//        workManager.enqueueUniquePeriodicWork("my_unique_worker",  ExistingPeriodicWorkPolicy.KEEP, sendNoteData)
        workManager.enqueue(sendNoteData)

    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount == 1) {
            finish()
        } else {
            if (backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }
        }
    }


}
