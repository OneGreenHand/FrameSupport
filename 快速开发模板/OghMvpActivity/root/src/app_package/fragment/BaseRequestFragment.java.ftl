package ${packageName}.view.fragment;

import android.os.Bundle;

public class ${activityClass}Fragment extends BaseRequestFragment<${activityClass}Pt,BaseBean> {

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
        return <#if generateLayout>R.layout.${layoutFragmentName}<#else>0</#if>;
    }	

}