package com.ljt.day_27.parallax;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ljt.day_27.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoeLjt on 2019/4/11.
 * Email: lijiateng1219@gmail.com
 * Description:
 */

public class ParallaxFragment extends Fragment implements LayoutInflaterFactory {

    public static final String LAYOUT_ID_KEY = "KEY";

    private CompatViewInflater mCompatViewInflater;

    private List<View> mParallaxViews = new ArrayList<>();

    private int[] mParallaxAttrs = new int[]{
            R.attr.translationXIn, R.attr.translationXOut,
            R.attr.translationYIn, R.attr.translationYOut
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int layoutId = 0;
        Bundle arguments = getArguments();
        if (arguments != null) {
            layoutId = arguments.getInt(LAYOUT_ID_KEY);
        }

        inflater = inflater.cloneInContext(getActivity());

        // 拦截 View 的创建，然后获取 View 去解析自定义属性
        LayoutInflaterCompat.setFactory(inflater, this);

        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        // 1. 拦截 View 的创建
        View view = createView(parent, name, context, attrs);

        // 2. 自己解析属性
        if (view != null)
            analysisAttrs(view, context, attrs);

        return view;
    }

    private void analysisAttrs(View view, Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, mParallaxAttrs);

        // 主动去解析自定义的几个属性，如果能够拿到，就去遍历解析
        if (array != null && array.getIndexCount() != 0) {

            ParallaxTag parallaxTag = new ParallaxTag();
            for (int i = 0; i < array.getIndexCount(); i++) {

                int arrayIndex = array.getIndex(i);

                switch (arrayIndex) {
                    case 0:
                        parallaxTag.setxIn(array.getFloat(arrayIndex, 0f));
                        break;
                    case 1:
                        parallaxTag.setxOut(array.getFloat(arrayIndex, 0f));
                        break;
                    case 2:
                        parallaxTag.setyIn(array.getFloat(arrayIndex, 0f));
                        break;
                    case 3:
                        parallaxTag.setyOut(array.getFloat(arrayIndex, 0f));
                        break;

                }

                // 要紧的问题是，解析到了以后怎么存 -> 给 View 设置 tag
                view.setTag(R.id.parallax_tag, parallaxTag);

                // 将准备操作的 View 放入集合中
                mParallaxViews.add(view);

            }

            array.recycle();
        }
    }

    public View createView(View parent, final String name, Context context,
                           AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mCompatViewInflater == null) {
            mCompatViewInflater = new CompatViewInflater();
        }

        // We only want the View to inherit it's context if we're running pre-v21
        final boolean inheritContext = isPre21
                && shouldInheritContext((ViewParent) parent);

        return mCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true /* Read read app:theme as a fallback at all times for legacy reasons */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (!(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    public List<View> getParallaxViews() {
        return mParallaxViews;
    }


}
