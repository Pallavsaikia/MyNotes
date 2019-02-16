package c.example.paul.mynotes.view.activities


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProviders
import c.example.paul.mynotes.R
import c.example.paul.mynotes.pojo.Notes
import c.example.paul.mynotes.viewmodel.DrawingView
import c.example.paul.mynotes.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.activity_paint.*
import org.jetbrains.anko.startActivity
import org.koin.android.viewmodel.ext.android.viewModel


class Paint : AppCompatActivity(), View.OnClickListener {

    private var mDrawingView: DrawingView? = null
    private lateinit var notesViewModel: NotesViewModel

    private var currPaint: ImageButton? = null
    private var drawButton: ImageButton? = null
    private var eraseButton: ImageButton? = null
    private var newButton: ImageButton? = null
    private var saveButton: ImageButton? = null

    var smallBrush: Float = 0.toFloat()
    var mediumBrush: Float = 0.toFloat()
    var largeBrush: Float = 0.toFloat()


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notesViewModel=ViewModelProviders.of(this).get(NotesViewModel::class.java)
        funcheckPermission()
        setContentView(R.layout.activity_paint)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        mDrawingView = findViewById<DrawingView>(R.id.drawing)
        // Getting the initial paint color.
        val paintLayout = findViewById<LinearLayout>(R.id.paint_colors)
        // 0th child is white color, so selecting first child to give black as initial color.
        currPaint = paintLayout.getChildAt(1) as ImageButton
        currPaint!!.setImageDrawable(resources.getDrawable(R.drawable.pallet_pressed))
        buttonBrush.setOnClickListener(this)
        buttonErase.setOnClickListener(this)
        buttonNew.setOnClickListener(this)
        buttonSave.setOnClickListener(this)

        smallBrush = resources.getInteger(R.integer.small_size).toFloat()
        mediumBrush = resources.getInteger(R.integer.medium_size).toFloat()
        largeBrush = resources.getInteger(R.integer.large_size).toFloat()

        // Set the initial brush size
        mDrawingView!!.setBrushSize(mediumBrush)
    }

    private fun funcheckPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this, "You have already granted this permission!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            requestStoragePermission()
        }
    }

    fun paintClicked(view: View) {
        if (view !== currPaint) {
            // Update the color
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            mDrawingView!!.setColor(colorTag)
            // Swap the backgrounds for last active and currently active image button.
            imageButton.setImageDrawable(resources.getDrawable(R.drawable.pallet_pressed))
            currPaint!!.setImageDrawable(resources.getDrawable(R.drawable.pallet))
            currPaint = view
            mDrawingView!!.setErase(false)
            mDrawingView!!.setBrushSize(mDrawingView!!.getLastBrushSize())
        }
    }


    override fun onClick(v: View?) {
        val id = v!!.id
        when (id) {
            R.id.buttonBrush ->
                // Show brush size chooser dialog
                showBrushSizeChooserDialog()
            R.id.buttonErase ->
                // Show eraser size chooser dialog
                showEraserSizeChooserDialog()
            R.id.buttonNew ->
                // Show new painting alert dialog
                showNewPaintingAlertDialog()
            R.id.buttonSave ->
                // Show save painting confirmation dialog.
                showSavePaintingConfirmationDialog()
        }
    }


    private fun showSavePaintingConfirmationDialog() {
        val saveDialog = AlertDialog.Builder(this)
        saveDialog.setTitle("Save drawing")
        saveDialog.setMessage("Save drawing to device Gallery?")
        saveDialog.setPositiveButton("Yes") { dialog, which ->
            val time=System.currentTimeMillis()
            //save drawing
            val filename = time.toString()
            mDrawingView!!.setDrawingCacheEnabled(true)
            val imgSaved = MediaStore.Images.Media.insertImage(contentResolver, mDrawingView!!.getDrawingCache(), filename + ".png", "drawing")
            if (imgSaved != null) {
                val notes= Notes("canvas$time",time.toString(),getRealPathFromURI(imgSaved.toUri()),System.currentTimeMillis(),true,false)
                notesViewModel.insertNotes(notes)
                startActivity<MainActivity>()
            } else {
                val unsavedToast = Toast.makeText(
                    applicationContext,
                    "Oops! Image could not be saved.", Toast.LENGTH_SHORT
                )
                unsavedToast.show()
            }
            // Destroy the current cache.
            mDrawingView!!.destroyDrawingCache()
        }
        saveDialog.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        saveDialog.show()

    }

    fun getRealPathFromURI(contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = this.getContentResolver().query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            return cursor!!.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor!!.close()
            }
        }
    }

    private fun showNewPaintingAlertDialog() {
        val newDialog = AlertDialog.Builder(this)
        newDialog.setTitle("New drawing")
        newDialog.setMessage("Start new drawing (you will lose the current drawing)?")
        newDialog.setPositiveButton("Yes") { dialog, which ->
            mDrawingView!!.startNew()
            dialog.dismiss()
        }
        newDialog.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        newDialog.show()
    }

    private fun showEraserSizeChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setTitle("Eraser size:")
        brushDialog.setContentView(R.layout.dialog_brush_size)
        val smallBtn = brushDialog.findViewById(R.id.small_brush) as ImageButton
        smallBtn.setOnClickListener {
            mDrawingView!!.setErase(true)
            mDrawingView!!.setBrushSize(smallBrush)
            brushDialog.dismiss()
        }
        val mediumBtn = brushDialog.findViewById(R.id.medium_brush) as ImageButton
        mediumBtn.setOnClickListener {
            mDrawingView!!.setErase(true)
            mDrawingView!!.setBrushSize(mediumBrush)
            brushDialog.dismiss()
        }
        val largeBtn = brushDialog.findViewById(R.id.large_brush) as ImageButton
        largeBtn.setOnClickListener {
            mDrawingView!!.setErase(true)
            mDrawingView!!.setBrushSize(largeBrush)
            brushDialog.dismiss()
        }
        brushDialog.show()
    }

    private fun showBrushSizeChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size:")
        val smallBtn = brushDialog.findViewById(R.id.small_brush) as ImageButton
        smallBtn.setOnClickListener {
            mDrawingView!!.setBrushSize(smallBrush)
            mDrawingView!!.setLastBrushSize(smallBrush)
            brushDialog.dismiss()
        }
        val mediumBtn = brushDialog.findViewById(R.id.medium_brush) as ImageButton
        mediumBtn.setOnClickListener {
            mDrawingView!!.setBrushSize(mediumBrush)
            mDrawingView!!.setLastBrushSize(mediumBrush)
            brushDialog.dismiss()
        }

        val largeBtn = brushDialog.findViewById(R.id.large_brush) as ImageButton
        largeBtn.setOnClickListener {
            mDrawingView!!.setBrushSize(largeBrush)
            mDrawingView!!.setLastBrushSize(largeBrush)
            brushDialog.dismiss()
        }
        mDrawingView!!.setErase(false)
        brushDialog.show()

    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {

            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed because of this and that")
                .setPositiveButton("ok") { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                    )
                }
                .setNegativeButton("cancel") { dialog, which -> dialog.dismiss() }
                .create().show()

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
            )
        }
    }


}
