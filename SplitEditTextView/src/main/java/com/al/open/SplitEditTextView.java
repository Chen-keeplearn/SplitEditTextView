package com.al.open;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.InflateException;

import androidx.appcompat.widget.AppCompatEditText;

public class SplitEditTextView extends AppCompatEditText {
    //密码显示模式：隐藏密码,显示圆形
    public static final int CONTENT_SHOW_MODE_PASSWORD = 1;
    //密码显示模式：显示密码
    public static final int CONTENT_SHOW_MODE_TEXT = 2;
    //输入框相连的样式
    public static final int INPUT_BOX_STYLE_CONNECT = 1;
    //单个的输入框样式
    public static final int INPUT_BOX_STYLE_SINGLE = 2;
    //下划线输入框样式
    public static final int INPUT_BOX_STYLE_UNDERLINE = 3;
    //画笔
    private RectF mRectFConnect;
    private RectF mRectFSingleBox;
    private Paint mPaintDivisionLine;
    private Paint mPaintContent;
    private Paint mPaintBorder;
    private Paint mPaintUnderline;
    //边框大小
    private Float mBorderSize;
    //边框颜色
    private int mBorderColor;
    //圆角大小
    private float mCornerSize;
    //分割线大小
    private float mDivisionLineSize;
    //分割线颜色
    private int mDivisionColor;
    //圆形密码的半径大小
    private float mCircleRadius;
    //密码框长度
    private int mContentNumber;
    //密码显示模式
    private int mContentShowMode;
    //单框和下划线输入样式下,每个输入框的间距
    private float mSpaceSize;
    //输入框样式
    private int mInputBoxStyle;
    //字体大小
    private float mTextSize;
    //字体颜色
    private int mTextColor;
    //每个输入框是否是正方形标识
    private boolean mInputBoxSquare;
    private OnInputListener inputListener;
    private Paint mPaintCursor;
    private CursorRunnable cursorRunnable;

    private int mCursorColor;//光标颜色
    private float mCursorWidth;//光标宽度
    private int mCursorHeight;//光标高度
    private int mCursorDuration;//光标闪烁时长

    private int mUnderlineFocusColor;//下划线输入样式下,输入框获取焦点时下划线颜色
    private int mUnderlineNormalColor;//下划线输入样式下,下划线颜色

    public SplitEditTextView(Context context) {
        this(context, null);
    }

