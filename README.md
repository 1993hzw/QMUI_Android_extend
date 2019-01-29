
QMUI_Android
---------------------------------
QMUI Android 的设计目的是用于辅助快速搭建一个具备基本设计还原效果的 Android 项目，同时利用自身提供的丰富控件及兼容处理，让开发者能专注于业务需求而无需耗费精力在基础代码的设计上。不管是新项目的创建，或是已有项目的维护，均可使开发效率和项目质量得到大幅度提升。

[QMUI官网](https://qmuiteam.com/android/) [github项目](https://github.com/Tencent/QMUI_Android)

本项目是针对QMUI的补充和拓展，提供其丰富性、稳定性
=======

[![](https://jitpack.io/v/1993hzw/QMUI_Android_extend.svg)](https://jitpack.io/#1993hzw/QMUI_Android_extend)

#### Gradle

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    compile 'com.github.1993hzw:QMUI_Android_extend:1.0'
}
```

#### Extend

* **QMUIAlphaImageView**

在 pressed 和 disabled 时改变 ImageView 的透明度

* **QMUIDialogEx**
1. `AutoResizeDialogBuilder`

随键盘升降自动调整 Dialog 高度的 Builder(修复大小变化后，输入框焦点发生变化的问题, 并支持设定内容区域的最小高度)

* **styles.xml**
1. `QMUI.Compat.Ex` / `QMUI.Compat.NoActionBar.Ex`

提高不同版本直接的兼容性，保持UI样式一致
