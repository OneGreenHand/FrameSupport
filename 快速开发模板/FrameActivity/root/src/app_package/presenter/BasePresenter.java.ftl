package ${packageName}.presenter;
import com.frame.base.BasePresenter;
import ${packageName}.view <#if selectViewType == "activity">.activity.${activityClass}Activity<#else>.fragment.${activityClass}Fragment</#if>;

<#if selectViewType == 'activity'>
public class ${activityClass}Pt extends BasePresenter<${activityClass}Activity>  {

    public ${activityClass}Pt(${activityClass}Activity activity) {
        super(activity);
    }

}
<#elseif selectViewType == 'fragment'>
public class ${activityClass}Pt extends BasePresenter<${activityClass}Fragment>  {

    public ${activityClass}Pt(${activityClass}Fragment fragment) {
        super(fragment);
    }

}
</#if>