/*
 * YukiHookAPI - An efficient Hook API and Xposed Module solution built in Kotlin.
 * Copyright (C) 2019-2023 HighCapable
 * https://github.com/fankes/YukiHookAPI
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is created by fankes on 2022/8/8.
 * Thanks for providing https://github.com/cinit/QAuxiliary/blob/main/app/src/main/java/io/github/qauxv/lifecycle/Parasitics.java
 */
package com.highcapable.yukihookapi.hook.xposed.parasitic.activity.base

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.highcapable.yukihookapi.hook.factory.injectModuleAppResources
import com.highcapable.yukihookapi.hook.factory.registerModuleAppActivities
import com.highcapable.yukihookapi.hook.xposed.bridge.YukiXposedModule
import com.highcapable.yukihookapi.hook.xposed.parasitic.reference.ModuleClassLoader

/**
 * 代理 [AppCompatActivity]
 *
 * 继承于此类的 [Activity] 可以同时在宿主与模块中启动
 *
 * - 在 (Xposed) 宿主环境需要在宿主启动时调用 [Context.registerModuleAppActivities] 进行注册
 *
 * - 在 (Xposed) 宿主环境需要重写 [moduleTheme] 设置 AppCompat 主题 - 否则会无法启动
 */
open class ModuleAppCompatActivity : AppCompatActivity() {

    /**
     * 设置当前代理的 [Activity] 类名
     *
     * 留空则使用 [Context.registerModuleAppActivities] 时设置的类名
     *
     * - 代理的 [Activity] 类名必须存在于宿主的 AndroidMainifest 清单中
     * @return [String]
     */
    open val proxyClassName get() = ""

    /**
     * 设置当前代理的 [Activity] 主题
     * @return [Int]
     */
    open val moduleTheme get() = -1

    override fun getClassLoader(): ClassLoader? = ModuleClassLoader.instance()

    @CallSuper
    override fun onConfigurationChanged(newConfig: Configuration) {
        if (YukiXposedModule.isXposedEnvironment) injectModuleAppResources()
        super.onConfigurationChanged(newConfig)
    }

    @CallSuper
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.getBundle("android:viewHierarchyState")?.classLoader = classLoader
        super.onRestoreInstanceState(savedInstanceState)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        if (YukiXposedModule.isXposedEnvironment && moduleTheme != -1) setTheme(moduleTheme)
        super.onCreate(savedInstanceState)
    }
}