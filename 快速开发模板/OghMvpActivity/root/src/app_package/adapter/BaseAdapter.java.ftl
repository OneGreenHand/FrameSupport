package ${packageName}.view.adapter;

public class ${activityClass}Adapter extends BaseQuickAdapter<${activityClass}Bean, BaseQuickHolder> {

    private Context mContext;
    public ${activityClass}Adapter(Context mContext) {
        super(R.layout.item_${activityClass});
        this.mContext=mContext;
    }
	
    @Override
    protected void convert(BaseQuickHolder helper, ${activityClass}Bean item) {
        
    }
}