package com.example.linjun.douyu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by linjun on 2017/1/3.
 */

public class DouYuView extends ImageView {
    private Paint paint;
    private Bitmap bitmap;
    private int unitHeight;
    private  int unitWidth;
    private int unitWidthScale;
    private int unitHeigthScale;
    private int unitRandomX;
    private int unitRandomY;
    private  float unitMoveDistance=0;
    private  Bitmap unitBp;
    private Bitmap mshowBp;
    private  Bitmap mShadeBp;
    private  boolean needRotate;
    private  int rotate;
    public  int DEFAULT_DEVIATE;
    private  boolean isReSet=true;
    interface onPuzzleListener{
        public void onSuccess();
        public void onFail();

    }
 private  onPuzzleListener listener;
    public void setPuzzleListener(onPuzzleListener mlistener){
        this.listener=mlistener;
    }

    public DouYuView(Context context) {
        super(context);

    }

    public DouYuView(Context context, AttributeSet attrs, Paint paint) {
        super(context, attrs);
        this.paint = paint;
    }

    public DouYuView(Context context, AttributeSet attrs, int defStyleAttr, Paint paint) {
        super(context, attrs, defStyleAttr);
        this.paint = paint;
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.DouYuView);
        unitWidth=ta.getDimensionPixelOffset(R.styleable.DouYuView_unitWidth,0);
        unitHeight=ta.getDimensionPixelOffset(R.styleable.DouYuView_unitHeight,0);
        unitHeigthScale=ta.getInteger(R.styleable.DouYuView_unitHeightScale,0);
        unitWidthScale=ta.getInteger(R.styleable.DouYuView_unitWidthScale,0);
        Drawable showBp=ta.getDrawable(R.styleable.DouYuView_unitShowSrc);
        mshowBp=drawableToBitamp(showBp);
        Drawable shadeBp=ta.getDrawable(R.styleable.DouYuView_unitShadeSrc);
        mShadeBp=drawableToBitamp(shadeBp);
        needRotate=ta.getBoolean(R.styleable.DouYuView_needRotate,true);
        DEFAULT_DEVIATE=ta.getInteger(R.styleable.DouYuView_deviate,10);
        ta.recycle();
        paint=new Paint();
        paint.setAntiAlias(true);
        if (needRotate){
            rotate= (int) ((Math.random()*3)*90);
        }else {
            rotate=0;
        }

    }
  private void initUnitXY(){
       unitRandomX= (int) (Math.random()*(bitmap.getWidth()-unitWidth));
      unitRandomY= (int) (Math.random()*(bitmap.getHeight()-unitHeight));
      if (unitRandomX<=bitmap.getWidth()/2){
          unitRandomX=unitRandomX+bitmap.getWidth();
      }
      if (unitRandomY<bitmap.getHeight()/2){

          unitRandomY=unitRandomY+bitmap.getHeight()/2;
      }

 return;

  }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isReSet){
            bitmap=getBaseBitmap();
            if (0==unitWidth){
                unitWidth=bitmap.getWidth()/unitWidthScale;
            }
            if (0==unitHeight){
                unitHeight=bitmap.getWidth()/unitHeigthScale;
            }
            initUnitXY();
            unitBp=Bitmap.createBitmap(bitmap,unitRandomX,unitRandomY,unitWidth,unitHeight);
        }
        isReSet=false;
        canvas.drawBitmap(drawTargetBitmap(),unitRandomX,unitRandomY,paint);
        canvas.drawBitmap(drawResultBitmap(unitBp),unitMoveDistance,unitRandomY,paint);
    }

    public  void reSet(){
        isReSet=true;
        unitMoveDistance=0;
        if (needRotate){
            rotate= (int) ((Math.random()*3)*90);

        }else {
            rotate=0;
        }
        invalidate();


    }
       public  float getAverageDistance(int max){
           return  (float) (bitmap.getWidth()-unitWidth)/max;
       }
public  void setUnitMoveDistance(float distance){
    unitMoveDistance=distance;
    if (unitMoveDistance>bitmap.getWidth()-unitWidth){
        unitMoveDistance=bitmap.getWidth()-unitWidth;
    }

    invalidate();

}
      public  void testPuzzle(){
          if (Math.abs(unitMoveDistance-unitRandomX)<=DEFAULT_DEVIATE){
              if (null!=listener){
                  listener.onSuccess();
              }
          }else {
              if (null==listener){
                  listener.onFail();
              }
          }


      }

    private Bitmap drawResultBitmap(Bitmap unitBp) {
         Bitmap shadeB;
        if (null!=mShadeBp){
            shadeB=handleBitmap(mShadeBp,unitWidth,unitHeight);
        }else {
            shadeB=handleBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.shade),unitWidth,unitHeight);
        }
          if (needRotate){
              shadeB=handleBitmap(rotateBitmap(rotate,shadeB),unitWidth,unitHeight);

          }
           Bitmap resultBmp=Bitmap.createBitmap(unitWidth,unitHeight,Bitmap.Config.ARGB_8888);
        Paint p=new Paint();
        p.setAntiAlias(true);
        Canvas canvas=new Canvas(resultBmp);
        canvas.drawBitmap(shadeB,new Rect(0,0,unitWidth,unitHeight),new Rect(0,0,unitWidth,unitHeight),paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        canvas.drawBitmap(unitBp,new Rect(0,0,unitWidth,unitHeight),new Rect(0,0,unitWidth,unitHeight),paint);
        return  resultBmp;
    }

    private Bitmap drawTargetBitmap() {
        Bitmap showB;
        if (null!=mshowBp){
            showB=handleBitmap(mshowBp,unitWidth,unitHeight);
        }else {
            showB=handleBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.show),unitWidth,unitHeight);
        }
        if (needRotate){
            showB=handleBitmap(rotateBitmap(rotate,showB),unitWidth,unitHeight);
        }
 return showB;

    }

    private Bitmap rotateBitmap(int rotate, Bitmap showB) {
    }

    private Bitmap handleBitmap(Bitmap mShadeBp, int unitWidth, int unitHeight) {
    }

    private Bitmap getBaseBitmap() {
    }

    private Bitmap drawableToBitamp(Drawable showBp) {
    }


}
