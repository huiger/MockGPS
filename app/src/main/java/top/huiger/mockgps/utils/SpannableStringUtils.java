package top.huiger.mockgps.utils;


import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout.Alignment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import top.huiger.mockgps.App;

import static android.graphics.BlurMaskFilter.Blur;

/**
 * getBuilder : 获取建造者
 * setFlag : 设置标识
 * setForegroundColor: 设置前景色
 * setBackgroundColor: 设置背景色
 * setQuoteColor : 设置引用线的颜色
 * setLeadingMargin : 设置缩进
 * setBullet : 设置列表标记
 * setProportion : 设置字体比例
 * setXProportion : 设置字体横向比例
 * setStrikethrough : 设置删除线
 * setUnderline : 设置下划线
 * setSuperscript : 设置上标
 * setSubscript : 设置下标
 * setBold : 设置粗体
 * setItalic : 设置斜体
 * setBoldItalic : 设置粗斜体
 * setFontFamily : 设置字体
 * setAlign : 设置对齐
 * setBitmap : 设置图片
 * setDrawable : 设置图片
 * setUri : 设置图片
 * setResourceId : 设置图片
 * setClickSpan : 设置点击事件
 * setUrl : 设置超链接
 * setBlur : 设置模糊
 * append : 追加样式字符串
 * create : 创建样式字符串
 */
public class SpannableStringUtils {

    private SpannableStringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取建造者 * * @return {@link Builder}
     */
    public static Builder getBuilder(@NonNull CharSequence text) {
        return new Builder(text);
    }

    public static class Builder {
        private int defaultValue = 0x12000000;
        private CharSequence text;
        private int flag;
        @ColorInt
        private int foregroundColor;
        @ColorInt
        private int backgroundColor;
        @ColorInt
        private int quoteColor;
        private boolean isLeadingMargin;
        private int first;
        private int rest;
        private boolean isBullet;
        private int gapWidth;
        private int bulletColor;
        private float proportion;
        private float xProportion;
        private boolean isStrikethrough;
        private boolean isUnderline;
        private boolean isSuperscript;
        private boolean isSubscript;
        private boolean isBold;
        private boolean isItalic;
        private boolean isBoldItalic;
        private String fontFamily;
        private Alignment align;
        private boolean imageIsBitmap;
        private Bitmap bitmap;
        private boolean imageIsDrawable;
        private Drawable drawable;
        private boolean imageIsUri;
        private Uri uri;
        private boolean imageIsResourceId;
        @DrawableRes
        private int resourceId;
        private ClickableSpan clickSpan;
        private String url;
        private boolean isBlur;
        private float radius;
        private Blur style;
        private SpannableStringBuilder mBuilder;

        private Builder(@NonNull CharSequence text) {
            this.text = text;
            flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
            foregroundColor = defaultValue;
            backgroundColor = defaultValue;
            quoteColor = defaultValue;
            proportion = -1;
            xProportion = -1;
            mBuilder = new SpannableStringBuilder();
        }

        /**
         * 设置标识 * * @param flag <ul> * <li>{@link Spanned#SPAN_INCLUSIVE_EXCLUSIVE}</li> * <li>{@link Spanned#SPAN_INCLUSIVE_INCLUSIVE}</li> * <li>{@link Spanned#SPAN_EXCLUSIVE_EXCLUSIVE}</li> * <li>{@link Spanned#SPAN_EXCLUSIVE_INCLUSIVE}</li> * </ul> * @return {@link Builder}
         */
        public Builder setFlag(int flag) {
            this.flag = flag;
            return this;
        }

        /**
         * 设置前景色 * * @param color 前景色 * @return {@link Builder}
         */
        public Builder setForegroundColor(@ColorInt int color) {
            this.foregroundColor = color;
            return this;
        }

        /**
         * 设置背景色 * * @param color 背景色 * @return {@link Builder}
         */
        public Builder setBackgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        /**
         * 设置引用线的颜色 * * @param color 引用线的颜色 * @return {@link Builder}
         */
        public Builder setQuoteColor(@ColorInt int color) {
            this.quoteColor = color;
            return this;
        }

