package yinkio.android.customView.progressBar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlinx.coroutines.*
import yinkio.android.customView.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class ArcCircleProgressBar : View {



    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val oval = RectF()

    val indicator = Arc.Indicator(this)
    val canal = Arc.Canal()

    var progress: Float = 0f
    set(value) {
        field = when{
            value > 100f -> 100f
            value < 0 -> 0f
            else -> value
        }
        indicator.progress = value
        invalidate()
    }

    var progressScale: Float = 0.95f
    var shadowLeft: Float = 10f
    var shadowTop: Float = 10f
    var shadowRight: Float = 10f
    var shadowBottom: Float = 10f

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    init {
        addOnAttachStateChangeListener(object : OnAttachStateChangeListener{
            override fun onViewAttachedToWindow(p0: View?) {}

            override fun onViewDetachedFromWindow(p0: View?) {
                coroutineScope.cancel()
            }
        })
    }


    private fun init(context: Context?, attrs: AttributeSet?){
        context?.withStyledAttributes(
            attrs,
            R.styleable.ArcCircleProgressBar,
            0
        ){
            val isRoundTips = getBoolean(R.styleable.ArcCircleProgressBar_roundTips, true)
            progressScale = getFloat(R.styleable.ArcCircleProgressBar_progressScale, 0.95f)

            progress = getFloat(R.styleable.ArcCircleProgressBar_progress, 0f)

            shadowLeft = getFloat(R.styleable.ArcCircleProgressBar_shadowLeft, 10f)
            shadowTop = getFloat(R.styleable.ArcCircleProgressBar_shadowTop, 10f)
            shadowRight = getFloat(R.styleable.ArcCircleProgressBar_shadowRight, 10f)
            shadowBottom = getFloat(R.styleable.ArcCircleProgressBar_shadowBottom, 10f)

            setupIndicator(isRoundTips)
            setupCanal(isRoundTips)

        }
    }




    private fun TypedArray.setupIndicator(isRoundCaps: Boolean) {
        indicator.apply {
            progress = getFloat(R.styleable.ArcCircleProgressBar_progress, 0f)
            width = getDimension(R.styleable.ArcCircleProgressBar_indicatorWidth, 10f)
            startAngle = getFloat(R.styleable.ArcCircleProgressBar_indicatorStartAngle, 0f)
            endAngle = getFloat(R.styleable.ArcCircleProgressBar_indicatorEndAngle, 360f)
            color = getColor(R.styleable.ArcCircleProgressBar_indicatorColor, Color.BLUE)


            val hasGradient = getBoolean(R.styleable.ArcCircleProgressBar_indicatorDrawGradient, false)
            if (hasGradient){
                indicator.paint.shader =
                    gradient(
                        positionsResId = R.styleable.ArcCircleProgressBar_indicatorGradientPositions,
                        colorsRes = R.styleable.ArcCircleProgressBar_indicatorGradientColors,
                        angleIndex = R.styleable.ArcCircleProgressBar_indicatorGradientAngle,
                        radiusIndex = R.styleable.ArcCircleProgressBar_indicatorGradientWidth,
                        tileModeIndex = R.styleable.ArcCircleProgressBar_indicatorGradientTileMode
                    )
            }

            val hasShadow = getBoolean(R.styleable.ArcCircleProgressBar_indicatorHasShadow, false)
            if (hasShadow){
                this.hasShadow = hasShadow
                shadowPaint.apply {

                    val hasShadowGradient = getBoolean(R.styleable.ArcCircleProgressBar_indicatorShadowDrawGradient, false)

                    if (hasShadowGradient){
                        shader = gradient(
                            positionsResId = R.styleable.ArcCircleProgressBar_indicatorShadowGradientPositions,
                            colorsRes = R.styleable.ArcCircleProgressBar_indicatorShadowGradientColors,
                            angleIndex = R.styleable.ArcCircleProgressBar_indicatorShadowGradientAngle,
                            radiusIndex = R.styleable.ArcCircleProgressBar_indicatorShadowGradientWidth,
                            tileModeIndex = R.styleable.ArcCircleProgressBar_indicatorShadowGradientTileMode
                        )
                    }

                    color = getColor(R.styleable.ArcCircleProgressBar_indicatorShadowColor, Color.GRAY)
                }
                shadowBlurRadius = getFloat(R.styleable.ArcCircleProgressBar_indicatorShadowBlurValue, 25f)



                setupShadowPaint()
            }


            setupCap(isRoundCaps)
            this.isRoundCaps = isRoundCaps
        }
    }



    private fun TypedArray.setupCanal(isRoundCaps: Boolean) {
        canal.apply {
            width = getDimension(R.styleable.ArcCircleProgressBar_canalWidth, 10f)
            startAngle = getFloat(R.styleable.ArcCircleProgressBar_canalStartAngle, 0f)
            endAngle = getFloat(R.styleable.ArcCircleProgressBar_canalEndAngle, 360f)
            color = getColor(R.styleable.ArcCircleProgressBar_canalColor, Color.GREEN)

            val hasGradient = getBoolean(R.styleable.ArcCircleProgressBar_canalDrawGradient, false)
            if (hasGradient){
                canal.paint.shader = gradient(
                    positionsResId = R.styleable.ArcCircleProgressBar_canalGradientPositions,
                    colorsRes = R.styleable.ArcCircleProgressBar_canalGradientColors,
                    angleIndex = R.styleable.ArcCircleProgressBar_canalGradientAngle,
                    radiusIndex = R.styleable.ArcCircleProgressBar_canalGradientWidth,
                    tileModeIndex = R.styleable.ArcCircleProgressBar_canalGradientTileMode
                )
            }

            val hasShadow = getBoolean(R.styleable.ArcCircleProgressBar_canalHasShadow, false)
            if (hasShadow){
                this.hasShadow = hasShadow
                shadowPaint.apply {

                    val hasShadowGradient = getBoolean(R.styleable.ArcCircleProgressBar_canalShadowDrawGradient, false)
                    if (hasShadowGradient){
                        shader = gradient(
                            positionsResId = R.styleable.ArcCircleProgressBar_canalShadowGradientPositions,
                            colorsRes = R.styleable.ArcCircleProgressBar_canalShadowGradientColors,
                            angleIndex = R.styleable.ArcCircleProgressBar_canalShadowGradientAngle,
                            radiusIndex = R.styleable.ArcCircleProgressBar_canalShadowGradientWidth,
                            tileModeIndex = R.styleable.ArcCircleProgressBar_canalShadowGradientTileMode
                        )
                    }

                    color = getColor(R.styleable.ArcCircleProgressBar_canalShadowColor, Color.GRAY)
                }

                shadowBlurRadius = getFloat(R.styleable.ArcCircleProgressBar_indicatorShadowBlurValue, 25f)

                setupShadowPaint()
            }


            setupCap(isRoundCaps)
            this.isRoundCaps = isRoundCaps
        }
    }

    private fun Arc.setupShadowPaint() {
        shadowPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = width
            strokeCap = Paint.Cap.ROUND
        }
    }

    private fun Arc.setupCap(
        isRoundCaps: Boolean
    ) {
        paint.strokeCap = cap(isRoundCaps)
        shadowPaint.strokeCap = cap(isRoundCaps)
    }

    private fun cap(isRoundTips: Boolean) = if (isRoundTips) {
        Paint.Cap.ROUND
    } else {
        Paint.Cap.BUTT
    }


    private fun TypedArray.gradient(
        positionsResId: Int,
        colorsRes: Int,
        angleIndex: Int,
        radiusIndex: Int,
        tileModeIndex: Int = 2
    ): Shader {
        val (x, y) = chords(angleIndex, radiusIndex)
        return LinearGradient(
            0f, 0f, x, y,
            colors(colorsRes),
            positions(positionsResId),
            Shader.TileMode.values()[
                    getInt(tileModeIndex, 2)
            ]
        )
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        coroutineScope.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f + paddingLeft / 2 - paddingRight / 2
        val centerY = height / 2f + paddingTop / 2 - paddingBottom / 2
        val side = min(width - (paddingLeft + paddingRight), height - (paddingTop + paddingBottom))
            .toFloat()
        val radius = side / 2 - maxOf(indicator.width, canal.width) / 2



        canvas.apply {

            oval.apply {
                val newRadius = radius * progressScale
                set(centerX + shadowLeft - newRadius,
                    centerY + shadowTop - newRadius,
                    centerX + shadowRight + newRadius,
                    centerY + shadowBottom + newRadius)
            }

            coroutineScope.launch {
                val canalShadow = if (canal.hasShadow){
                    coroutineScope.async {
                        shadowBitmap(blurRadius = canal.shadowBlurRadius) {
                            drawCanal(it, canal.shadowPaint)
                        }
                    }
                } else null

                val indicatorShadow = if (indicator.hasShadow){
                    coroutineScope.async {
                        shadowBitmap(blurRadius = indicator.shadowBlurRadius){
                            drawIndicator(it, indicator.shadowPaint)
                        }
                    }
                } else null

                canalShadow?.let { drawBitmap(it.await(), 0f, 0f, null) }
                indicatorShadow?.let { drawBitmap(it.await(), 0f, 0f, null) }
            }






            oval.apply {
                val newRadius = radius * progressScale
                set(centerX - newRadius,
                    centerY - newRadius,
                    centerX + newRadius,
                    centerY + newRadius)
            }

            drawCanal(this, canal.paint)
            drawIndicator(this, indicator.paint)
        }

    }

    private fun shadowBitmap(
        blurRadius: Float = 25f,
        draw: (Canvas) -> Unit
    ): Bitmap {
        val shadowBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val shadowCanvas = Canvas(shadowBitmap)
        draw(shadowCanvas)

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val rs = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.RGBA_8888(rs))
        val inAlloc = Allocation.createFromBitmap(rs, shadowBitmap)
        val outAlloc = Allocation.createFromBitmap(rs, output)

        blurScript.setRadius(blurRadius)
        blurScript.setInput(inAlloc)
        blurScript.forEach(outAlloc)
        outAlloc.copyTo(output)
        rs.destroy()
        return output
    }

    private fun drawCanal(
        canvas: Canvas,
        paint: Paint
    ) {
        canal.apply {
            updatePaints()
            draw(canvas, oval, this@ArcCircleProgressBar, paint)
        }
    }

    private fun drawIndicator(
        canvas: Canvas,
        paint: Paint
    ) {
        indicator.apply {

            updatePaints()
            draw(canvas, oval, this@ArcCircleProgressBar, paint)
        }
    }

    abstract class Arc{
        open var width: Float = 10f
        open var color: Int = Color.BLACK
        open var startAngle: Float = 0f
        open var endAngle: Float = 360f
        open var isRoundCaps = true
        open var hasShadow = false
        open var shadowBlurRadius = 0f
        open val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)


        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun updatePaints(){
            paint.apply {
                color = this@Arc.color
                strokeWidth = width
                style = Paint.Style.STROKE
            }
        }

        open fun draw(canvas: Canvas, oval: RectF, view: View, paint: Paint){
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

            override fun draw(canvas: Canvas, oval: RectF, view: View, paint: Paint){
                canvas.apply {
                    drawArc(oval, startAngle, progressAngle, false, paint)
                }
            }
        }

        class Canal : Arc(){
            override var color: Int = Color.GRAY
        }
    }

    companion object{
        private const val MAX_PROGRESS = 100f
    }
}