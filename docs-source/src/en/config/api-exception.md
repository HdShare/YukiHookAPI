---
pageClass: hidden-anchor-page
---

# API Exception Handling

> Exceptions are the main problems often encountered in the development process. Here are some common exceptions that may be encountered during the use of `YukiHookAPI` and how to deal with them.

The exception description here will only synchronize the latest API version, and the exception of the older API version will not be described again, please always keep the API version up-to-date.

## Non-Blocking Exceptions

> These exceptions will not cause the app to stop running (FC), but will print `E` level logs on the console, and may also stop continuing to execute related functions.

###### exception

::: danger loggerE

Could not found XposedBridge in current space! Aborted

:::

**Abnormal**

Your Hook Framework is not working or `XposedBridge` has not been successfully loaded.

**Solution**

Please make sure you have loaded the `encase` method of `YukiHookAPI` in the correct place. For details, please refer to [Use as Xposed Module Configs](../config/xposed-using) and [Use as Hook API Configs](../config/api-using).

###### exception

::: danger loggerE

You cannot load a hooker in "onInit" or "onXposedEvent" method! Aborted

:::

**Abnormal**

You try to load the `encase` method in the `onInit` or `onXposedEvent` method of the Hook entry class that implements `IYukiHookXposedInit`.

> The following example

```kotlin
class HookEntry : IYukiHookXposedInit {

    override fun onInit() {
        // ❗ Wrong usage
        YukiHookAPI.encase {
            // Your code here.
        }
    }

    override fun onXposedEvent() {
        // ❗ Wrong usage
        YukiHookAPI.encase {
            // Your code here.
        }
    }

    override fun onHook() {
        // Your code here.
    }
}
```

**Solution**

Please load the `encase` method in the `onHook` method.

> The following example

```kotlin
class HookEntry : IYukiHookXposedInit {

    override fun onInit() {
        // Only the configs method can be loaded here
        YukiHookAPI.configs {
            // Your code here.
        }
    }

    override fun onHook() {
        // ✅ Correct usage
        YukiHookAPI.encase {
            // Your code here.
        }
    }
}
```

###### exception

::: danger loggerE

Hooking Process exception occurred

:::

**Abnormal**

`YukiHookAPI` throws an exception during its own initialization hook.

**Solution**

Usually, this kind of error does not happen easily. If this error occurs, you can directly submit the log for feedback.

###### exception

::: danger loggerE

YukiHookAPI try to load HookEntryClass failed

:::

**Abnormal**

`YukiHookAPI` encountered an unhandled exception or the entry class could not be found when trying to load the hook entry class `onInit` or `onHook` method.

**Solution**

Usually, this kind of error does not occur easily.

If this error occurs, please check the log printed on the console to locate the problem.

After confirming that the problem is not caused by your own code, you can submit the log for feedback.

###### exception

::: danger loggerE

YukiHookAPI bind initZygote failed

:::

**Abnormal**

`YukiHookAPI` encountered an unhandled exception when trying to load the Xposed native interface `initZygote` method.

**Solution**

Usually, this kind of error does not occur easily.

If this error occurs, please check the log printed on the console to locate the problem.

After confirming that the problem is not caused by your own code, you can submit the log for feedback.

###### exception

::: danger loggerE

Failed to execute method "**NAME**", maybe your Hook Framework not support Resources Hook

:::

**Abnormal**

`YukiHookAPI` An error occurred while trying to do a Resources Hook.

**Solution**

Please double check the error log for details.

If a `Resources$NotFoundException` occurs, you may be looking for an incorrect Resources Id.

If `ClassNotFound` or `NoClassDefFoundError` occurs, it may be that Hook Framework does not support Resources Hook.

###### exception

::: danger loggerE

HookClass \[**NAME**\] not found

:::

**Abnormal**

The `Class` currently being hooked was not found.

**Solution**

Please check if the target `Class` exists, to ignore this error use the `ignoredHookClassNotFoundFailure` method.

###### exception

::: danger loggerE

Hook Member \[**NAME**\] failed

:::

**Abnormal**

An error occurred while hooking the target method, constructor.

**Solution**

This problem is usually caused by Hook Framework.

Please check the corresponding log content.

If the problem persists, please bring detailed logs for feedback.

###### exception

::: danger loggerE

Hooked Member with a finding error by **CLASS**

:::

**Abnormal**