        /**
         * 设置缩进 * * @param first 首行缩进 * @param rest 剩余行缩进 * @return {@link Builder}
         */
        public Builder setLeadingMargin(int first, int rest) {
            this.first = first;
            this.rest = rest;
            isLeadingMargin = true;
            return this;
        }

        /**
         * 设置列表标记 * * @param gapWidth 列表标记和文字间距离 * @param color 列表标记的颜色 * @return {@link Builder}
         */
        public Builder setBullet(int gapWidth, int color) {
            this.gapWidth = gapWidth;
            bulletColor = color;
            isBullet = true;
            return this;
        }

        /**
         * 设置字体比例 * * @param proportion 比例 * @return {@link Builder}
         */
        public Builder setProportion(float proportion) {
            this.proportion = proportion;
            return this;
        }

        /**
         * 设置字体横向比例 * * @param proportion 比例 * @return {@link Builder}
         */
        public Builder setXProportion(float proportion) {
            this.xProportion = proportion;
            return this;
        }

        /**
         * 设置删除线 * * @return {@link Builder}
         */
        public Builder setStrikethrough() {
            this.isStrikethrough = true;
            return this;
        }

        /**
         * 设置下划线 * * @return {@link Builder}
         */
        public Builder setUnderline() {
            this.isUnderline = true;
            return this;
        }

        /**
         * 设置上标 * * @return {@link Builder}
         */
        public Builder setSuperscript() {
            this.isSuperscript = true;
            return this;
        }

        /**
         * 设置下标 * * @return {@link Builder}
         */
        public Builder setSubscript() {
            this.isSubscript = true;
            return this;
        }

        /**
         * 设置粗体 * * @return {@link Builder}
         */
        public Builder setBold() {
            isBold = true;
            return this;
        }

        /**
         * 设置斜体 * * @return {@link Builder}
         */
        public Builder setItalic() {
            isItalic = true;
            return this;
        }

        /**
         * 设置粗斜体 * * @return {@link Builder}
         */
        public Builder setBoldItalic() {
            isBoldItalic = true;
            return this;
        }

        /**
         * 设置字体 * * @param fontFamily 字体 * <ul> * <li>monospace</li> * <li>serif</li> * <li>sans-serif</li> * </ul> * @return {@link Builder}
         */
        public Builder setFontFamily(@Nullable String fontFamily) {
            this.fontFamily = fontFamily;
            return this;
        }

        /**
         * 设置对齐 * <ul> * <li>{@link Alignment#ALIGN_NORMAL}正常</li> * <li>{@link Alignment#ALIGN_OPPOSITE}相反</li> * <li>{@link Alignment#ALIGN_CENTER}居中</li> * </ul> * * @return {@link Builder}
         */
        public Builder setAlign(@Nullable Alignment align) {
            this.align = align;
            return this;
        }

        /**
         * 设置图片 * * @param bitmap 图片位图 * @return {@link Builder}
         */
        public Builder setBitmap(@NonNull Bitmap bitmap) {
            this.bitmap = bitmap;
            imageIsBitmap = true;
            return this;
        }

        /**
         * 设置图片 * * @param drawable 图片资源 * @return {@link Builder}
         */
        public Builder setDrawable(@NonNull Drawable drawable) {
            this.drawable = drawable;
            imageIsDrawable = true;
            return this;
        }

        /**
         * 设置图片 * * @param uri 图片uri * @return {@link Builder}
         */
        public Builder setUri(@NonNull Uri uri) {
            this.uri = uri;
            imageIsUri = true;
            return this;
        }

        /**
         * 设置图片 * * @param resourceId 图片资源id * @return {@link Builder}
         */
        public Builder setResourceId(@DrawableRes int resourceId) {
            this.resourceId = resourceId;
            imageIsResourceId = true;
            return this;
        }

        /**
         * 设置点击事件 * <p>需添加view.setMovementMethod(LinkMovementMethod.getInstance())</p> * @param clickSpan 点击事件 * @return {@link Builder}
         */
        public Builder setClickSpan(@NonNull ClickableSpan clickSpan) {
            this.clickSpan = clickSpan;
            return this;
        }

