package c.example.paul.mynotes.viewmodel

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent
import android.util.TypedValue
import android.graphics.PorterDuff
import c.example.paul.mynotes.R


class DrawingView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var drawPath : Path?=null
    private var drawPaint : Paint?=null
    private var canvasPaint:Paint?=null
    private var paintColor = 0xff000000
    private var previousColor = paintColor
    private var drawCanvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null
    private var brushSize:Float?=null
    private var lastBrushSize:Float?=null
    private var erase = false
    init {
        setUpDrawing()
    }

    private fun setUpDrawing() {
        drawPath = Path()
        drawPaint = Paint()
        drawPaint!!.color = paintColor.toInt()
        // Making drawing smooth.
        drawPaint!!.isAntiAlias = true
        drawPaint!!.style = Paint.Style.STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND

        canvasPaint = Paint(Paint.DITHER_FLAG)

        // Initial brush size is medium.
        brushSize = resources.getInteger(R.integer.medium_size).toFloat()
        lastBrushSize = brushSize
        drawPaint!!.strokeWidth = brushSize!!
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888) as Bitmap
        drawCanvas = Canvas(canvasBitmap)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.run {
            drawBitmap(canvasBitmap!!, 0f, 0f, drawPaint!!)
            drawPath(drawPath, drawPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var touchX = event!!.x
        var touchY = event.y
        // Draw the path according to the touch event taking place.
        when (event.action) {
            MotionEvent.ACTION_DOWN -> drawPath!!.moveTo(touchX, touchY)

            MotionEvent.ACTION_MOVE -> drawPath!!.lineTo(touchX, touchY)

            MotionEvent.ACTION_UP -> {
                if (erase) {
                    drawPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                }
                drawCanvas!!.drawPath(drawPath, drawPaint)
                drawPath!!.reset()
                drawPaint!!.xfermode= null
            }

            else -> {
                return false
            }
        }
        invalidate()
        return true
    }

    fun setColor(newColor: String) {
        // invalidate the view
        invalidate()
        paintColor = Color.parseColor(newColor).toLong()
        drawPaint!!.color = paintColor.toInt()
        previousColor = paintColor
    }

    fun setBrushSize(newSize: Float) {
        val pixelAmount = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            newSize, resources.displayMetrics
        )
        brushSize = pixelAmount
        drawPaint!!.strokeWidth = brushSize!!

    }

    fun setLastBrushSize(lastSize: Float) {
        lastBrushSize = lastSize
    }

    fun getLastBrushSize(): Float {
        return lastBrushSize!!.toFloat()
    }

    fun setErase(isErase: Boolean) {
        //set erase true or false
        erase = isErase
        if (erase) {
            drawPaint!!.color = Color.WHITE
            //drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            drawPaint!!.color = previousColor.toInt()
            drawPaint!!.xfermode = null
        }
    }

    fun startNew() {
        drawCanvas!!.drawColor(0, PorterDuff.Mode.CLEAR)
        invalidate()
    }


}