After the Hook is executed, the `member` of the Hook is `null` and the target Hook method and constructed class have been set.

**Solution**

Please check the previous error log before this error occurs, maybe there is an error that the method and constructor cannot be found when searching for methods and constructors.

###### exception

::: danger loggerE

Hooked Member cannot be non-null by **CLASS**

:::

**Abnormal**

After the Hook is executed, the `member` of the Hook is `null` and the target Hook method and constructed class are not set.

> The following example

```kotlin
injectMember {
    // There are no search conditions for methods and constructors that require hooks
    afterHook {
        // ...
    }
}
```

**Solution**

Please confirm that you have correctly set the method to be hooked and the way to find the constructor before hooking.

> The following example

```kotlin
injectMember {
    // ✅ Examples of correct usage
    method {
        // Your code here.
    }
    afterHook {
        // ...
    }
}
```

###### exception

::: danger loggerE

Hooked method return type match failed, required \[**TYPE**\] but got \[**TYPE**\]

:::

**Abnormal**

`HookParam.result` is set in the Hook callback method body or `replaceHook` is used but the return value type of the hooked method does not match the original return value type.

> The following example

Suppose this is the method being Hooked.

```java
private boolean test()
```

Below is an error case.

```kotlin
injectMember {
    method {
        name = "test"
        emptyParam()
    }
    // <Scenario 1> Set the wrong type, the original type is Boolean
    beforeHook {
        result = 0
    }
    // <Scenario 2> Return the wrong type, the original type is Boolean
    replaceAny {
        0
    }
    // <Scenario 3> Use the wrong type directly, the original type is Boolean
    replaceTo(any = 0)
}
```

::: warning

If the above scenario occurs in **beforeHook** or **afterHook**, it will cause the Host App to throw an exception from **XposedBridge** (which will expose the fact of being Hooked).

:::

**Solution**

Please confirm the correct return value type of the current Hook method, modify it and try again.

###### exception

::: danger loggerE

Hook initialization failed because got an Exception

:::

**Abnormal**

An arbitrary exception occurred while preparing the Hook.

**Solution**

This is a reminder that an exception occurred during the Hook preparation stage, please carefully check what the specific exception is to re-determine the problem.

###### exception

::: danger loggerE

Try to hook **NAME**\[**NAME**\] got an Exception

:::

**Abnormal**

An arbitrary exception occurred at the start of the Hook.

**Solution**

This is a reminder that an exception occurred at the beginning of the Hook, please check carefully what the specific exception is to re-determine the problem.

###### exception

::: danger loggerE

Method/Constructor/Field match type "**TYPE**" not allowed

:::

**Abnormal**

A disallowed parameter type was set when looking up methods, constructors, and variables.

> The following example

```kotlin
// Find a method
method {
    // ❗ Invalid type example is set
    param(false, 1, 0)
    // ❗ Invalid type example is set
    returnType = false
}

// Find a variable
field {
    // ❗ Invalid type example is set
    type = false
}
```

**Solution**

In the search, `param`, `returnType`, `type` only accept `Class`, `String`, `VariousClass` types, and parameter instances cannot be passed in.

> The following example

```kotlin
// Find a method
method {
    // ✅ Examples of correct usage
    param(BooleanType, IntType, IntType)
    // ✅ Examples of correct usage
    returnType = BooleanType
    // ✅ The following scheme is also correct
    returnType = "java.lang.Boolean"
}

// Find a variable
field {
    // ✅ Examples of correct usage
    type = BooleanType
}
```

###### exception

::: danger loggerE

NoSuchMethod/NoSuchConstructor/NoSuchField happend in \[**NAME**\]

:::

**Abnormal**

The target method, constructor, and variable were not found when looking for methods, constructors, and variables.

**Solution**

Please confirm that your search criteria can correctly match the specified methods, constructors and variables in the target `Class`.

###### exception

::: danger loggerE

Trying **COUNT** times and all failure by RemedyPlan

:::

**Abnormal**

When using `RemedyPlan` to search for methods, constructors, and variables, the methods, constructors, and variables are still not found.

**Solution**

Please confirm the `RemedyPlan` parameter you set and the `Class` that exists in the Host App, and try again.

###### exception

::: danger loggerE

You must set a condition when finding a Method/Constructor/Field

:::

**Abnormal**

No conditions are set when looking for methods, constructors, and variables.

