package ${packageName}.view.activity;

import android.os.Bundle;

public class ${activityClass}Activity extends BaseActivity {

 @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutID() {     
        return <#if generateLayout>R.layout.${layoutName}<#else>0</#if>;
    }

}