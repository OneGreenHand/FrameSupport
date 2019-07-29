package ${packageName}.view.adapter;

import com.frame.adapter.BaseQuickAdapter;
import com.frame.base.BaseQuickHolder;

public class ${activityClass}Adapter extends BaseQuickAdapter<${activityClass}Bean, BaseQuickHolder> {

    public ${activityClass}Adapter() {
        super(R.layout.item_${activityClass?lower_case});
    }
	
    @Override
    protected void convert(BaseQuickHolder helper, ${activityClass}Bean item) {
        
    }
}