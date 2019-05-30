package ${packageName}.view.fragment;

import android.os.Bundle;

public class ${activityClass}Fragment extends BaseFragment {

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
	
}