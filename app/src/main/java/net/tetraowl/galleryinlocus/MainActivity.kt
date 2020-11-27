package net.tetraowl.galleryinlocus

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ExifInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.android.synthetic.main.activity_main.*
import locus.api.android.ActionBasics
import locus.api.android.ActionDisplayPoints
import locus.api.android.ActionFiles
import locus.api.android.objects.PackPoints
import locus.api.android.utils.LocusUtils
import locus.api.objects.extra.Location
import locus.api.objects.geoData.Point
import net.tetraowl.galleryinlocus.activities.About
import net.tetraowl.galleryinlocus.activities.OpenSourceLicences
import net.tetraowl.galleryinlocus.listview.ListAdapter
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    private var addPathButton: LinearLayout?=null
    private var button: SwitchMaterial?=null
    private val storage = Storage(this)
    private val locus = LocusStuff(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locus.checkLocus()
        initList()
        this.button = findViewById(R.id.state)
        this.button?.setOnCheckedChangeListener { compoundButton, boolean ->
            if (!boolean) {
                locus.deleteOld()
            } else {
                locus.imageRefresher()
            }
        }
        this.addPathButton = findViewById(R.id.addPath)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 126);

        addPathButton?.setOnClickListener {
            ActionFiles.actionPickDir(this, 1, "Pick a photo location!")
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode ==1) {
            val any = if (resultCode == Activity.RESULT_OK && data != null) {
                val dir = File(data.data!!.path)
                if (!dir.exists()) {
                    Toast.makeText(this, getString(R.string.select_exist), Toast.LENGTH_LONG).show()
                }
                storage.addPath(data.data!!.path!!)
                (list.adapter as ListAdapter).updateDataset(storage.paths)
                list.adapter?.notifyDataSetChanged()
                locus.imageRefresher()
            } else {
                Toast.makeText(this, R.string.process_failed, Toast.LENGTH_LONG)
            }
        }
    }

    fun initList() {

        val viewManager = LinearLayoutManager(this)
        val viewAdapter = ListAdapter(storage.paths.toMutableList(), this.storage)

        val recyclerView = findViewById<RecyclerView>(R.id.list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.library -> {
                startActivity(Intent(this, OpenSourceLicences::class.java))
                return true
            }
            R.id.about -> {
                startActivity(Intent(this, About::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
