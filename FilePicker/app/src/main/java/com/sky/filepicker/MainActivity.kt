package com.sky.filepicker

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import com.sky.filepicker.upload.Constants
import com.sky.filepicker.upload.LocalUpdateActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.setOnClickListener {
            val intent = Intent(this, LocalUpdateActivity::class.java)
            intent.putExtra("maxNum", 3)
            startActivityForResult(
                intent,
                Constants.UPLOAD_FILE_REQUEST
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Constants.UPLOAD_FILE_RESULT && requestCode == Constants.UPLOAD_FILE_REQUEST) {
            val pathList = data?.getStringArrayListExtra("pathList")
            if (pathList != null && pathList.isNotEmpty()) {
                for (path in pathList) {
                    Log.e("filePicker", "$path")

                    val f = File(path)
                    if (f.exists()) {
                        var fis: FileInputStream? = null
                        try {
                            fis = FileInputStream(f)
                            val time: String = SimpleDateFormat("yyyy-MM-dd")
                                .format(Date(f.lastModified()))
                            println("文件文件创建时间$time")
                            Log.e("filePicker","文件大小:" + ShowLongFileSzie(f.length())) // 计算文件大小
                            // B,KB,MB,
                            Log.e("filePicker","文件大小:" + fis.available().toString() + "B")
                            Log.e("filePicker","文件名称：" + f.getName())
                            Log.e("filePicker","文件是否存在：" + f.exists())
                            Log.e("filePicker","文件的相对路径：" + f.getPath())
                            Log.e("filePicker","文件的绝对路径：" + f.getAbsolutePath())
                            Log.e("filePicker","文件可以读取：" + f.canRead())
                            Log.e("filePicker","文件可以写入：" + f.canWrite())
                            Log.e("filePicker","文件上级路径：" + f.getParent())
                            Log.e("filePicker","文件大小：" + f.length().toString() + "B")
                            Log.e("filePicker","文件最后修改时间：" + Date(f.lastModified()))
                            Log.e("filePicker","是否是文件类型：" + f.isFile())
                            Log.e("filePicker","是否是文件夹类型：" + f.isDirectory())
                            Log.e("filePicker" , "文件内容 ${String(f.readBytes(), StandardCharsets.UTF_8)}")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    /****
     * 计算文件大小
     *
     * @param length
     * @return
     */
    fun ShowLongFileSzie(length: Long): String? {
        return if (length >= 1048576) {
            (length / 1048576).toString() + "MB"
        } else if (length >= 1024) {
            (length / 1024).toString() + "KB"
        } else if (length < 1024) {
            length.toString() + "B"
        } else {
            "0KB"
        }
    }
}