package ${packageName}.view.activity;

import android.os.Bundle;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.frame.base.BaseQuickHolder;
import com.frame.base.activity.BaseSwipeListActivity;
import com.frame.bean.BaseBean;
import ${packageName}.bean.${activityClass}Bean;
import ${packageName}.databinding.Activity${activityClass}Binding;
import ${packageName}.presenter.${activityClass}Pt;
import ${packageName}.view.adapter.${activityClass}Adapter;

public class ${activityClass}Activity extends BaseSwipeListActivity<Activity${activityClass}Binding,${activityClass}Pt,BaseBean,${activityClass}Bean> {

    @Override
    public BaseQuickAdapter<${activityClass}Bean,BaseQuickHolder> setAdapter() {
        return new ${activityClass}Adapter();
    }

    @Override
    public void loadMoreListRequest(int page) {
    }

    @Override
    protected void onRefreshRequest() {
    }

    @Override
    protected ${activityClass}Pt setPresenter() {
        return  new ${activityClass}Pt(this);
    }

    @Override
    public void requestSuccess(BaseBean data,  Object tag, int pageIndex, int pageCount) {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
    }
}