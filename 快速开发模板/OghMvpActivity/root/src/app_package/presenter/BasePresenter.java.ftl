package ${packageName}.presenter;

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