> The following example

```kotlin
method {
    // No conditions are set here
}
```

**Solution**

Please complete your search criteria and try again.

###### exception

::: danger loggerE

Can't find this Class in \[**CLASSLOADER**\]: **CONTENT** Generated by YukiHookAPI#ReflectionTool

:::

**Abnormal**

The `Class` object to be searched for was not found via `ClassLoader.searchClass` or `PackageParam.searchClass`.

> The following example

```kotlin
customClassLoader?.searchClass {
    from(...)
    // ...
}.get()
```

**Solution**

This is a security exception, please check the conditions you set, use the relevant tools to view the `Class` and bytecode object characteristics in the **Dex** and try again.

###### exception

::: danger loggerE

Can't find this Method/Constructor/Field in \[**CLASS**\]: **CONTENT** Generated by YukiHookAPI#ReflectionTool

:::

**Abnormal**

The methods, constructors, and variables that need to be found cannot be found by specifying conditions.

> The following example

```kotlin
TargetClass.method {
    name = "test"
    param(BooleanType)
}
```

**Solution**

This is a security exception, please check the conditions you set, use the relevant tools to view the bytecode object characteristics in the `Class`, and try again.

###### exception

::: danger loggerE

The number of VagueType must be at least less than the count of paramTypes

:::

**Abnormal**

Incorrect use of `VagueType` in `Method`, `Constructor` lookup conditions.

> The following example

```kotlin
TargetClass.method {
    name = "test"
    // <Scenario 1>
    param(VagueType)
    // <Scenario 2>
    param(VagueType, VagueType ...)
}
```

**Solution**

`VagueType` cannot be completely filled in method and constructor parameters. If there is such a requirement, please use `paramCount`.

###### exception

::: danger loggerE

Field match type class is not found

:::

**Abnormal**

An instance of `Class` for `type` was not found in the lookup criteria set when looking up the variable.

> The following example

```kotlin
field {
    name = "test"
    // Assume that the Class of the type set here does not exist
    type = "com.example.TestClass"
}
```

**Solution**

Please check if `Class` of `type` in the lookup condition exists and try again.

###### exception

::: danger loggerE

Method match returnType class is not found

:::

**Abnormal**

An instance of `Class` of `returnType` was not found in the search criteria set when looking up the method.

> The following example

```kotlin
method {
    name = "test"
    // Assume that the Class of returnType set here does not exist
    returnType = "com.example.TestClass"
}
```

**Solution**

Please check if `Class` of `returnType` in the lookup condition exists and try again.

###### exception

::: danger loggerE

Method/Constructor match paramType\[**INDEX**\] class is not found

:::

**Abnormal**

The `Class` instance subscripted by the `index` number of `param` was not found in the search conditions set when searching for methods and constructors.

```kotlin
method {
    name = "test"
    // Assume that the Class with subscript "No.1" set here does not exist
    param(StringType, "com.example.TestClass", BooleanType)
}
```

**Solution**

Please check if the `Class` subscripted by the `index` number of `param` in the lookup condition exists and try again.

###### exception

::: danger loggerE

Resources Hook condition name/type cannot be empty \[**TAG**\]

:::

**Abnormal**

No conditions were set when looking for Resources.

> The following example

```kotlin
// Case 1
conditions {
    // No conditions are set here
}
// Case 2
conditions {
    name = "test"
    // The type condition is missing here
}
```

**Solution**

The Hook of Resources is not a Hook similar to a method.

It must have a complete name and type description in order to find it successfully.

Please complete the search conditions and try again.

###### exception

::: danger loggerE

Resources Hook type is invalid \[**TAG**\]

:::

**Abnormal**

An exception of the wrong type occurred while Hooking Resources.

**Solution**

`YukiHookAPI` will try to load Resources Hook in `initZygote` and `handleInitPackageResources`.

If all loading fails, this exception may occur.

The current Hook Framework needs to support and enable the Resources Hook function, please check and try again.

###### exception

::: danger loggerE

Resources Hook got an Exception \[**TAG**\]

:::

**Abnormal**

An arbitrary exception occurred while Hooking Resources.

**Solution**

This is a summary of exceptions, please check down the log for the specific exception, such as the problem that the Resources Id cannot be found.

###### exception

::: danger loggerE

Received action "**ACTION**" failed

:::

**Abnormal**

Callback broadcast event exception when using `YukiHookDataChannel`.