    //这里没有写成默认的EditText属性样式android.R.attr.editTextStyle,这样会存在EditText默认的样式
    public SplitEditTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplitEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context c, AttributeSet attrs) {
        TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.SplitEditTextView);
        mBorderSize = array.getDimension(R.styleable.SplitEditTextView_borderSize, dp2px(1f));
        mBorderColor = array.getColor(R.styleable.SplitEditTextView_borderColor, Color.BLACK);
        mCornerSize = array.getDimension(R.styleable.SplitEditTextView_cornerSize, 0f);
        mDivisionLineSize = array.getDimension(R.styleable.SplitEditTextView_divisionLineSize, dp2px(1f));
        mDivisionColor = array.getColor(R.styleable.SplitEditTextView_divisionLineColor, Color.BLACK);
        mCircleRadius = array.getDimension(R.styleable.SplitEditTextView_circleRadius, dp2px(5f));
        mContentNumber = array.getInt(R.styleable.SplitEditTextView_contentNumber, 6);
        mContentShowMode = array.getInteger(R.styleable.SplitEditTextView_contentShowMode, CONTENT_SHOW_MODE_PASSWORD);
        mInputBoxStyle = array.getInteger(R.styleable.SplitEditTextView_inputBoxStyle, INPUT_BOX_STYLE_CONNECT);
        mSpaceSize = array.getDimension(R.styleable.SplitEditTextView_spaceSize, dp2px(10f));
        mTextSize = array.getDimension(R.styleable.SplitEditTextView_android_textSize, sp2px(16f));
        mTextColor = array.getColor(R.styleable.SplitEditTextView_android_textColor, Color.BLACK);
        mInputBoxSquare = array.getBoolean(R.styleable.SplitEditTextView_inputBoxSquare, true);
        mCursorColor = array.getColor(R.styleable.SplitEditTextView_cursorColor, Color.BLACK);
        mCursorDuration = array.getInt(R.styleable.SplitEditTextView_cursorDuration, 500);
        mCursorWidth = array.getDimension(R.styleable.SplitEditTextView_cursorWidth, dp2px(2f));
        mCursorHeight = (int) array.getDimension(R.styleable.SplitEditTextView_cursorHeight, 0);
        mUnderlineNormalColor = array.getInt(R.styleable.SplitEditTextView_underlineNormalColor, Color.BLACK);
        mUnderlineFocusColor = array.getInt(R.styleable.SplitEditTextView_underlineFocusColor, 0);
        array.recycle();
        init();
    }

    private void init() {
        mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setStrokeWidth(mBorderSize);
        mPaintBorder.setColor(mBorderColor);

        mPaintDivisionLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintDivisionLine.setStyle(Paint.Style.STROKE);
        mPaintDivisionLine.setStrokeWidth(mDivisionLineSize);
        mPaintDivisionLine.setColor(mDivisionColor);

        mPaintContent = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintContent.setTextSize(mTextSize);

        mPaintCursor = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCursor.setStrokeWidth(mCursorWidth);
        mPaintCursor.setColor(mCursorColor);

        mPaintUnderline = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintUnderline.setStrokeWidth(mBorderSize);
        mPaintUnderline.setColor(mUnderlineNormalColor);


        //避免onDraw里面重复创建RectF对象,先初始化RectF对象,在绘制时调用set()方法
        //单个输入框样式的RectF
        mRectFSingleBox = new RectF();
        //绘制Connect样式的矩形框
        mRectFConnect = new RectF();
        //设置单行输入
        setSingleLine();

        //若构造方法中没有写成android.R.attr.editTextStyle的属性,应该需要设置该属性,EditText默认是获取焦点的
        setFocusableInTouchMode(true);

        //取消默认的光标
        setCursorVisible(false);
        //设置InputFilter,设置输入的最大字符长度为设置的长度
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(mContentNumber)});
        //setTextSelectHandleLeft(android.R.color.transparent);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        cursorRunnable = new CursorRunnable();
        postDelayed(cursorRunnable, mCursorDuration);
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(cursorRunnable);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mInputBoxSquare) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //计算view高度,使view高度和每个item的宽度相等,确保每个item是一个正方形
            float itemWidth = getContentItemWidthOnMeasure(width);
            switch (mInputBoxStyle) {
                case INPUT_BOX_STYLE_UNDERLINE:
                    setMeasuredDimension(width, (int) (itemWidth + mBorderSize));
                    break;
                case INPUT_BOX_STYLE_SINGLE:
                case INPUT_BOX_STYLE_CONNECT:
                default:
                    setMeasuredDimension(width, (int) (itemWidth + mBorderSize * 2));
                    break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制输入框
        switch (mInputBoxStyle) {
            case INPUT_BOX_STYLE_SINGLE:
                drawSingleStyle(canvas);
                break;
            case INPUT_BOX_STYLE_UNDERLINE:
                drawUnderlineStyle(canvas);
                break;
            case INPUT_BOX_STYLE_CONNECT:
            default:
                drawConnectStyle(canvas);
                break;
        }
        //绘制输入框内容
        drawContent(canvas);
        //绘制光标
        drawCursor(canvas);
    }

    /**
     * 根据输入内容显示模式,绘制内容是圆心还是明文的text
     */
    private void drawContent(Canvas canvas) {
        int cy = getHeight() / 2;
        String password = getText().toString().trim();
        if (mContentShowMode == CONTENT_SHOW_MODE_PASSWORD) {
            mPaintContent.setColor(Color.BLACK);
            for (int i = 0; i < password.length(); i++) {
                float startX = getDrawContentStartX(i);
                canvas.drawCircle(startX, cy, mCircleRadius, mPaintContent);
            }
        } else {
            mPaintContent.setColor(mTextColor);
            //计算baseline
            float baselineText = getTextBaseline(mPaintContent, cy);
            for (int i = 0; i < password.length(); i++) {
                float startX = getDrawContentStartX(i);
                //计算文字宽度
                String text = String.valueOf(password.charAt(i));
                float textWidth = mPaintContent.measureText(text);
                //绘制文字x应该还需要减去文字宽度的一半
                canvas.drawText(text, startX - textWidth / 2, baselineText, mPaintContent);
            }
        }
    }

    /**
     * 绘制光标
     * 光标只有一个,所以不需要根据循环来绘制,只需绘制第N个就行
     * 即:
     * 当输入内容长度为0,光标在第0个位置
     * 当输入内容长度为1,光标应在第1个位置
     * ...
     * 所以光标所在位置为输入内容的长度
     * 这里光标的长度默认就是 height/2
     */
    private void drawCursor(Canvas canvas) {
        if (mCursorHeight > getHeight()) {
            throw new InflateException("cursor height must smaller than view height");
        }
        String content = getText().toString().trim();
        float startX = getDrawContentStartX(content.length());
        //如果设置得有光标高度,那么startY = (高度-光标高度)/2+边框宽度
        if (mCursorHeight == 0) {
            mCursorHeight = getHeight() / 2;
        }
        int sy = (getHeight() - mCursorHeight) / 2;
        float startY = sy + mBorderSize;
        float stopY = getHeight() - sy - mBorderSize;

        //此时的绘制光标竖直线,startX = stopX
        canvas.drawLine(startX, startY, startX, stopY, mPaintCursor);
    }

    /**
     * 、
     * 计算三种输入框样式下绘制圆和文字的x坐标
     *
     * @param index 循环里面的下标 i
     */
    private float getDrawContentStartX(int index) {
        switch (mInputBoxStyle) {
            case INPUT_BOX_STYLE_SINGLE:
                //单个输入框样式下的startX
                //即 itemWidth/2 + i*itemWidth + i*每一个间距宽度 + 前面所有的左右边框
                //   i = 0,左侧1个边框
                //   i = 1,左侧3个边框(一个完整的item的左右边框+ 一个左侧边框)
                //   i = ..., (2*i+1)*mBorderSize
                return getContentItemWidth() / 2 + index * getContentItemWidth() + index * mSpaceSize + (2 * index + 1) * mBorderSize;
            case INPUT_BOX_STYLE_UNDERLINE:
                //下划线输入框样式下的startX
                //即 itemWidth/2 + i*itemWidth + i*每一个间距宽度
                return getContentItemWidth() / 2 + index * mSpaceSize + index * getContentItemWidth();
            case INPUT_BOX_STYLE_CONNECT:
                //矩形输入框样式下的startX
                //即 itemWidth/2 + i*itemWidth + i*分割线宽度 + 左侧的一个边框宽度
            default:
                return getContentItemWidth() / 2 + index * getContentItemWidth() + index * mDivisionLineSize + mBorderSize;
        }
    }


    /**
     * 绘制下划线输入框样式
     * 线条起点startX:每个字符所占宽度itemWidth + 每个字符item之间的间距mSpaceSize
     * 线条终点stopX:stopX与startX之间就是一个itemWidth的宽度
     */
    private void drawUnderlineStyle(Canvas canvas) {
        String content = getText().toString().trim();
        for (int i = 0; i < mContentNumber; i++) {
            //计算绘制下划线的startX
            float startX = i * getContentItemWidth() + i * mSpaceSize;
            //stopX
            float stopX = getContentItemWidth() + startX;
            //对于下划线这种样式,startY = stopY
            float startY = getHeight() - mBorderSize / 2;
            //这里判断是否设置有输入框获取焦点时,下划线的颜色
            if (mUnderlineFocusColor != 0) {
                if (content.length() >= i) {
                    mPaintUnderline.setColor(mUnderlineFocusColor);
                } else {
                    mPaintUnderline.setColor(mUnderlineNormalColor);
                }
            }
            canvas.drawLine(startX, startY, stopX, startY, mPaintUnderline);
        }
    }

    /**
     * 绘制单框输入模式
     * 这里计算left、right时有点饶,
     * 理解、计算时最好根据图形、参照drawConnectStyle()绘制带边框的矩形
     * left:绘制带边框的矩形等图形时,去掉边框的一半即 + mBorderSize / 2,同时加上每个字符item的间距 + i*mSpaceSize
     * 另外,每个字符item的宽度 + i*itemWidth
     * 最后,绘制时都是以整个view的宽度计算,绘制第N个时,都应该加上以前的边框宽度
     * 即第一个：i = 0 ,边框的宽度为0
     * 第二个：i = 1,边框的宽度 2*mBorderSize,左右两个的边框宽度
     * 以此...最后应该 + i*2*mBorderSize
     * 同理
     * right：去掉边框的一半： -mBorderSize/2,还应该加上前面一个item的宽度：+(i+1)*itemWidth
     * 同样,绘制时都是以整个view的宽度计算,绘制后面的,都应该加上前面的所有宽度
     * 即 间距：+i*mSpaceSize
     * 边框：(注意是计算整个view)
     * 第一个：i = 0,2个边框2*mBorderSize
     * 第二个：i = 1,4个边框,即 (1+1)*2*mBorderSize
     * 所以算上边框 +(i+1)*2*mBorderSize
     */
    private void drawSingleStyle(Canvas canvas) {
        for (int i = 0; i < mContentNumber; i++) {
            mRectFSingleBox.setEmpty();
            float left = i * getContentItemWidth() + i * mSpaceSize + i * mBorderSize * 2 + mBorderSize / 2;
            float right = i * mSpaceSize + (i + 1) * getContentItemWidth() + (i + 1) * 2 * mBorderSize - mBorderSize / 2;
            //为避免在onDraw里面创建RectF对象,这里使用rectF.set()方法
            mRectFSingleBox.set(left, mBorderSize / 2, right, getHeight() - mBorderSize / 2);
            canvas.drawRoundRect(mRectFSingleBox, mCornerSize, mCornerSize, mPaintBorder);
        }
    }

    /**
     * 绘制矩形外框
     * 在绘制圆角矩形的时候,应该减掉边框的宽度
     * 不然会有所偏差
     * <p>
     * 在绘制矩形以及其它图形的时候,矩形(图形)的边界是边框的中心,不是边框的边界
     * 所以在绘制带边框的图形的时候应该减去边框宽度的一半
     * https://blog.csdn.net/a512337862/article/details/74161988
     */
    private void drawConnectStyle(Canvas canvas) {
        //每次重新绘制时,先将rectF重置下
        mRectFConnect.setEmpty();
        //需要减去边框的一半
        mRectFConnect.set(
                mBorderSize / 2,
                mBorderSize / 2,
                getWidth() - mBorderSize / 2,
                getHeight() - mBorderSize / 2
        );
        canvas.drawRoundRect(mRectFConnect, mCornerSize, mCornerSize, mPaintBorder);
        //绘制分割线
        drawDivisionLine(canvas);
    }

    /**
     * 分割线条数为内容框数目-1
     * 这里startX应该要加上左侧边框的宽度
     * 应该还需要加上分割线的一半
     * 至于startY和stopY不是 mBorderSize/2 而是 mBorderSize
     * startX是计算整个宽度的,需要算上左侧的边框宽度,所以不是+mBorderSize/2 而是+mBorderSize
     * startY和stopY：分割线是紧挨着边框内部的,所以应该是mBorderSize,而不是mBorderSize/2
     */
    private void drawDivisionLine(Canvas canvas) {
        float stopY = getHeight() - mBorderSize;
        for (int i = 0; i < mContentNumber - 1; i++) {
            //对于分割线条,startX = stopX
            float startX = (i + 1) * getContentItemWidth() + i * mDivisionLineSize + mBorderSize + mDivisionLineSize / 2;
            canvas.drawLine(startX, mBorderSize, startX, stopY, mPaintDivisionLine);
        }
    }

    /**
     * 计算3种样式下,相应每个字符item的宽度
     */
    private float getContentItemWidth() {
        //计算每个密码字符所占的宽度,每种输入框样式下,每个字符item所占宽度也不一样
        float tempWidth;
        switch (mInputBoxStyle) {
            case INPUT_BOX_STYLE_SINGLE:
                //单个输入框样式：宽度-间距宽度(字符数-1)*每个间距宽度-每个输入框的左右边框宽度
                tempWidth = getWidth() - (mContentNumber - 1) * mSpaceSize - 2 * mContentNumber * mBorderSize;
                break;
            case INPUT_BOX_STYLE_UNDERLINE:
                //下划线样式：宽度-间距宽度(字符数-1)*每个间距宽度
                tempWidth = getWidth() - (mContentNumber - 1) * mSpaceSize;
                break;
            case INPUT_BOX_STYLE_CONNECT:
                //矩形输入框样式：宽度-左右两边框宽度-分割线宽度(字符数-1)*每个分割线宽度
            default:
                tempWidth = getWidth() - (mDivisionLineSize * (mContentNumber - 1)) - 2 * mBorderSize;
                break;
        }
        return tempWidth / mContentNumber;
    }

    /**
     * 根据view的测量宽度,计算每个item的宽度
     *
     * @param measureWidth view的measure
     * @return onMeasure时的每个item宽度
     */
    private float getContentItemWidthOnMeasure(int measureWidth) {
        //计算每个密码字符所占的宽度,每种输入框样式下,每个字符item所占宽度也不一样
        float tempWidth;
        switch (mInputBoxStyle) {
            case INPUT_BOX_STYLE_SINGLE:
                //单个输入框样式：宽度-间距宽度(字符数-1)*每个间距宽度-每个输入框的左右边框宽度
                tempWidth = measureWidth - (mContentNumber - 1) * mSpaceSize - 2 * mContentNumber * mBorderSize;
                break;
            case INPUT_BOX_STYLE_UNDERLINE:
                //下划线样式：宽度-间距宽度(字符数-1)*每个间距宽度
                tempWidth = measureWidth - (mContentNumber - 1) * mSpaceSize;
                break;
            case INPUT_BOX_STYLE_CONNECT:
                //矩形输入框样式：宽度-左右两边框宽度-分割线宽度(字符数-1)*每个分割线宽度
            default:
                tempWidth = measureWidth - (mDivisionLineSize * (mContentNumber - 1)) - 2 * mBorderSize;
                break;
        }
        return tempWidth / mContentNumber;
    }

    /**
     * 计算绘制文本的基线
     *
     * @param paint      绘制文字的画笔
     * @param halfHeight 高度的一半
     */
    private float getTextBaseline(Paint paint, float halfHeight) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        return halfHeight + dy;
    }

    /**
     * 设置密码是否可见
     */
    public void setContentShowMode(int mode) {
        if (mode != CONTENT_SHOW_MODE_PASSWORD && mode != CONTENT_SHOW_MODE_TEXT) {
            throw new IllegalArgumentException(
                    "the value of the parameter must be one of" +
                            "{1:EDIT_SHOW_MODE_PASSWORD} or " +
                            "{2:EDIT_SHOW_MODE_TEXT}"
            );
        }
        mContentShowMode = mode;
        invalidate();
    }

    /**
     * 获取密码显示模式
     */
    public int getContentShowMode() {
        return mContentShowMode;
    }

    /**
     * 设置输入框样式
     */
    public void setInputBoxStyle(int inputBoxStyle) {
        if (inputBoxStyle != INPUT_BOX_STYLE_CONNECT
                && inputBoxStyle != INPUT_BOX_STYLE_SINGLE
                && inputBoxStyle != INPUT_BOX_STYLE_UNDERLINE
        ) {
            throw new IllegalArgumentException(
                    "the value of the parameter must be one of" +
                            "{1:INPUT_BOX_STYLE_CONNECT}, " +
                            "{2:INPUT_BOX_STYLE_SINGLE} or " +
                            "{3:INPUT_BOX_STYLE_UNDERLINE}"
            );
        }
        mInputBoxStyle = inputBoxStyle;
        // 这里没有调用invalidate因为会存在问题
        // invalidate会重绘,但是不会去重新测量,当输入框样式切换的之后,item的宽度其实是有变化的,所以此时需要重新测量
        // requestLayout,调用onMeasure和onLayout,不一定会调用onDraw,当view的l,t,r,b发生改变时会调用onDraw
        requestLayout();
        //invalidate();
    }

    public void setOnInputListener(OnInputListener listener) {
        this.inputListener = listener;
    }

    /**
     * 通过复写onTextChanged来实现对输入的监听
     * 如果在onDraw里面监听text的输入长度来实现,会重复的调用该方法,就不妥当
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        String content = text.toString().trim();
        if (inputListener != null) {
            if (content.length() == mContentNumber) {
                inputListener.onInputFinished(content);
            } else {
                inputListener.onInputChanged(content);
            }
        }
    }

    /**
     * 获取输入框样式
     */
    public int getInputBoxStyle() {
        return mInputBoxStyle;
    }

    private float dp2px(float dpValue) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                Resources.getSystem().getDisplayMetrics()
        );
    }

    private float sp2px(float spValue) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                spValue,
                Resources.getSystem().getDisplayMetrics()
        );
    }

    /**
     * 光标Runnable
     * 通过Runnable每500ms执行重绘,每次runnable通过改变画笔的alpha值来使光标产生闪烁的效果
     */
    private class CursorRunnable implements Runnable {

        @Override
        public void run() {
            //获取光标画笔的alpha值
            int alpha = mPaintCursor.getAlpha();
            //设置光标画笔的alpha值
            mPaintCursor.setAlpha(alpha == 0 ? 255 : 0);
            invalidate();
            postDelayed(this, mCursorDuration);
        }
    }
}
