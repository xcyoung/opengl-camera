package me.xcyoung.opengl.camera.widget.camera

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import me.xcyoung.opengl.camera.R

/**
 * @author ChorYeung
 * @since 2021/11/24
 */
class GLCameraView : GLSurfaceView {
    companion object {
        const val RATIO: Float = 3.0f / 4.0f
    }

    private val cameraXController: CameraXController = CameraXController()
    private val callback = object : GLCameraRender.Callback {
        override fun onSurfaceChanged() {
            setUpCamera()
        }

        override fun onFrameAvailable() {
            requestRender()
        }
    }
    private val cameraRender = GLCameraRender(context, callback)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.GLCameraView)
        val type = typeArray.getString(R.styleable.GLCameraView_type) ?: "Normal"
        cameraRender.type = type
        typeArray.recycle()
    }

    init {
        setEGLContextClientVersion(2)
        setRenderer(cameraRender)

        renderMode = RENDERMODE_WHEN_DIRTY
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
//        val heightSpec =
//            MeasureSpec.makeMeasureSpec(((widthSpecSize / RATIO).toInt()), MeasureSpec.EXACTLY)
//        super.onMeasure(widthMeasureSpec, heightSpec)
//    }

    private fun setUpCamera() {
        cameraXController.setUpCamera(context, cameraRender)
    }

    fun setProgress(progress: Float) {
        cameraRender.setProgress(progress)
    }

    fun setTint(progress: Float) {
        cameraRender.setTint(progress)
    }
}