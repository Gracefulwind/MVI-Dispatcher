package com.kunminx.purenote.ui.page;

import android.util.Log;

import com.kunminx.architecture.ui.page.BaseActivity;
import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.kunminx.architecture.ui.page.StateHolder;
import com.kunminx.purenote.BR;
import com.kunminx.purenote.R;
import com.kunminx.purenote.domain.intent.ComplexIntent;
import com.kunminx.purenote.domain.intent.Messages;
import com.kunminx.purenote.domain.message.PageMessenger;
import com.kunminx.purenote.domain.request.ComplexRequester;

import java.util.Objects;

public class MainActivity extends BaseActivity {
  private MainAtyStates mStates;
  private PageMessenger mMessenger;
  private ComplexRequester mComplexRequester;

  @Override
  protected void initViewModel() {
    mMessenger = getApplicationScopeViewModel(PageMessenger.class);
    mComplexRequester = getActivityScopeViewModel(ComplexRequester.class);
  }

  @Override
  protected DataBindingConfig getDataBindingConfig() {
    return new DataBindingConfig(R.layout.activity_main, BR.state, mStates);
  }

  /**
   * TODO tip 1：
   *  通过唯一出口 'dispatcher.output' 统一接收 '可信源' 回推之消息，根据 id 分流处理 UI 逻辑。
   *  ~
   *  Through the only exit 'dispatcher.output()' uniformly receives the message pushed back
   *  by the Single Source of Truth, and processes the UI logic according to the ID shunting.
   */
  @Override
  protected void onOutput() {
    mMessenger.output(this, messages -> {
      if (Objects.equals(messages.id, Messages.FinishActivity.ID)) finish();
    });

    mComplexRequester.output(this, complexIntent -> {
      switch (complexIntent.id) {
        case ComplexIntent.Test1.ID:
          Log.d("ComplexIntent", "---1");
          break;
        case ComplexIntent.Test2.ID:
          Log.d("ComplexIntent", "---2");
          break;
        case ComplexIntent.Test3.ID:
          Log.d("ComplexIntent", "---3");
          break;
        case ComplexIntent.Test4.ID:
          ComplexIntent.Test4 test4 = (ComplexIntent.Test4) complexIntent;
          Log.d("ComplexIntent", "---4 " + test4.resultCount1);
          break;
      }
    });
  }

  /**
   * TODO tip 2：
   *  通过唯一入口 'dispatcher.input' 发消息至 "可信源"，由其内部统一处理业务逻辑和结果分发。
   *  ~
   *  Through the unique entry 'dispatcher Input' sends a message to the Single Source of Truth,
   *  which processes the business logic and distributes the results internally.
   */
  @Override
  protected void onInput() {

    //TODO 此处展示通过 dispatcher.input 连续发送多事件而不被覆盖
    // ~
    // Here you can see through dispatcher Input sends multiple events continuously without being overwritten

    mComplexRequester.input(ComplexIntent.Test1(1));
    mComplexRequester.input(ComplexIntent.Test2(2));
    mComplexRequester.input(ComplexIntent.Test2(2));
    mComplexRequester.input(ComplexIntent.Test2(2));
    mComplexRequester.input(ComplexIntent.Test3(3));
    mComplexRequester.input(ComplexIntent.Test3(3));
    mComplexRequester.input(ComplexIntent.Test3(3));
    mComplexRequester.input(ComplexIntent.Test3(3));
  }

  public static class MainAtyStates extends StateHolder {
  }
}