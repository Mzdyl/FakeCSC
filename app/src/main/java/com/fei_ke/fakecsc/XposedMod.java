package com.fei_ke.fakecsc;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.os.Build;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedMod implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Class<?> SystemProperties = XposedHelpers.findClass("android.os.SystemProperties", lpparam.classLoader);
        XposedBridge.hookAllMethods(SystemProperties, "get", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String key = (String) param.args[0];
                if (key.equals("ro.csc.sales_code")) {
                    param.setResult("CHINA");
                } else if (key.equals("ro.csc.country_code")) {
                    param.setResult("CN");
                } else if (key.equals("ro.csc.countryiso_code")) {
                    param.setResult("CHC");
                }
            }
        });

        findAndHookMethod("android.telephony.TelephonyManager", lpparam.classLoader, "getSimOperator",
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return "46000";
                    }
                });
        findAndHookMethod("android.telephony.TelephonyManager", lpparam.classLoader, "getSimCountryIso",
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return "cn";
                    }
                });
        findAndHookMethod("android.telephony.TelephonyManager", lpparam.classLoader, "getSimOperatorName",
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                        return "中国移动";
                    }
                });
        XposedHelpers.setStaticObjectField(Build.class, "MODEL", "SM-S9380");
    }
}
