package ${packageName}.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.frame.base.BaseQuickHolder;

public class ${activityClass}Adapter extends BaseQuickAdapter<${activityClass}Bean, BaseQuickHolder> implements LoadMoreModule{

    public ${activityClass}Adapter() {
        super(R.layout.item_${activityClass?lower_case});
    }
	
    @Override
    protected void convert(BaseQuickHolder helper, ${activityClass}Bean item) {
    }
}