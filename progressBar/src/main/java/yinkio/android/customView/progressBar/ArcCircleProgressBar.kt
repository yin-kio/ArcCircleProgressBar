package yinkio.android.customView.progressBar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import yinkio.android.customView.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ArcCircleProgressBar : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {init(context, attrs)}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    private val oval = RectF()

    val indicator = Arc.Indicator(this)
    val canal = Arc.Canal()



    private fun init(context: Context?, attrs: AttributeSet?){
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.ArcCircleProgressBar,
            0, 0
        )?.apply {
            try {
                val isRoundTips = getBoolean(R.styleable.ArcCircleProgressBar_roundTips, true)
                setupIndicator(isRoundTips)
                setupCanal(isRoundTips)

            } finally {
                recycle()
            }
        }
    }


    private fun TypedArray.setupIndicator(isRoundTips: Boolean) {
        indicator.apply {
            progress = getFloat(R.styleable.ArcCircleProgressBar_progress, 0f)
            width = getDimension(R.styleable.ArcCircleProgressBar_indicatorWidth, 10f)
            startAngle = getFloat(R.styleable.ArcCircleProgressBar_indicatorStartAngle, 0f)
            endAngle = getFloat(R.styleable.ArcCircleProgressBar_indicatorEndAngle, 360f)
            color = getColor(R.styleable.ArcCircleProgressBar_indicatorColor, Color.BLUE)

            showGradient(
                arc = this,
                isShowRes = R.styleable.ArcCircleProgressBar_indicatorShowGradient,
                positionsResId = R.styleable.ArcCircleProgressBar_indicatorGradientPositions,
                colorsRes = R.styleable.ArcCircleProgressBar_indicatorGradientColors,
                angleIndex = R.styleable.ArcCircleProgressBar_indicatorGradientAngle,
                radiusIndex = R.styleable.ArcCircleProgressBar_indicatorGradientWidth,
                tileModeIndex = R.styleable.ArcCircleProgressBar_indicatorGradientTileMode
            )


            tip.color = color
            tip.radius = width / 2
            this.isRoundTips = isRoundTips
        }
    }

    private fun TypedArray.setupCanal(isRoundTips: Boolean) {
        canal.apply {
            width = getDimension(R.styleable.ArcCircleProgressBar_canalWidth, 10f)
            startAngle = getFloat(R.styleable.ArcCircleProgressBar_canalStartAngle, 0f)
            endAngle = getFloat(R.styleable.ArcCircleProgressBar_canalEndAngle, 360f)
            color = getColor(R.styleable.ArcCircleProgressBar_canalColor, Color.GREEN)

            showGradient(
                this,
                isShowRes = R.styleable.ArcCircleProgressBar_canalShowGradient,
                positionsResId = R.styleable.ArcCircleProgressBar_canalGradientPositions,
                colorsRes = R.styleable.ArcCircleProgressBar_canalGradientColors,
                angleIndex = R.styleable.ArcCircleProgressBar_canalGradientAngle,
                radiusIndex = R.styleable.ArcCircleProgressBar_canalGradientWidth,
                tileModeIndex = R.styleable.ArcCircleProgressBar_canalGradientTileMode
            )

            tip.radius = width / 2
            tip.color = color
            this.isRoundTips = isRoundTips
        }
    }



    private fun TypedArray.showGradient(
        arc: Arc,
        isShowRes: Int,
        positionsResId: Int,
        colorsRes: Int,
        angleIndex: Int,
        radiusIndex: Int,
        tileModeIndex: Int = 2
    ) {
        if (getBoolean(isShowRes, false)) {
            val (x, y) = chords(angleIndex, radiusIndex)
            val shader = LinearGradient(
                0f, 0f, x, y,
                colors(colorsRes),
                positions(positionsResId),
                Shader.TileMode.values()[
                        getInt(tileModeIndex, 2)
                ]
            )
            arc.paint.shader = shader
            arc.tip.paint.shader = shader
        }
    }

    private fun TypedArray.chords(
        angleIndex: Int,
        radiusIndex: Int
    ): Pair<Float, Float> {
        val degrees = getFloat(angleIndex, 0f)
        val radius = getDimension(radiusIndex, 200f)

        val x = radius * cos(degrees * PI.toFloat() / 180)
        val y = radius * sin(degrees * PI.toFloat() / 180)
        return Pair(x, y)
    }

    private fun TypedArray.colors(
        colorsRes: Int
    ): IntArray {
        val colorsRef = getResourceId(colorsRes, 0)
        if (colorsRef == 0) return intArrayOf(Color.RED, Color.GREEN, Color.BLUE)

        val colors = resources.obtainTypedArray(colorsRef)
        val intArray = colors.toIntArray()
        colors.recycle()
        return intArray
    }

    private fun TypedArray.positions(
        resId: Int
    ): FloatArray? {
        val positionsRef = getResourceId(resId, 0)
        if (positionsRef == 0) return null

        val positionsTyped = resources.obtainTypedArray(positionsRef)
        val positions = positionsTyped.toFloatArray()

        positionsTyped.recycle()
        return positions
    }



    private fun TypedArray.toIntArray() : IntArray{
        val array = IntArray(length())
        for (i in 0 until length()){
            array[i] = getInt(i, 0)
        }
        return array
    }

    private fun TypedArray.toFloatArray() : FloatArray {
        val array = FloatArray(length())
        for (i in 0 until length()){
            array[i] = getFloat(i, 0f)
        }
        return array
    }






    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        setMeasuredDimension(widthMeasureSpec + (paddingLeft + paddingRight),
            heightMeasureSpec + (paddingTop + paddingBottom))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        oval.apply {
            val side = min(width - (paddingLeft + paddingRight), height - (paddingTop + paddingBottom))
            val radius = side / 2 - maxOf(indicator.width, canal.width) / 2
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

    abstract class Arc{
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


        class Indicator(
            private val view: View
        ) : Arc(){
            var progress = 0f
            set(value) {
                field = value
                view.invalidate()
            }

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
        val paint = Paint()

        internal fun updatePaint(){
            paint.apply {
                color = this@Circle.color
                isAntiAlias = false
            }
        }

        internal fun draw(canvas: Canvas, outAngle: Float, view: View, outOffset: Float){
            canvas.apply {
                val outRadius = min(width - (view.paddingLeft + view.paddingRight),
                    height - (view.paddingTop + view.paddingBottom)) / 2f - radius  - outOffset / 2
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
}