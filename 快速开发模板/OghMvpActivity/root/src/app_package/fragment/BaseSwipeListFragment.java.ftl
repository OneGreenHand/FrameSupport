package ${packageName}.view.fragment;

import android.os.Bundle;

public class ${activityClass}Fragment extends BaseSwipeListFragment<${activityClass}Pt,BaseBean,${activityClass}Bean> {

    @Override
    public BaseQuickAdapter<${activityClass}Bean,BaseQuickHolder> setAdapter() {
        return new ${activityClass}Adapter(this);
    }

    @Override
    public void loadMoreListRequest(int page) {

    }

    @Override
    protected void onRefreshRequest() {

    }

    @Override
    protected ${activityClass}Pt setPresenter() {
        return  new ${activityClass}Pt(mActivity);
    }

    @Override
    protected void reRequest() {

    }

    @Override
    public void requestSuccess(BaseBean data, BaseModel.LoadMode loadMode, Object tag, int pageCount) {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutID() {     
        return <#if generateLayout>R.layout.${layoutFragmentName}<#else>0</#if>;
    }
	
	@Override
    public void initImmersionBar() {
        
    }

}