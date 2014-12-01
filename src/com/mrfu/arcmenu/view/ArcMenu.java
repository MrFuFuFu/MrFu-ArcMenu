/*
 * Copyright (C) 2012 Capricorn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mrfu.arcmenu.view;

import com.mrfu.arcmenu.R;
import com.mrfu.arcmenu.view.ArcLayout.OnGetPostionListener;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A custom view that looks like the menu in <a href="https://path.com">Path
 * 2.0</a> (for iOS).
 * 
 * @author Capricorn
 * 
 */
public class ArcMenu extends RelativeLayout {
	private ArcLayout mArcLayout;

    private ImageView mHintView;

	private View panel_arcmenu;

	private TextView tv_createring;

	private TextView tv_customring;

    public ArcMenu(Context context) {
        super(context);
        init(context);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        applyAttrs(attrs);
    }

    private void init(Context context) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.arc_menu, this);

        mArcLayout = (ArcLayout) findViewById(R.id.item_layout);
        
        panel_arcmenu = findViewById(R.id.panel_arcmenu);
        tv_createring = (TextView)findViewById(R.id.tv_createring);
        tv_customring = (TextView)findViewById(R.id.tv_customring);

        final ViewGroup controlLayout = (ViewGroup) findViewById(R.id.control_layout);
        controlLayout.setClickable(true);
        controlLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    mOnAddBtnClickListener.onAddBtnClick(mArcLayout.isExpanded());
                	addClickChange();
                }
                return false;
            }
        });

        mHintView = (ImageView) findViewById(R.id.control_hint);
        
        panel_arcmenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//		    	Log.i("MrFu", "----");
		    	addClickChange();
			}
		});
        
        mArcLayout.setOnGetPostionListener(new OnGetPostionListener() {
			@Override
			public void onGetPostion(boolean expanded, float x_left, float y_left, float x_right, float y_right) {
				if (isFirst) {
					isFirst = false;
					expanded = true;
				}
				if (!expanded) {
					tv_createring.setVisibility(View.INVISIBLE);
					tv_customring.setVisibility(View.INVISIBLE);
					return;
				}
//				Log.i("MrFu", "expanded = " + expanded + "  x_left = " + x_left + "  y_left = " + y_left + "  x_right = " + x_right + "  y_right = " + y_right);
				tv_createring.setVisibility(View.VISIBLE);
				tv_customring.setVisibility(View.VISIBLE);
//				Log.i("MrFu", "tv_customring.getHeight() = " + tv_customring.getHeight());
				tv_createring.setX(x_left - 10);
				tv_createring.setY(y_left - tv_createring.getHeight());
				tv_customring.setX(x_right - 10);
				tv_customring.setY(y_right - tv_customring.getHeight());
			}
		});
    }
    private boolean isFirst = true;
    
    private void addClickChange() {
		if (!mArcLayout.isExpanded()) {
			panel_arcmenu.setVisibility(View.VISIBLE);
		}else {
			panel_arcmenu.setVisibility(View.INVISIBLE);
			tv_createring.setVisibility(View.INVISIBLE);
			tv_customring.setVisibility(View.INVISIBLE);
		}
		Animation animation = createHintSwitchAnimation(mArcLayout.isExpanded());
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				if (mArcLayout.isExpanded() && null != mItem1) {
					mItem1.setVisibility(View.VISIBLE);
				}
				if (mArcLayout.isExpanded() && null != mItem2) {
					mItem2.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				if (!mArcLayout.isExpanded() && null != mItem1) {
					mItem1.setVisibility(View.INVISIBLE);
				}
				if (!mArcLayout.isExpanded() && null != mItem2) {
					mItem2.setVisibility(View.INVISIBLE);
				}
			}
		});
		mHintView.startAnimation(animation);
		mArcLayout.switchState(true);
	}

    private void applyAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArcLayout, 0, 0);

            float fromDegrees = a.getFloat(R.styleable.ArcLayout_fromDegrees, ArcLayout.DEFAULT_FROM_DEGREES);
            float toDegrees = a.getFloat(R.styleable.ArcLayout_toDegrees, ArcLayout.DEFAULT_TO_DEGREES);
            mArcLayout.setArc(fromDegrees, toDegrees);

            int defaultChildSize = mArcLayout.getChildSize();
            int newChildSize = a.getDimensionPixelSize(R.styleable.ArcLayout_childSize, defaultChildSize);
            mArcLayout.setChildSize(newChildSize);

            a.recycle();
        }
    }

    private View mItem1, mItem2;
    public void addItem(int index, View view, OnClickListener listener) {
        mArcLayout.addView(view);
        view.setVisibility(View.INVISIBLE);
        if (index == 0) {
        	mItem1 = view;
		}else if (index == 1) {
			mItem2 = view;
		}
        view.setOnClickListener(getItemClickListener(listener));
    }

    private OnClickListener getItemClickListener(final OnClickListener listener) {
        return new OnClickListener() {

            @Override
            public void onClick(final View viewClicked) {
        		if (mArcLayout.isExpanded()) {
        			panel_arcmenu.setVisibility(View.INVISIBLE);
					tv_createring.setVisibility(View.INVISIBLE);
					tv_customring.setVisibility(View.INVISIBLE);
        		}
                Animation animation = bindItemAnimation(viewClicked, true, 400);
                animation.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                itemDidDisappear();
                            }
                        }, 0);
                    }
                });

                final int itemCount = mArcLayout.getChildCount();
                for (int i = 0; i < itemCount; i++) {
                    View item = mArcLayout.getChildAt(i);
                    if (viewClicked != item) {
                        bindItemAnimation(item, false, 300);
                    }
                }

                mArcLayout.invalidate();
                Animation animation1 = createHintSwitchAnimation(true);
                animation1.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					@Override
					public void onAnimationEnd(Animation animation) {
						if (null != mItem1) {
							mItem1.setVisibility(View.INVISIBLE);
						}
						if (null != mItem2) {
							mItem2.setVisibility(View.INVISIBLE);
						}
					}
				});
                mHintView.startAnimation(animation1);
                if (listener != null) {
                    listener.onClick(viewClicked);
                }
            }
        };
    }

    private Animation bindItemAnimation(final View child, final boolean isClicked, final long duration) {
        Animation animation = createItemDisapperAnimation(duration, isClicked);
        child.setAnimation(animation);

        return animation;
    }

    private void itemDidDisappear() {
        final int itemCount = mArcLayout.getChildCount();
        for (int i = 0; i < itemCount; i++) {
            View item = mArcLayout.getChildAt(i);
            item.clearAnimation();
        }

        mArcLayout.switchState(false);
    }

    private static Animation createItemDisapperAnimation(final long duration, final boolean isClicked) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, isClicked ? 2.0f : 0.0f, 1.0f, isClicked ? 2.0f : 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));

        animationSet.setDuration(duration);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setFillAfter(true);

        return animationSet;
    }

    private static Animation createHintSwitchAnimation(final boolean expanded) {
        Animation animation = new RotateAnimation(expanded ? 45 : 0, expanded ? 0 : 45, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setStartOffset(0);
        animation.setDuration(100);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setFillAfter(true);

        return animation;
    }
}