        /**
         * 设置超链接 * <p>需添加view.setMovementMethod(LinkMovementMethod.getInstance())</p> * * @param url 超链接 * @return {@link Builder}
         */
        public Builder setUrl(@NonNull String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置模糊 * <p>尚存bug，其他地方存在相同的字体的话，相同字体出现在之前的话那么就不会模糊，出现在之后的话那会一起模糊</p> * <p>推荐还是把所有字体都模糊这样使用</p> * * @param radius 模糊半径（需大于0） * @param style 模糊样式<ul> * <li>{@link Blur#NORMAL}</li> * <li>{@link Blur#SOLID}</li> * <li>{@link Blur#OUTER}</li> * <li>{@link Blur#INNER}</li> * </ul> * @return {@link Builder}
         */
        public Builder setBlur(float radius, Blur style) {
            this.radius = radius;
            this.style = style;
            this.isBlur = true;
            return this;
        }

        /**
         * 追加样式字符串 * * @param text 样式字符串文本 * @return {@link Builder}
         */
        public Builder append(@NonNull CharSequence text) {
            setSpan();
            this.text = text;
            return this;
        }

        /**
         * 创建样式字符串 * * @return 样式字符串
         */
        public SpannableStringBuilder create() {
            setSpan();
            return mBuilder;
        }

        /**
         * 设置样式
         */
        private void setSpan() {
            int start = mBuilder.length();
            mBuilder.append(this.text);
            int end = mBuilder.length();
            if (foregroundColor != defaultValue) {
                mBuilder.setSpan(new ForegroundColorSpan(foregroundColor), start, end, flag);
                foregroundColor = defaultValue;
            }
            if (backgroundColor != defaultValue) {
                mBuilder.setSpan(new BackgroundColorSpan(backgroundColor), start, end, flag);
                backgroundColor = defaultValue;
            }
            if (isLeadingMargin) {
                mBuilder.setSpan(new LeadingMarginSpan.Standard(first, rest), start, end, flag);
                isLeadingMargin = false;
            }
            if (quoteColor != defaultValue) {
                mBuilder.setSpan(new QuoteSpan(quoteColor), start, end, 0);
                quoteColor = defaultValue;
            }
            if (isBullet) {
                mBuilder.setSpan(new BulletSpan(gapWidth, bulletColor), start, end, 0);
                isBullet = false;
            }
            if (proportion != -1) {
                mBuilder.setSpan(new RelativeSizeSpan(proportion), start, end, flag);
                proportion = -1;
            }
            if (xProportion != -1) {
                mBuilder.setSpan(new ScaleXSpan(xProportion), start, end, flag);
                xProportion = -1;
            }
            if (isStrikethrough) {
                mBuilder.setSpan(new StrikethroughSpan(), start, end, flag);
                isStrikethrough = false;
            }
            if (isUnderline) {
                mBuilder.setSpan(new UnderlineSpan(), start, end, flag);
                isUnderline = false;
            }
            if (isSuperscript) {
                mBuilder.setSpan(new SuperscriptSpan(), start, end, flag);
                isSuperscript = false;
            }
            if (isSubscript) {
                mBuilder.setSpan(new SubscriptSpan(), start, end, flag);
                isSubscript = false;
            }
            if (isBold) {
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
                isBold = false;
            }
            if (isItalic) {
                mBuilder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, flag);
                isItalic = false;
            }
            if (isBoldItalic) {
                mBuilder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, flag);
                isBoldItalic = false;
            }
            if (fontFamily != null) {
                mBuilder.setSpan(new TypefaceSpan(fontFamily), start, end, flag);
                fontFamily = null;
            }
            if (align != null) {
                mBuilder.setSpan(new AlignmentSpan.Standard(align), start, end, flag);
                align = null;
            }
            if (imageIsBitmap || imageIsDrawable || imageIsUri || imageIsResourceId) {
                if (imageIsBitmap) {
                    mBuilder.setSpan(new ImageSpan(App.getContext(), bitmap), start, end, flag);
                    bitmap = null;
                    imageIsBitmap = false;
                } else if (imageIsDrawable) {
                    mBuilder.setSpan(new ImageSpan(drawable), start, end, flag);
                    drawable = null;
                    imageIsDrawable = false;
                } else if (imageIsUri) {
                    mBuilder.setSpan(new ImageSpan(App.getContext(), uri), start, end, flag);
                    uri = null;
                    imageIsUri = false;
                } else {
                    mBuilder.setSpan(new ImageSpan(App.getContext(), resourceId), start, end, flag);
                    resourceId = 0;
                    imageIsResourceId = false;
                }
            }
            if (clickSpan != null) {
                mBuilder.setSpan(clickSpan, start, end, flag);
                clickSpan = null;
            }
            if (url != null) {
                mBuilder.setSpan(new URLSpan(url), start, end, flag);
                url = null;
            }
            if (isBlur) {
                mBuilder.setSpan(new MaskFilterSpan(new BlurMaskFilter(radius, style)), start, end, flag);
                isBlur = false;
            }
            flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        }
    }

