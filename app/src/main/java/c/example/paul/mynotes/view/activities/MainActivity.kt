package c.example.paul.mynotes.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import c.example.paul.mynotes.R
import c.example.paul.mynotes.helper.FragmentTools.replaceFragment
import c.example.paul.mynotes.view.fragments.AddNoteFragment
import c.example.paul.mynotes.view.fragments.DisplayNoteFragment
import c.example.paul.mynotes.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), AnkoLogger {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        replaceFragment(DisplayNoteFragment.instance(), supportFragmentManager, R.id.notesContainer)

        addCanvas.setOnClickListener {

            startActivity<Paint>()
        }


        addNotes.setOnClickListener {



            replaceFragment(AddNoteFragment.instance(),supportFragmentManager,R.id.notesContainer)

        }

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
