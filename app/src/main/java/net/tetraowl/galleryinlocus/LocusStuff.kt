package net.tetraowl.galleryinlocus

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.media.ExifInterface
import android.util.Log
import locus.api.android.ActionDisplayPoints
import locus.api.android.ActionDisplayVarious
import locus.api.android.objects.PackPoints
import locus.api.android.utils.LocusUtils
import locus.api.objects.extra.Location
import locus.api.objects.geoData.Point
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess

class LocusStuff(private val context: Context) {
    fun checkLocus() {
        val lv = LocusUtils.getActiveVersion(context)
        if (lv == null) {
            AlertDialog.Builder(context)
                .setTitle("Could not start")
                .setMessage(context.getString(R.string.locus_not_found))
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    exitProcess(0)
                }
                .setCancelable(false)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .show()
        }
    }

    fun imageRefresher() {
        Thread {
            val storage = Storage(context)
            val sendPoints = mutableListOf<String>()
            storage.paths.forEach { path: String ->
                for (i in getImageFiles(path)) {
                    if (i.exists() && i.isFile && Files.probeContentType(i.toPath()).contains("image/")) {
                        Log.d("Files", "Found image:" + i.name)
                        val exif = ExifInterface(i);
                        val coords = GeoDegree(exif)
                        if (coords.Latitude != null&& coords.Longitude!= null) {
                            val pw = PackPoints("galleryinlocus${Math.random()}")
                            if(exif.thumbnailBitmap==null) {
                                return@Thread
                            }
                            pw.bitmap = Bitmap.createBitmap(exif.thumbnailBitmap, 0, 0, 100, 100)
                            val loc = Location()
                            loc.latitude = coords.Latitude.toDouble()
                            loc.longitude = coords.Longitude.toDouble()
                            val point = Point("Photo ${i.name}", loc)
                            point.addAttachmentPhoto(i.path)
                            point.parameterDescription = """
                            Captured at: ${exif.getAttribute(ExifInterface.TAG_DATETIME)}
                            
                        """.trimIndent()
                            pw.addPoint(point)
                            // send data
                            sendPoints.add(pw.name)
                            ActionDisplayPoints.sendPackSilent(context, pw,false)
                        }

                    }
                }
            }

        }.start()
    }

    fun deleteOld() {
        val storage = Storage(context)
        val ids = storage.ids.toMutableList()
        val list = mutableListOf<String>()
        ids.forEach {
            val invisible = PackPoints(it)
            ActionDisplayPoints.sendPackSilent(context, invisible, false)
        }
        ids.removeAll(list)
        storage.ids = ids
    }

    fun getImageFiles(path: String): List<File> {
        val images = mutableListOf<File>()
        val directory: File = File(path)
        val files = directory.listFiles()
        if (files!= null) {
            for (i in files) {
                if (i.exists() && i.isFile && Files.probeContentType(i.toPath()).contains("image/")) {
                    images.add(i)
                }
            }
        }
        return images;
    }
}
