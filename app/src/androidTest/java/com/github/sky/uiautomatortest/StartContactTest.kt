package com.github.sky.uiautomatortest

import android.support.test.InstrumentationRegistry
import android.support.test.filters.SdkSuppress
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import org.junit.Before
import org.junit.runner.RunWith
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.test.uiautomator.By
import android.support.test.uiautomator.Until
import org.junit.Assert.*
import org.junit.Test


/**
 * Created by fuyuxian on 2018/2/1.
 */

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class StartContactTest {

    private val packageName = "com.android.contacts"
    private val timeOut = 5000L
    private lateinit var deveice: UiDevice

    @Before
    fun setup() {
        deveice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        deveice.pressHome()

        val launcherPackage = getLauncherPackageName()
        assert(launcherPackage.isNotEmpty())
        deveice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), timeOut)


        val context = InstrumentationRegistry.getContext()

        context.packageManager
                .getLaunchIntentForPackage(packageName)
                .apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }.run {
                    context.startActivity(this)
                }

        deveice.wait(Until.hasObject(By.pkg(packageName).depth(0)), timeOut)

    }

    @Test
    fun addContactTest() {
        deveice.findObject(By.descContains("add"))
                ?.click()
        Thread.sleep(500)
        deveice.swipe(100, 800, 100, 400, 10)
        Thread.sleep(1000)
        deveice.pressBack()
        Thread.sleep(500)
    }

    private fun getLauncherPackageName(): String {
        // Create launcher Intent
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)

        // Use PackageManager to get the launcher package name
        val pm = InstrumentationRegistry.getContext().packageManager
        val resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo.activityInfo.packageName
    }
}