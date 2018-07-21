package com.gameplaycoder.cartrell.guardiannewsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SectionActivity extends AppCompatActivity {

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // static / const
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////
  public static final String INTENT_PARAM_SECTION = "section";

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // members
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////
  private Unbinder mUnbinder;

  @BindView(R.id.sections_radio_group)
  RadioGroup mRadioGroup;

  @BindView(R.id.btn_section_clear)
  Button mBtnClear;

  @BindView(R.id.btn_section_submit)
  Button mBtnSubmit;

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // protected
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // onCreate
  //=======================================================================================
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(com.gameplaycoder.cartrell.guardiannewsapp.R.layout.activity_section);

    // bind view using butter knife
    mUnbinder = ButterKnife.bind(this);

    initSubmitButton();
    initClearButton();
  }

  //=======================================================================================
  // onDestroy
  //=======================================================================================
  @Override
  protected void onDestroy() {
    super.onDestroy();

    // unbind all views bound by butterknife
    mUnbinder.unbind();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // private
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // getSectionFromSelectedRad
  //=======================================================================================
  private String getSectionFromSelectedRad() {
    int radioButtonId = mRadioGroup.getCheckedRadioButtonId();
    if (radioButtonId == 0) {
      return(null);
    }

    RadioButton radioButton = findViewById(radioButtonId);
    return((String)radioButton.getTag());
  }

  //=======================================================================================
  // initClearButton
  //=======================================================================================
  private void initClearButton() {
    mBtnClear.setOnClickListener(new View.OnClickListener() {

      //===================================================================================
      // onClick
      //===================================================================================
      @Override
      public void onClick(View view) {
        mRadioGroup.clearCheck();
      }
    });
  }

  //=======================================================================================
  // initSubmitButton
  //=======================================================================================
  private void initSubmitButton() {
    final Activity activity = this;
    final Context context = this;

    mBtnSubmit.setOnClickListener(new View.OnClickListener() {

      //===================================================================================
      // onClick
      //===================================================================================
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, MainActivity.class);

        String section = getSectionFromSelectedRad();
        if (section != null) {
          intent.putExtra(INTENT_PARAM_SECTION, section);
        }

        activity.startActivity(intent);
      }
    });
  }
}
