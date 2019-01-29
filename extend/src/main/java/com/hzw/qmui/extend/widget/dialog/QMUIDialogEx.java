package com.hzw.qmui.extend.widget.dialog;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogBuilder;

/**
 * @author ziwei huang
 */
public class QMUIDialogEx extends QMUIDialog {
    public QMUIDialogEx(Context context) {
        super(context);
    }

    public QMUIDialogEx(Context context, int styleRes) {
        super(context, styleRes);
    }

    /**
     * 随键盘升降自动调整 Dialog 高度的 Builder
     * (修复大小变化后，输入框焦点发生变化的问题, 并支持设定内容区域的最小高度)
     */
    public static abstract class AutoResizeDialogBuilder extends QMUIDialogBuilder {

        private ScrollView mScrollerView;

        private int mAnchorHeight = 0;
        private int mScreenHeight = 0;
        private int mScrollHeight = 0;

        private int mMinHeight = 0;

        public AutoResizeDialogBuilder(Context context) {
            this(context, 0);
        }

        public AutoResizeDialogBuilder(Context context, int minHeight) {
            super(context);
            mMinHeight = minHeight;
        }

        @Override
        protected void onCreateContent(QMUIDialog dialog, ViewGroup parent, Context context) {
            mScrollerView = new ScrollView(context);
            mScrollerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, onGetScrollHeight()));
            mScrollerView.addView(onBuildContent(dialog, mScrollerView));
            parent.addView(mScrollerView);
        }

        @Override
        protected void onAfter(QMUIDialog dialog, LinearLayout parent, Context context) {
            super.onAfter(dialog, parent, context);
            bindEvent(context);
        }

        public abstract View onBuildContent(QMUIDialog dialog, ScrollView parent);

        public int onGetScrollHeight() {
            return ScrollView.LayoutParams.WRAP_CONTENT;
        }

        private void bindEvent(final Context context) {
            mAnchorTopView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mAnchorBottomView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    //noinspection ConstantConditions
                    View mDecor = mDialog.getWindow().getDecorView();
                    Rect r = new Rect();
                    mDecor.getWindowVisibleDisplayFrame(r);
                    mScreenHeight = QMUIDisplayHelper.getScreenHeight(context);
                    int anchorShouldHeight = mScreenHeight - r.bottom;
                    if (anchorShouldHeight != mAnchorHeight) {
                        mAnchorHeight = anchorShouldHeight;
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mAnchorBottomView.getLayoutParams();
                        lp.height = mAnchorHeight;
                        mAnchorBottomView.setLayoutParams(lp);
                        LinearLayout.LayoutParams slp = (LinearLayout.LayoutParams) mScrollerView.getLayoutParams();
                        if (onGetScrollHeight() == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            mScrollHeight = Math.max(mScrollHeight, mScrollerView.getMeasuredHeight());
                        } else {
                            mScrollHeight = onGetScrollHeight();
                        }
                        if (mAnchorHeight == 0) {
                            slp.height = mScrollHeight;
                        } else {
//                            mScrollerView.getChildAt(0).requestFocus();
                            slp.height = mScrollHeight - mAnchorHeight;
                        }
                        slp.height = Math.max(mMinHeight, slp.height);
                        mScrollerView.setLayoutParams(slp);
                    } else {
                        //如果内容过高,anchorShouldHeight=0,但实际下半部分会被截断,因此需要保护
                        //由于高度超过后,actionContainer并不会去测量和布局,所以这里拿不到action的高度,因此用比例估算一个值
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mDialogView.getLayoutParams();
                        int dialogLayoutMaxHeight = mScreenHeight - lp.bottomMargin - lp.topMargin - r.top;
                        int scrollLayoutHeight = mScrollerView.getMeasuredHeight();
                        if (scrollLayoutHeight > dialogLayoutMaxHeight * 0.8) {
                            mScrollHeight = (int) (dialogLayoutMaxHeight * 0.8);
                            LinearLayout.LayoutParams slp = (LinearLayout.LayoutParams) mScrollerView.getLayoutParams();
                            slp.height = mScrollHeight;
                            slp.height = Math.max(mMinHeight, slp.height);
                            mScrollerView.setLayoutParams(slp);
                        }
                    }
                }
            });
        }
    }
}
