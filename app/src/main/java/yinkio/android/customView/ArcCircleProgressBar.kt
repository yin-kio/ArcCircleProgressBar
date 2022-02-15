package yinkio.android.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ArcCircleProgressBar : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {init(context, attrs)}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)




    private val indicator = Arc.Indicator()
    private val canal = Arc.Canal()



    private fun init(context: Context?, attrs: AttributeSet?){
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.ArcCircleProgressBar,
            0, 0
        )?.apply {
            try {
                val isRoundTips = getBoolean(R.styleable.ArcCircleProgressBar_roundTips, true)
                indicator.apply {
                    progress = getFloat(R.styleable.ArcCircleProgressBar_progress, 0f)
                    width = getDimension(R.styleable.ArcCircleProgressBar_indicatorWidth, 10f)
                    startAngle = getFloat(R.styleable.ArcCircleProgressBar_indicatorStartAngle, 0f)
                    endAngle = getFloat(R.styleable.ArcCircleProgressBar_indicatorEndAngle, 360f)
                    color = getColor(R.styleable.ArcCircleProgressBar_indicatorColor, Color.BLUE)

                    tip.color = color
                    tip.radius = width / 2
                    this.isRoundTips = isRoundTips
                }

                canal.apply {
                    width = getDimension(R.styleable.ArcCircleProgressBar_canalWidth, 10f)
                    startAngle = getFloat(R.styleable.ArcCircleProgressBar_canalStartAngle, 0f)
                    endAngle = getFloat(R.styleable.ArcCircleProgressBar_canalEndAngle, 360f)
                    color = getColor(R.styleable.ArcCircleProgressBar_canalColor, Color.BLUE)

                    tip.radius = width / 2
                    tip.color = color
                    this.isRoundTips = isRoundTips
                }
            } finally {
                recycle()
            }
        }
    }

    private val oval = RectF()


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        setMeasuredDimension(widthMeasureSpec + (paddingLeft + paddingRight),
            heightMeasureSpec + (paddingTop + paddingBottom))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        oval.apply {
            val side = min(width - (paddingLeft + paddingRight), height - (paddingTop + paddingBottom))
            val radius = side / 2 - maxOf(indicator.width, canal.width)
            val centerX = width / 2f + paddingLeft / 2 - paddingRight / 2
            val centerY = height / 2f + paddingTop / 2 - paddingBottom / 2
            set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        }

        canvas.apply {

            val maxArcWidth = maxOf(canal.width, indicator.width)
            val minArcWidth = minOf(canal.width, indicator.width)
            val indentOffsetKoef = 1 - minArcWidth / maxArcWidth

            drawCanal(maxArcWidth, indentOffsetKoef, canvas)
            drawIndicator(maxArcWidth, indentOffsetKoef, canvas)
        }
    }

    private fun drawCanal(
        maxArcWidth: Float,
        indentOffsetKoef: Float,
        canvas: Canvas
    ) {
        canal.apply {

            if (isRoundTips) {
                val outOffset =
                    if (canal.width < indicator.width) maxArcWidth * indentOffsetKoef else 0f

                tip.updatePaint()
                tip.draw(canvas, startAngle, this@ArcCircleProgressBar, outOffset)
                tip.draw(canvas, endAngle, this@ArcCircleProgressBar, outOffset)


            }

            updatePaint()
            draw(canvas, oval, this@ArcCircleProgressBar)
        }
    }

    private fun drawIndicator(
        maxArcWidth: Float,
        indentOffsetKoef: Float,
        canvas: Canvas
    ) {
        indicator.apply {

            if (isRoundTips) {
                val outOffset =
                    if (indicator.width < canal.width) maxArcWidth * indentOffsetKoef else 0f
                tip.updatePaint()
                tip.draw(canvas, startAngle, this@ArcCircleProgressBar, outOffset)
                tip.draw(
                    canvas,
                    progressAngle + startAngle,
                    this@ArcCircleProgressBar,
                    outOffset
                )
            }

            updatePaint()
            draw(canvas, oval, this@ArcCircleProgressBar)
        }
    }

    private abstract class Arc{
        open var width: Float = 10f
        open var color: Int = Color.BLACK
        open var startAngle: Float = 0f
        open var endAngle: Float = 360f
        open var isRoundTips = true


        val tip = Circle()

        val paint = Paint()

        fun updatePaint(){
            paint.apply {
                color = this@Arc.color
                strokeWidth = width
                style = Paint.Style.STROKE
                isAntiAlias = false
            }
        }

        open fun draw(canvas: Canvas, oval: RectF, view: View){
            canvas.apply {
                val end = if (startAngle < 0) startAngle * -1 + endAngle else endAngle
                drawArc(oval, startAngle, end, false, paint)
            }
        }


        class Indicator : Arc(){
            var progress = 0f
            override var color: Int = Color.BLUE

            val progressAngle: Float
                get() {
                    val koef = progress / MAX_PROGRESS
                    return (startAngle * -1 + endAngle) * koef
                }

            override fun draw(canvas: Canvas, oval: RectF, view: View){
                canvas.apply {
                    drawArc(oval, startAngle, progressAngle, false, paint)
                }
            }
        }

        class Canal : Arc(){
            override var color: Int = Color.GRAY
        }
    }

    class Circle{
        var color: Int = Color.BLACK
        var radius = 0f
        private val paint = Paint()

        internal fun updatePaint(){
            paint.apply {
                color = this@Circle.color
                isAntiAlias = false
            }
        }

        internal fun draw(canvas: Canvas, outAngle: Float, view: View, outOffset: Float){
            canvas.apply {
                val outRadius = min(width - (view.paddingLeft + view.paddingRight),
                    height - (view.paddingTop + view.paddingBottom)) / 2f - radius * 2 - outOffset
                val radians = (outAngle * PI / 180)
                val x = (outRadius) * cos(radians) + width / 2 + view.paddingLeft / 2 - view.paddingRight / 2
                val y = (outRadius) * sin(radians) + height / 2 + view.paddingTop / 2 - view.paddingBottom / 2

                drawCircle(x.toFloat(), y.toFloat(), radius, paint)
            }
        }
    }

    companion object{
        private const val MAX_PROGRESS = 100f
    }

    //TODO add tip clipping for assigning opacity of colors
    //TODO add public properties
    //TODO add wrap content
}