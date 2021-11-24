package me.xcyoung.opengl.camera.widget.camera

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import me.xcyoung.opengl.camera.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @author ChorYeung
 * @since 2021/11/24
 */
class ScreenFilter(context: Context) {
    private val vPosition: Int
    private val vCoord: Int
    private val vTexture: Int
    private val vMatrix: Int
    private var mtx: FloatArray = FloatArray(16)
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private val textureBuffer: FloatBuffer
    private val vertexBuffer: FloatBuffer

    //顶点坐标
    private val VERTEX = floatArrayOf(
        -1.0f, -1.0f,
        1.0f, -1.0f,
        -1.0f, 1.0f,
        1.0f, 1.0f
    )

    //纹理坐标
    private val TEXTURE = floatArrayOf(
        0.0f, 0.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f
//        1f, 1f,
//        1f, 0f,
//        0f, 1f,
//        0f, 0f
    )

    private val program: Int

    init {
        vertexBuffer = ByteBuffer.allocateDirect(4 * 4 * 2)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexBuffer.clear()
        vertexBuffer.put(VERTEX)

        textureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        textureBuffer.clear()
        textureBuffer.put(TEXTURE)

        val vertexShader = OpenGLUtils.readRawTextFile(context, R.raw.camera_vert)
        val textureShader = OpenGLUtils.readRawTextFile(context, R.raw.camera_frag)

        program = OpenGLUtils.loadProgram(vertexShader, textureShader)

        vPosition = GLES20.glGetAttribLocation(program, "vPosition")
        vCoord = GLES20.glGetAttribLocation(program, "vCoord")
        vTexture = GLES20.glGetUniformLocation(program, "vTexture")
        vMatrix = GLES20.glGetUniformLocation(program, "vMatrix")
    }

    fun onDrawFrame(textureId: Int): Int {
        // 1.设置窗口大小
        GLES20.glViewport(0, 0, mWidth, mHeight)
        // 2.使用着色器程序
        GLES20.glUseProgram(program)
        // 3.给着色器程序中传值
        // 3.1 给顶点坐标数据传值
        vertexBuffer.position(0)
        GLES20.glVertexAttribPointer(vPosition, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        // 激活
        GLES20.glEnableVertexAttribArray(vPosition)
        // 3.2 给纹理坐标数据传值
        textureBuffer.position(0)
        GLES20.glVertexAttribPointer(vCoord, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)
        GLES20.glEnableVertexAttribArray(vCoord)

        // 3.3 变化矩阵传值
        GLES20.glUniformMatrix4fv(vMatrix, 1, false, mtx, 0)

        // 3.4 给片元着色器中的 采样器绑定
        // 激活图层
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        // 图像数据
        GLES20.glBindTexture(GLES11Ext.GL_SAMPLER_EXTERNAL_OES, textureId)
        // 传递参数
        GLES20.glUniform1i(vTexture, 0)

        //参数传递完毕,通知 opengl开始画画
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        // 解绑
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        return textureId
    }

    fun setTransformMatrix(mtx: FloatArray) {
        this.mtx = mtx
    }

    fun onReady(width: Int, height: Int) {
        mWidth = width
        mHeight = height
    }

    fun release() {
        GLES20.glDeleteProgram(program)
    }
}