package me.xcyoung.opengl.camera.widget.camera

interface Filter {
    fun onDrawFrame(textureId: Int): Int
    fun setTransformMatrix(mtx: FloatArray)
    fun onReady(width: Int, height: Int)
}