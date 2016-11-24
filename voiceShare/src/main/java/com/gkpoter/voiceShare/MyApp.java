package com.gkpoter.voiceShare;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.gkpoter.voiceShare.util.PicassoImageLoader;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Created by dy on 2016/11/17.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //设置主题
        //ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()//#19b4ff
                .setTitleBarBgColor(Color.rgb(0x19, 0xb4, 0xff))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(Color.RED)
                .setFabPressedColor(Color.BLUE)
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.BLACK)
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
//            .setMutiSelect(false)//配置是否多选
//            .setMutiSelectMaxSize(int maxSize)//配置多选数量
            .setEnableEdit(true)//开启编辑功能
            .setEnableCrop(true)//开启裁剪功能
            .setEnableRotate(true)//开启旋转功能
            .setEnableCamera(true)//开启相机功能
            .setCropWidth(400)//裁剪宽度
            .setCropHeight(400)//裁剪高度
            .setCropSquare(true)//裁剪正方形
//            .setSelected(List)//添加已选列表,只是在列表中默认呗选中不会过滤图片
//            .setFilter(List list)//添加图片过滤，也就是不在GalleryFinal中显示
//            .takePhotoFolter(File file)//配置拍照保存目录，不做配置的话默认是/sdcard/DCIM/GalleryFinal/
//            .setRotateReplaceSource(boolean)//配置选择图片时是否替换原始图片，默认不替换
//            .setCropReplaceSource(boolean)//配置裁剪图片时是否替换原始图片，默认不替换
//            .setForceCrop(boolean)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
//            .setForceCropEdit(boolean)//在开启强制裁剪功能时是否可以对图片进行编辑（也就是是否显示旋转图标和拍照图标）
            .setEnablePreview(true)//是否开启预览功能
            .build();

        //配置imageloader
        ImageLoader imageloader = new PicassoImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(getApplicationContext(), imageloader, theme)
                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }
}
