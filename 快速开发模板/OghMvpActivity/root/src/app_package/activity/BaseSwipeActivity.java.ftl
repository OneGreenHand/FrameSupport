package ${packageName}.view.activity;

import android.os.Bundle;

public class ${activityClass}Activity extends BaseSwipeActivity<${activityClass}Pt,BaseBean> {

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

    @Override
    protected int getLayoutID() {     
        return <#if generateLayout>R.layout.${layoutName}<#else>0</#if>;
    }

}