**Solution**

Under normal circumstances, this error basically does not occur.

Once an error occurs, after eliminating the problem of your own code, please bring detailed logs for feedback.

###### exception

::: danger loggerE

Failed to sendBroadcast like "**KEY**", because got null context in "**PACKAGENAME**"

:::

**Abnormal**

Sending a broadcast when using `YukiHookDataChannel` got an empty context instance.

**Solution**

Under normal circumstances, this error basically does not occur.

In the latest version, the problems that may occur when the host is used have been fixed.

If the latest version still has errors, after eliminating the problem of your own code, please bring detailed logs for feedback.

###### exception

::: danger loggerE

Failed to inject module resources into \[**RESOURCES**\]

:::

**Abnormal**

An exception occurred when injecting Module App's Resources using `injectModuleAppResources` in a (Xposed) Host environment.

**Solution**

Under normal circumstances, this error basically does not occur. After eliminating the problem of your own code, please bring detailed logs for feedback.

###### exception

::: danger loggerE

Activity Proxy initialization failed because got an Exception

:::

**Abnormal**

An exception occurred when injecting a Module App's `Activity` using `registerModuleAppActivities` in a (Xposed) Host environment.

**Solution**

Please check the next error log after this error occurs.

Maybe some problems may have occurred in the configuration parameters.

If you cannot find the description of the relevant error log, after eliminating the problem of your own code, please bring the detailed log for feedback.

###### exception

::: danger loggerE

Activity Proxy got an Exception in msg.what \[**WHAT**\]

:::

**Abnormal**

An exception occurred when injecting a Module App's `Activity` using `registerModuleAppActivities` in a (Xposed) Host environment.

**Solution**

Under normal circumstances, this error basically does not occur, but according to the difference of the system version, no detailed testing has been done.

After eliminating the problem of your own code, please bring detailed logs for feedback.

###### exception

::: danger loggerE

This proxy \[**TYPE**\] type is not allowed

:::

**Abnormal**

Invalid parameters were filled in when injecting Module App's `Activity` using `registerModuleAppActivities` in a (Xposed) Host environment.

> The following example

```kotlin
// ❗ The content filled in here is just an example
// And the proxy is filled with invalid parameters that cannot be understood
registerModuleAppActivities(proxy = false)
```

**Solution**

The `proxy` parameter in the method only accepts `String`, `CharSequence`, `Class` types, please refer to the related usage method to fill in the method parameters correctly.

###### exception

::: danger loggerE

Cound not got launch intent for package "**NAME**"

:::

**Abnormal**

When injecting Module App's `Activity` using `registerModuleAppActivities` in a (Xposed) Host environment, the Host App's launching `Activity` cannot be found.

> The following example

```kotlin
// Register directly with default parameters
registerModuleAppActivities()
```

**Solution**

The default parameter (no parameter) can only be used for the app that can be launched.

If the app does not declare the startup entry `Activity`, you need to manually specify the `proxy` parameter of the method.

###### exception

::: danger loggerE

Could not found "**NAME**" or Class is not a type of Activity

:::

**Abnormal**

When injecting Module App's `Activity` with `registerModuleAppActivities` in a (Xposed) Host environment, the `Activity` filled with the parameter `proxy` cannot be found.

> The following example

```kotlin
registerModuleAppActivities(proxy = "com.demo.test.TestActivity")
```

**Solution**

Please make sure that the `Activity` name you fill in really and effectively exists in the Host App, and the target `Class` extends `Activity`.

## Blocking Exceptions

> These exceptions will directly cause the app to stop running (FC), at the same time print `E` level logs on the console, and also cause the Hook process to "die".

###### exception

::: danger RuntimeException

!!!DO NOT ALLOWED!!! You cannot hook or reflection to call the internal class of the YukiHookAPI itself, The called class is \[**CLASS**\]

:::

**Abnormal**

You have invoked the `Class` object of the API itself using `YukiHookAPI` related reflection or Hook function.

> The following example

```kotlin
// <Scenario 1>
YukiHookAPI.current()
// <Scenario 2>
PackageParam::class.java.hook {
    // ...
}
// <Scenario 3>
MethodFinder::class.java.method {
    name = "name"
    param(StringType)
}.get().call("name")
// ...
```

**Solution**

Please check the code section for errors, such as the case below.

> The following example

