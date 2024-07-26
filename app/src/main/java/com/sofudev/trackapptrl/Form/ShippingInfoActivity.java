package com.sofudev.trackapptrl.Form;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Fragment.CartShippingFragment;
import com.sofudev.trackapptrl.Fragment.Step2Fragment;
import com.sofudev.trackapptrl.Fragment.Step3Fragment;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ShippingInfoActivity extends AppCompatActivity {
    Config config = new Config();
    String URLGETSHIPPINGMODE = config.Ip_address + config.shipping_mode;

    @SuppressLint("StaticFieldLeak")
    public static View viewCircleFinishStep1, viewCircleCurrentStep1;
    @SuppressLint("StaticFieldLeak")
    public static View viewHorizontalOn1, viewHorizontalOff1;
    @SuppressLint("StaticFieldLeak")
    public static View viewCircleFinishStep2, viewCircleCurrentStep2;
    @SuppressLint("StaticFieldLeak")
    public static View viewHorizontalOn2, viewHorizontalOff2;
    @SuppressLint("StaticFieldLeak")
    public static View viewCircleFinishStep3, viewCircleCurrentStep3;
    public static int width = 0;
    public static int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_info);

        getShippingMode();
        loadComponent();
    }

    private void getShippingMode(){
        StringRequest request = new StringRequest(Request.Method.POST, URLGETSHIPPINGMODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.getString("shipping_template").equals("WEB_FLOW"))
                    {
                        loadFragment(new CartShippingFragment());
                    }
                    else
                    {
                        loadFragment(new Step3Fragment());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ShippingInfoActivity.class.getSimpleName(), error.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    private void loadComponent() {
        viewCircleFinishStep1       = findViewById(R.id.view_circle_finish_step_1);
        viewCircleCurrentStep1      = findViewById(R.id.view_circle_current_step_1);
        viewHorizontalOn1           = findViewById(R.id.view_horizontal_on_1);
        viewHorizontalOff1          = findViewById(R.id.view_horizontal_off_1);
        viewCircleFinishStep2       = findViewById(R.id.view_circle_finish_step_2);
        viewCircleCurrentStep2      = findViewById(R.id.view_circle_current_step_2);
        viewHorizontalOn2           = findViewById(R.id.view_horizontal_on_2);
        viewHorizontalOff2          = findViewById(R.id.view_horizontal_off_2);
        viewCircleFinishStep3       = findViewById(R.id.view_circle_finish_step_3);
        viewCircleCurrentStep3      = findViewById(R.id.view_circle_current_step_3);

        RelativeLayout rlStep1 = findViewById(R.id.relative_layout_container_step_1);
        RelativeLayout rlStep2 = findViewById(R.id.relative_layout_container_step_2);
        RelativeLayout rlStep3 = findViewById(R.id.relative_layout_container_step_3);

        ImageView btnBack      = findViewById(R.id.shipping_info_btnback);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlStep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position <= 2)
                {
                    position--;
                }
                else
                {
                    position -= 2;
                    backToStepOrangTua();

                    getSupportFragmentManager().popBackStack();
                }

                backToStepBiodataDiri();
                getSupportFragmentManager().popBackStack();

                Log.d(ShippingInfoActivity.class.getSimpleName(), "Pos : " + position);
            }
        });

        rlStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position <= 1) {
                    goToStepOrangTua();
                    Step2Fragment step2Fragment = new Step2Fragment();
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right, R.anim.slide_in_from_left, R.anim.slide_out_from_left)
                            .replace(R.id.frame_layout, step2Fragment)
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    position--;
                    backToStepOrangTua();
                    getSupportFragmentManager().popBackStack();
                }

                Log.d(ShippingInfoActivity.class.getSimpleName(), "Pos : " + position);
            }
        });

        rlStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < 2) {
                    goToStepOrangTua();
                }

                goToStepUlasan();

                Step3Fragment step3Fragment = new Step3Fragment();
                  getSupportFragmentManager().beginTransaction()
                          .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right, R.anim.slide_in_from_left, R.anim.slide_out_from_left)
                          .replace(R.id.frame_layout, step3Fragment)
                          .addToBackStack(null)
                          .commit();
                Log.d(ShippingInfoActivity.class.getSimpleName(), "Pos : " + position);
            }
        });

        viewHorizontalOff1.post(new Runnable() {
            @Override
            public void run() {
                width = viewHorizontalOff1.getWidth();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, fragment)
                .commit();
    }

    public static void goToStepOrangTua() {
        position = 2;
        ObjectAnimator objectAnimatorCircleFinish = ObjectAnimator.ofFloat(viewCircleFinishStep1, "alpha", 0, 1);
        objectAnimatorCircleFinish.setDuration(300);
        objectAnimatorCircleFinish.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                ObjectAnimator objectAnimatorHorizontal = ObjectAnimator.ofFloat(viewHorizontalOff1, "translationX", 0, width);
                objectAnimatorHorizontal.setDuration(300);
                objectAnimatorHorizontal.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        ObjectAnimator objectAnimatorCircle = ObjectAnimator.ofFloat(viewCircleCurrentStep2, "alpha", 0, 1);
                        objectAnimatorCircle.setDuration(300);
                        objectAnimatorCircle.start();
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                });
                objectAnimatorHorizontal.start();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
        objectAnimatorCircleFinish.start();
    }

    public static void goToStepUlasan() {
        position = 3;
        ObjectAnimator objectAnimatorCircleFinish = ObjectAnimator.ofFloat(viewCircleFinishStep2, "alpha", 0, 1);
        objectAnimatorCircleFinish.setDuration(300);
        objectAnimatorCircleFinish.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                ObjectAnimator objectAnimatorHorizontal = ObjectAnimator.ofFloat(viewHorizontalOff2, "translationX", 0, width);
                objectAnimatorHorizontal.setDuration(300);
                objectAnimatorHorizontal.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        ObjectAnimator objectAnimatorCircle = ObjectAnimator.ofFloat(viewCircleCurrentStep3, "alpha", 0, 1);
                        objectAnimatorCircle.setDuration(300);
                        objectAnimatorCircle.start();
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                });
                objectAnimatorHorizontal.start();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
        objectAnimatorCircleFinish.start();
    }

    public static void backToStepBiodataDiri() {
        final ObjectAnimator objectAnimatorCircleCurrent = ObjectAnimator.ofFloat(viewCircleCurrentStep2, "alpha", 1, 0);
        objectAnimatorCircleCurrent.setDuration(300);
        objectAnimatorCircleCurrent.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                ObjectAnimator objectAnimatorHorizontal = ObjectAnimator.ofFloat(viewHorizontalOff1, "translationX", width, 0);
                objectAnimatorHorizontal.setDuration(300);
                objectAnimatorHorizontal.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        ObjectAnimator objectAnimatorCircleFinish = ObjectAnimator.ofFloat(viewCircleFinishStep1, "alpha", 1, 0);
                        objectAnimatorCircleFinish.setDuration(300);
                        objectAnimatorCircleFinish.start();
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                });
                objectAnimatorHorizontal.start();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
        objectAnimatorCircleCurrent.start();
    }

    public static void backToStepOrangTua() {
        ObjectAnimator objectAnimatorCurrent = ObjectAnimator.ofFloat(viewCircleCurrentStep3, "alpha", 1, 0);
        objectAnimatorCurrent.setDuration(300);
        objectAnimatorCurrent.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                ObjectAnimator objectAnimatorHorizontal = ObjectAnimator.ofFloat(viewHorizontalOff2, "translationX", width, 0);
                objectAnimatorHorizontal.setDuration(300);
                objectAnimatorHorizontal.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        ObjectAnimator objectAnimatorCircleFinish = ObjectAnimator.ofFloat(viewCircleFinishStep2, "alpha", 1, 0);
                        objectAnimatorCircleFinish.setDuration(300);
                        objectAnimatorCircleFinish.start();
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {

                    }
                });
                objectAnimatorHorizontal.start();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
        objectAnimatorCurrent.start();
    }

    @Override
    public void onBackPressed() {
//        position--;
//        if (position == 1)
//        {
//            backToStepBiodataDiri();
//        }
//        else if (position == 2)
//        {
//            backToStepOrangTua();
//        }
//        super.onBackPressed();
    }
 }