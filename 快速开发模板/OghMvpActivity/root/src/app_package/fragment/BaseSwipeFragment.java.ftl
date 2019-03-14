package ${packageName}.view.fragment;

import android.os.Bundle;

public class ${activityClass}Fragment extends BaseSwipeFragment<${activityClass}Pt,BaseBean> {

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