```kotlin
YourClass.method {
    // ...
    // ❗ The method execution is not called
    // The actual method is called here is the MethodFinder.Result object
}.get(instance).current()
YourClass.method {
    // ...
    // ✅ The correct way to use it, assuming this method has no parameters
}.get(instance).call().current()
```

Inlining, reflection, Hook `YukiHookAPI`'s own `Class` and internal functions are not allowed to prevent errors.

###### exception

::: danger UnsupportedOperationException

!!!DANGEROUS!!! Hook \[**CLASS**\] Class is a dangerous behavior! \[**CONTENT**\] \[**SOLVE**\]

:::

**Abnormal**

You tried to hook a `Class` object in the list of dangerous behaviors, such as `Class`, `ClassLoader`, `Method`.

> The following example

```kotlin
// <Scenario 1>
JavaClassLoader.hook {
    // ...
}
// <Scenario 2>
JavaClass.hook {
    // ...
}
// <Scenario 3>
JavaMethod.hook {
    // ...
}
// ...
```

**Solution**

These functions are internal to the system, <u>**they should not be hooked, may not be supported on some Hook Frameworks, and may cause other errors**</u>, please try to replace the hook point.

::: tip

If you still want to use this feature, please refer to [YukiMemberHookCreator.useDangerousOperation](../api/public/com/highcapable/yukihookapi/hook/core/YukiMemberHookCreator#usedangerousoperation-method).

But **It is strongly recommended not to do this, please do not report any problems, <u>all the consequences will be borne by yourself</u>**.

:::

###### exception

::: danger NoClassDefFoundError

Can't find this Class in \[**CLASSLOADER**\]: **CONTENT** Generated by YukiHookAPI#ReflectionTool

:::

**Abnormal**

The `Class` object you were looking for was not found via `String.toClass(...)` or `classOf<...>()`.

> The following example

```kotlin
"com.demo.Test".toClass()
```

**Solution**

Please check if the `Class` matched by the current string or entity exists in the current `ClassLoader` and try again.

###### exception

::: danger IllegalStateException

ClassLoader \[**CLASSLOADER**\] is not a DexClassLoader

:::

**Abnormal**

Use `ClassLoader.searchClass` or `PackageParam.searchClass` to find `Class` but currently `ClassLoader` does not extends `BaseDexClassLoader`.

**Solution**

This situation basically does not exist, unless the current app references a Non-ART platform executable (which not realistic) or the current `ClassLoader` is null.

###### exception

::: danger IllegalStateException

Failed to got SystemContext

:::

**Abnormal**

`systemContext` was called in the Host App but the instance object was not successfully obtained.

> The following example

```kotlin
encase {
    // This variable is called
    systemContext...
}
```

**Solution**

This situation should not exist, since `systemContext` is obtained from `ActivityThread` through reflection, unless the system process fails, the obtained object will not be null.

###### exception

::: danger IllegalStateException

App is dead, You cannot call to appContext

:::

**Abnormal**

> The first case

The `appContext` of the `ModuleApplication` is called within the Hook App.

> The following example

```kotlin
encase {
    // This variable is called
    ModuleApplication.appContext...
}
```

> The second case

`appContext` was called when using `ModuleApplication` but the app may have been destroyed or not started correctly.

> The following example

```kotlin
// This variable is called but the app may have been destroyed or not started correctly
ModuleApplication.appContext...
```

**Solution**

> The first case

You can only use the `appContext` of `ModuleApplication` in the Module App, please use the `appContext` in the `PackageParam` in the Host App, please make sure you use it correctly.

> The second case

This situation basically does not exist, because `appContext` is assigned in `onCreate`, unless the `onCreate` method of the parent class is called by reflection before multi-process concurrent startup or app is not started and completed.

###### exception

::: danger IllegalStateException

YukiHookModulePrefs not allowed in Custom Hook API

:::

**Abnormal**

`YukiHookModulePrefs` is used in Hook's own app (not Xposed Module).

> The following example

```kotlin
class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        YukiHookAPI.encase(base) {
            // ❗ Can't use prefs in this case
            prefs.getBoolean("test_data")
        }
        super.attachBaseContext(base)
    }
}
```

**Solution**

You can only use `YukiHookModulePrefs` when [Use as Xposed Module Configs](../config/xposed-using), please use the native `Sp` storage in the Hook's own app.

###### exception

::: danger IllegalStateException

Cannot load the XSharedPreferences, maybe is your Hook Framework not support it

:::

**Abnormal**

Using `YukiHookModulePrefs` in (Xposed) Host environment but unable to get `XSharedPreferences` object.

> The following example

```kotlin
encase {
    // This variable is called
    prefs...
}
```

**Solution**

Under normal circumstances, this problem does not occur.

If you continue to fail to obtain the `XSharedPreferences` object, it may be that the Hook Framework you are using does not support this function or has an error.

###### exception

::: danger IllegalStateException

YukiHookDataChannel not allowed in Custom Hook API

:::

**Abnormal**

`YukiHookDataChannel` is used in Hook's own app (not Xposed Module).

> The following example

```kotlin
class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        YukiHookAPI.encase(base) {
            // ❗ dataChannel cannot be used in this case
            dataChannel.wait(key = "test_data") {
                // ...
            }
        }
        super.attachBaseContext(base)
    }
}
```

**Solution**

You can only use `YukiHookDataChannel` when [Use as Xposed Module Configs](../config/xposed-using).

###### exception

::: danger IllegalStateException

YukiHookDataChannel only support used on an Activity, but this current context is "**CLASSNAME**"

:::

**Abnormal**

`YukiHookDataChannel` is used in a non-`Activity` context of a Module App.

**Solution**

You can only use `YukiHookDataChannel` in `Activity` or `Fragment`.

###### exception

::: danger IllegalStateException

Xposed modulePackageName load failed, please reset and rebuild it

:::

**Abnormal**

When using `YukiHookModulePrefs` or `YukiHookDataChannel` in the Hook process, the `modulePackageName` at load time cannot be read, resulting in the package name of the own Module App cannot be determined.

**Solution**

Please read the help document [here](../config/xposed-using#modulepackagename-parameter) carefully, and configure the Module App's Hook entry class package name correctly.

###### exception

::: danger IllegalStateException

YukiHookModulePrefs missing Context instance

:::

**Abnormal**

`YukiHookModulePrefs` is used in the Module App to store data but no `Context` instance is passed in.

> The following example

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ❗ Wrong usage
        // Constructor has been set to private in API 1.0.88 and later
        YukiHookModulePrefs().getBoolean("test_data")
    }
}
```

**Solution**

It is recommended to use the `modulePrefs` method to load `YukiHookModulePrefs` in `Activity`.

> The following example

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ✅ Correct usage
        modulePrefs.getBoolean("test_data")
    }
}
```

