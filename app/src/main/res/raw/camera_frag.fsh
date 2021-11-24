#extension GL_OES_EGL_image_external : require
//SurfaceTexture比较特殊
//float数据是什么精度的
precision mediump float;

//采样点的坐标
varying vec2 aCoord;

//采样器
uniform samplerExternalOES vTexture;

void main() {
    //变量 接收像素值
    // texture2D：采样器 采集 aCoord的像素
    //赋值给 gl_FragColor 就可以了
    /// 正常
        gl_FragColor = texture2D(vTexture, aCoord);
        vec4 rgba = texture2D(vTexture, aCoord);

    /// 灰度图 305911
    //    float c = rgba.r*0.3+rgba.g*0.59+rgba.b*0.11;
    //    gl_FragColor = vec4(c, c, c, rgba.a);

    /// x方向2分屏
    //    float x = aCoord.x;
    //    if (x < 0.5) {
    //        x += 0.25;
    //    } else {
    //        x -= 0.25;
    //    }
    //    gl_FragColor = texture2D(vTexture, vec2(x, aCoord.y));

    /// x方向3分屏
    //    float x = aCoord.x;
    //    if (x < (1.0 / 3.0)) {
    //        x += (1.0 / 3.0);
    //    } else if (x > (2.0 / 3.0)) {
    //        x -= (1.0 / 3.0);
    //    }
    //    gl_FragColor = texture2D(vTexture, vec2(x, aCoord.y));
}