    /**
     * 设置金额字体大小
     *
     * @param text 金额
     * @return 1.oo
     */
    public static SpannableStringBuilder setMoneySizeSpan(String text, float proportion) {
        String startText = text.substring(0, text.indexOf(".") + 1);
        String endText = text.substring(text.indexOf(".") + 1, text.length());
        return SpannableStringUtils.getBuilder(startText).append(endText).setProportion(proportion).create();
    }

    public static void setTestTextView(TextView tv, TextView tv2) {

        //创建一个SpannableString对象
        SpannableString sStr = new SpannableString("最是那一低头的温柔，像一朵水莲花不胜凉风的娇羞，道一声珍重，道一声珍重，那一声珍重里有蜜甜的忧愁");
        //设置字体(default,default-bold,monospace,serif,sans-serif)
        sStr.setSpan(new TypefaceSpan("default"), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new TypefaceSpan("default-bold"), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new TypefaceSpan("monospace"), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new TypefaceSpan("serif"), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new TypefaceSpan("sans-serif"), 8, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体大小（绝对值,单位：像素）,第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素
        sStr.setSpan(new AbsoluteSizeSpan(20), 10, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new AbsoluteSizeSpan(20, true), 12, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍   ,0.5表示一半
        sStr.setSpan(new RelativeSizeSpan(0.5f), 14, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体前景色
        sStr.setSpan(new ForegroundColorSpan(Color.RED), 16, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体背景色
        sStr.setSpan(new BackgroundColorSpan(Color.CYAN), 18, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体样式: NORMAL正常，BOLD粗体，ITALIC斜体，BOLD_ITALIC粗斜体
        sStr.setSpan(new StyleSpan(Typeface.NORMAL), 20, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new StyleSpan(Typeface.BOLD), 21, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new StyleSpan(Typeface.ITALIC), 22, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 23, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置下划线
        sStr.setSpan(new UnderlineSpan(), 24, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置删除线
        sStr.setSpan(new StrikethroughSpan(), 26, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置上下标
        sStr.setSpan(new SubscriptSpan(), 28, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sStr.setSpan(new SuperscriptSpan(), 30, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体大小（相对值,单位：像素） 参数表示为默认字体宽度的多少倍 ,2.0f表示默认字体宽度的两倍，即X轴方向放大为默认字体的两倍，而高度不变
        sStr.setSpan(new ScaleXSpan(2.0f), 32, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置项目符号
        sStr.setSpan(new BulletSpan(BulletSpan.STANDARD_GAP_WIDTH, Color.GREEN), 0, sStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //第一个参数表示项目符号占用的宽度，第二个参数为项目符号的颜色
        //设置图片
//        Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        sStr.setSpan(new ImageSpan(drawable), 24, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(sStr);
        tv.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString sStr2 = new SpannableString("电话邮件百度一下短信彩信进入地图");
        //超级链接（需要添加setMovementMethod方法附加响应）
        sStr2.setSpan(new URLSpan("tel:8008820"), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //电话
        sStr2.setSpan(new URLSpan("mailto:kejunlu@qq.com"), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //邮件
        sStr2.setSpan(new URLSpan("http://www.baidu.com"), 4, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //网络
        sStr2.setSpan(new URLSpan("sms:10086"), 8, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //短信   使用sms:或者smsto:
        sStr2.setSpan(new URLSpan("mms:10086"), 10, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //彩信   使用mms:或者mmsto:
        sStr2.setSpan(new URLSpan("geo:32.123456,-17.123456"), 12, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //地图

        tv2.setText(sStr2);
        tv2.setMovementMethod(LinkMovementMethod.getInstance());
    }
}