###### exception

::: danger IllegalStateException

Key-Value type **TYPE** is not allowed

:::

**Abnormal**

An unsupported storage type was passed in when using the `get` or `put` methods of `YukiHookModulePrefs` or the `wait` or `put` methods of `YukiHookDataChannel`.

**Solution**

The supported types of `YukiHookModulePrefs` are only `String`, `Set<String>`, `Int`, `Float`, `Long`, `Boolean`, please pass in the supported types.

The supported types of `YukiHookDataChannel` are the types restricted by `Intent.putExtra`, please pass in the supported types.

###### exception

::: danger IllegalStateException

YukiHookDataChannel cannot be used in zygote

:::

**Abnormal**

`YukiHookDataChannel` is used in `loadZygote`.

> The following example

```kotlin
loadZygote {
    // This variable is called
    dataChannel...
}
```

**Solution**

`YukiHookDataChannel` can only be used in `loadSystem`, `loadApp`.

###### exception

::: danger IllegalStateException

Custom Hooking Members is empty

:::

**Abnormal**

`members()` is called in `MemberHookCreator` but the `Member` instance that requires the Hook is not set.

> The following example

```kotlin
injectMember {
    // Method parameters in parentheses are left blank
    members()
    afterHook {
        // ...
    }
}
```

**Solution**

To use `members()` to set a custom Hook method, you must ensure that the `Member` array object in its method parameter cannot be empty.

###### exception

::: danger IllegalStateException

HookParam Method args index must be >= 0

:::

**Abnormal**

`args().last()` is called in `HookParam` but the target `param` is empty or the `index` in `args` is set to a value less than 0.

> The following example

```kotlin
injectMember {
    // ...
    afterHook {
        // Assume param is empty
        args().last()...
        // Set an index less than 0
        args(index = -5)...
    }
}
```

**Solution**

