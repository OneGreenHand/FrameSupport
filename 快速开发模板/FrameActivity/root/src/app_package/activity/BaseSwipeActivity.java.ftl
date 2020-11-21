package ${packageName}.view.activity;

import android.os.Bundle;
import com.frame.bean.BaseBean;
import com.frame.base.activity.BaseSwipeActivity;
import ${packageName}.databinding.Activity${activityClass}Binding;
import ${packageName}.presenter.${activityClass}Pt;

public class ${activityClass}Activity extends BaseSwipeActivity<Activity${activityClass}Binding,${activityClass}Pt,BaseBean> {

    @Override
    protected void onRefreshRequest() {
    }

    @Override
    protected ${activityClass}Pt setPresenter() {
        return new ${activityClass}Pt(this);
    }

    @Override
    protected void reRequest() {
    }

    @Override
    public void requestSuccess(BaseBean data, Object tag, int pageIndex, int pageCount) {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }

}