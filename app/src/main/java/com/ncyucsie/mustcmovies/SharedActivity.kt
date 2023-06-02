package com.ncyucsie.mustcmovies

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.ncyucsie.mustcmovies.SharedFragments.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import com.ncyucsie.mustcmovies.databinding.LoFiLayoutBinding
import com.ncyucsie.mustcmovies.databinding.NeonLayoutBinding

class SharedActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var fabBack: FloatingActionButton
    private lateinit var fabDownload: FloatingActionButton
    private lateinit var fabShare: FloatingActionButton
    private lateinit var tabLayout: TabLayout
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shared_page)

        supportActionBar?.hide()
        componentInitial()
        listenerInitial()
    }

    private fun componentInitial() {
        viewPager = findViewById(R.id.sharedViewPager)

        fabBack = findViewById(R.id.fabBack)
        fabDownload = findViewById(R.id.fabDownload)
        fabShare = findViewById(R.id.fabShare)
        tabLayout = findViewById(R.id.tabLayout)
        constraintLayout = findViewById(R.id.constraintLayout)

        tabLayout.setupWithViewPager(viewPager)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun listenerInitial() {
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        fabBack.setOnClickListener {
            this.finish()
        }

        fabDownload.setOnClickListener {
            val bitmap = shotFormView(constraintLayout as View)

            if (bitmap != null) {
                saveOrShare(bitmap, 0)
            }
        }

        fabShare.setOnClickListener {
            val bitmap = shotFormView(constraintLayout as View)

            if (bitmap != null) {
                saveOrShare(bitmap, 1)
            }
        }
    }

    private fun shareToInstagram(imageUri: Uri?) {
        val intent = Intent("com.instagram.share.ADD_TO_STORY")
        //val sourceApplication = "637650711602068"
        val backgroundAssetUri = Uri.parse(imageUri.toString())
        intent.setDataAndType(backgroundAssetUri, "image/jpeg")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        startActivity(intent)
    }

    private fun shotFormView(view: View): Bitmap? {
        var screenshot: Bitmap? = null
        try {
            screenshot = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(screenshot)
            view.draw(canvas)
        } catch (e: Exception) {
            Log.d("Screen Shot Error", e.toString())
        }

        return screenshot
    }

    /**
     * mode: 0 -> save
     *       1 -> share
     */
    private fun saveOrShare(bitmap: Bitmap, mode: Int) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
                if (mode == 1) {
                    shareToInstagram(imageUri)
                }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            if (mode == 0) {
                Toast.makeText(this, "已儲存成圖片！" , Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "正在分享至 Instagram！" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int) : Fragment {
        val fragment = when (position) {
            0 -> LofiFragment()
            1 -> NeonFragment()
            else -> TotoroFragment()
        }

        return fragment
    }

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Lofi"
        1 -> "Neon"
        else -> "Totoro"
    }

    override fun getItemPosition(`object`: Any): Int {
        notifyDataSetChanged()

        return super.getItemPosition(`object`)
    }

    override fun getCount() = 3
}