Please make sure that the number of method parameters of the target method and constructor of your Hook is not empty, and the subscript of `args` cannot be set to a value less than 0.

###### exception

::: danger IllegalStateException

HookParam instance got null! Is this a static member?

:::

**Abnormal**

An object that calls an `instance` variable or `instance` method in a `HookParam` but cannot get the current instance.

> The following example

```kotlin
injectMember {
    // ...
    afterHook {
        // This variable is called
        instance...
        // This method is called
        instance<Any>()...
    }
}
```

**Solution**

Please confirm whether the method of your Hook is a static type.

The static type method has no instance and cannot use this function.

If it is not a static method, please check whether the instance has been destroyed.

###### exception

::: danger IllegalStateException

Current hooked Member args is null

:::

**Abnormal**

The `args` variable is called in `HookParam`, but the parameter array of the current instance method and constructor cannot be obtained.

> The following example

```kotlin
injectMember {
    // ...
    afterHook {
        // This variable is called
        args...
    }
}
```

**Solution**

This kind of problem generally does not occur.

If this problem does occur, please bring detailed logs for feedback.

###### exception

::: danger IllegalStateException

Current hooked Member is null

:::

**Abnormal**

Call the `member` variable in `HookParam` but cannot get the method and constructor instance of the current instance.

> The following example

```kotlin
injectMember {
    // ...
    afterHook {
        // This variable is called
        member...
    }
}
```

**Solution**

This kind of problem generally does not occur.

If this problem does occur, please bring detailed logs for feedback.

###### exception

::: danger IllegalStateException

Current hooked Member is not a Method

:::

**Abnormal**

Calling the `method` variable in `HookParam` but not getting the method instance of the current instance.

> The following example

```kotlin
injectMember {
    // ...
    afterHook {
        // This variable is called
        method...
    }
}
```

**Solution**

Please confirm whether the method of your Hook is a constructor or a common method and use the method of the corresponding type to obtain the specified instance.

If you do not know the type of the bytecode, you can directly use `member` to obtain it.

###### exception

::: danger IllegalStateException

Current hooked Member is not a Constructor

:::

**Abnormal**

A method instance for calling a `constructor` variable in a `HookParam` but not getting the current instance.

> The following example

```kotlin
injectMember {
    // ...
    afterHook {
        // This variable is called
        constructor...
    }
}
```

**Solution**

Please confirm whether the method of your Hook is a common method or a constructor method and use the method of the corresponding type to obtain the specified instance.

If you do not know the type of the bytecode, you can directly use `member` to obtain it.

###### exception

::: danger IllegalStateException

HookParam instance cannot cast to **TYPE**

:::

**Abnormal**

Invoking the `instance` method in a `HookParam` specifies the wrong type.

> The following example

```kotlin
injectMember {
    // ...
    afterHook {
        // The type is cast to Activity
        // But assumes the current instance's type is not this type
        instance<Activity>()...
    }
}
```

**Solution**

Please confirm the correct type of the current Hook instance and refill the type in the generic.

If you are not sure, please use `Any` or directly use the `instance` variable.

###### exception

::: danger IllegalStateException

HookParam Method args is empty, mabe not has args

:::

**Abnormal**

The `ArgsModifyer.set` method is called in `HookParam` but the method parameter array for the current instance is empty.

> The following example

```kotlin
injectMember {
    // ...
    afterHook {
        // This method is called
        args(...).set(...)
    }
}
```

**Solution**

Please make sure that the number of method parameters of the target method and constructor of your Hook is not empty, otherwise you cannot use this function.

###### exception

::: danger IllegalStateException

HookParam Method args index out of bounds, max is **NUMBER**

:::

**Abnormal**

Calling the `ArgsModifyer.set` method in `HookParam` specifies an array number beyond the subscript of the method parameter.

> The following example

```kotlin
injectMember {
    // ...
    afterHook {
        // The subscript starts from 0
        // Assuming the original parameter subscript is 5, but fill in 6 here
        args(index = 6).set(...)
    }
}
```

**Solution**

Please confirm the target method of your Hook, the number of method parameters of the constructor, and reset the array subscript.

###### exception

::: danger IllegalStateException

PackageParam got null ClassLoader

:::

**Abnormal**

The `appClassLoader` variable was called in `PackageParam` but the instance object could not be obtained.

> The following example

```kotlin
encase {
    // This variable is called
    appClassLoader...
}
```

