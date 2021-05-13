package com.sky.filepicker.upload

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.sky.filepicker.R
import kotlinx.android.synthetic.main.activity_local_upload.*

class LocalUpdateActivity : AppCompatActivity(), View.OnClickListener {
    private var storageFragment: StorageFragment? = null
    val pathList = ArrayList<String>()
    var maxNum: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_upload)

        maxNum = intent.getIntExtra("maxNum",1)
        tv_back.setOnClickListener(this)
        tv_upload.setOnClickListener(this)
        checkPermission()
    }

    override fun onClick(v: View?) {
        when (v) {
            tv_back -> finish()
            tv_upload -> upload()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var isAllAgree = true
        for (i in grantResults.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAllAgree = false
            }
        }

        if (!isAllAgree) {
            Toast.makeText(this, getString(R.string.tips_Storage_permissions), Toast.LENGTH_SHORT).show()
        } else {
            goNext()
        }
    }

    private fun checkPermission() {
        var needReq = false
        for (i in Constants.PERMISSIONS_STORAGE.indices) {
            if (PackageManager.PERMISSION_GRANTED !=
                ContextCompat.checkSelfPermission(
                    this@LocalUpdateActivity,
                    Constants.PERMISSIONS_STORAGE[i]
                )
            ) {
                needReq = true
            }
        }
        if (needReq) {
            ActivityCompat.requestPermissions(
                this,
                Constants.PERMISSIONS_STORAGE,
                Constants.REQUEST_EXTERNAL_STORAGE
            )
        } else {
            goNext()
        }
    }

    private fun goNext() {
        storageFragment =
            StorageFragment.newInstance(Environment.getExternalStorageDirectory().absolutePath)
        storageFragment?.let { storageFragment ->
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fl_container, storageFragment)
                commit()
            }
        }
    }

    private fun upload() {
        if (pathList.size <= 0) {
            Toast.makeText(this, getString(R.string.tips_No_file_selected), Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent()
        //此处返回的列表数据
        intent.putExtra("pathList", pathList)
        setResult(Constants.UPLOAD_FILE_RESULT, intent)
        finish()
    }

    fun setText() {
        if (pathList.size > 0) {
            tv_upload.text = "${getString(R.string.upload)}(${pathList.size})"
        } else {
            tv_upload.text = "${getString(R.string.upload)}"
        }
    }
}