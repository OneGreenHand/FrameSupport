package ${packageName}.view.fragment;

import android.os.Bundle;
import com.frame.bean.BaseBean;
import com.frame.base.fragment.BaseRequestFragment;
import ${packageName}.databinding.Fragment${activityClass}Binding;
import ${packageName}.presenter.${activityClass}Pt;

public class ${activityClass}Fragment extends BaseRequestFragment<Fragment${activityClass}Binding,${activityClass}Pt,BaseBean> {

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