**Solution**

This situation hardly exists, unless there is a problem with the Host App where the Module App is loaded or the target Hook Framework itself.

If this problem does occur, please provide feedback with detailed logs.

###### exception

::: danger IllegalStateException

Current Hook Framework not support moduleAppResources

:::

**Abnormal**

The `moduleAppResources` variable was called in `PackageParam` but the instance object could not be obtained.

> The following example

```kotlin
encase {
    // This variable is called
    moduleAppResources...
}
```

**Solution**

`moduleAppResources` requires the current Hook Framework to support the `initZygote` function, please check and try again.

###### exception

::: danger IllegalStateException

VariousClass match failed of those **CLASSES**

:::

**Abnormal**

All `Class` were not found when creating indeterminate `Class` objects using `VariousClass`.

**Solution**

After checking whether there is a matching `Class` in the Host App of the current Hook, try again.

###### exception

::: danger IllegalStateException

Cannot get hook class "**NAME**" cause **THROWABLE**

:::

**Abnormal**

The `instanceClass` variable is called in the `hook` method body other than the `onPrepareHook` method and the `Class` of the current Hook does not exist.

> The following example

```kotlin
TargetClass.hook {
    // The possible case is that the instanceClass variable
    // Who is called in the body of the non-onPrepareHook method to print the log
    loggerD(msg = "$instanceClass hook start")
}
```

**Solution**

Using `instanceClass` directly in `hook` is very dangerous, if the Class does not exist, it will directly cause the Hook process to "die".

For details, please refer to [Status Monitor](../guide/example#status-monitor).

###### exception

::: danger IllegalStateException

LayoutInflatedParam View instance got null

:::

**Abnormal**

`currentView` was called in the layout hook callback but no instance object was obtained.

> The following example

```kotlin
injectResource {
    conditions {
        name = "activity_main"
        layout()
    }
    injectAsLayout {
        // This variable is called
        currentView...
    }
}
```

**Solution**

This situation basically does not exist, unless the current `Activity` of the Host App has been destroyed or there is a problem with the Hook Framework itself.

###### exception

::: danger IllegalStateException

XResForwarder is invalid

:::

**Abnormal**

`resources` was called in `YukiResForwarder` but no instance object was obtained.

> The following example

```kotlin
// This variable is called
moduleAppResources.fwd(...).resources
```

**Solution**

This basically doesn't exist unless there is a problem with the Hook Framework itself.

###### exception

::: danger IllegalStateException

paramTypes is empty, please use emptyParam() instead

:::

**Abnormal**

The empty `param` method is preserved when looking up methods, constructors.

> The following example

```kotlin
method {
    name = "test"
    // No parameters are filled in parentheses
    param()
}
```

**Solution**

To identify this method, the constructor has no parameters, you can have a setter method as follows.

The first way, set `emptyParam` (recommended)

> The following example

```kotlin
method {
    name = "test"
    emptyParam()
}
```

The second way, set `paramCount = 0`

> The following example

```kotlin
method {
    name = "test"
    paramCount = 0
}
```

###### exception

::: danger IllegalStateException

Invalid YukiHookCallback type

:::

**Abnormal**

The core Hook functionality of `YukiHookAPI` is broken.

**Solution**

This situation basically does not exist.

If the above problem occurs, after confirming that the problem is not in your own code, you can submit a log for feedback.

###### exception

::: danger IllegalStateException

ModuleContextThemeWrapper already loaded

:::

**Abnormal**

Called repeatedly when using the `applyModuleTheme` method in the `Context`.

> The following example

```kotlin
// Assume this is the current Context object
context.applyModuleTheme(R.style.Theme_AppCompat).applyModuleTheme(R.style.Theme_AppCompat)
```

**Solution**

The `ModuleContextThemeWrapper` can only be created once in the `Context`, please check the code for loop call problems.

###### exception

::: danger IllegalStateException

Cannot create classes cache for "android", please remove "name" param

:::

**Abnormal**

The `DexClassFinder` cache function `searchClass(name = ...)` is used in the System Framework ("android") Host App.

> The following example

```kotlin
loadSystem {
    searchClass(name = "test") {
        from(...)
        // ...
    }.get()
}
```

**Solution**

Since the cache will store the found `Class` name in `SharedPreferences`, but the data directory does not exist in the System Framework, so please do not use this function in the System Framework.