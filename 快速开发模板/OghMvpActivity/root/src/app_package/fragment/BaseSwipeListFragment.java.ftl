package ${packageName}.view.fragment;

import android.os.Bundle;

public class ${activityClass}Fragment extends BaseSwipeListFragment<${activityClass}Pt,BaseBean,${activityClass}Bean> {

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
        return <#if generateLayout>R.layout.${layoutFragmentName}<#else>0</#if>;
    }	

}