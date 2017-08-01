# ProgressWebView
ProgressWebView


webview顶部加载进度条，可自定义


使用方法：
自己界面创建一个webview和progress控件,分别初始化

//初始化
ProgressWebView progressWebView;
progressWebView=new ProgressWebView();

// 开启属性动画让进度条平滑消失
progressWebView.startDismissAnimation(mProgressBar.getProgress(),mProgressBar);

// 开启属性动画让进度条平滑递增
progressWebView.startProgressAnimation(newProgress,mProgressBar);

// 防止调用多次动画
progressWebView.setAnimStart(true);

//传递进度
progressWebView.setCurrentProgress(mProgressBar.getProgress());
mProgressBar.setProgress(newProgress);

详细请看注解，很全面，可以自己定制

如有问题请联系我
