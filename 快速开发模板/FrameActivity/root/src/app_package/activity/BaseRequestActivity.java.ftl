package ${packageName}.view.activity;

import android.os.Bundle;
import com.frame.bean.BaseBean;
import com.frame.base.activity.BaseRequestActivity;
import ${packageName}.databinding.Activity${activityClass}Binding;
import ${packageName}.presenter.${activityClass}Pt;

public class ${activityClass}Activity extends BaseRequestActivity<Activity${activityClass}Binding,${activityClass}Pt,BaseBean> {

    @Override
    protected ${activityClass}Pt setPresenter() {
        return new ${activityClass}Pt(this);
    }

    @Override
    public void requestSuccess(BaseBean data, Object tag, int pageIndex, int pageCount) {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }

}