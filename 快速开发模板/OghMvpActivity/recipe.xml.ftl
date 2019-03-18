<?xml version="1.0"?>	
<recipe>
<#if selectViewType == "activity">
	<#if generateLayout&&frameSupperActivity == "BaseSwipeList">   
	<instantiate from="root/src/app_package/xml/layout_rv_simple.xml.ftl"
                   to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />  
    <open file="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />
	<#elseif generateLayout>
	<instantiate from="root/src/app_package/xml/layout_simple.xml.ftl"
                   to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />  
    <open file="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />
    </#if>
	
    <#if frameSupperActivity == "Base">       
		 <instantiate from="root/src/app_package/activity/BaseActivity.java.ftl"
                        to="${escapeXmlAttribute(srcOut)}/view/activity/${activityClass}Activity.java" />
		 <open file="${escapeXmlAttribute(srcOut)}/view/activity/${activityClass}Activity.java" />
    <#elseif frameSupperActivity == "BaseSwipe">
        <instantiate from="root/src/app_package/activity/BaseSwipeActivity.java.ftl"
					   to="${escapeXmlAttribute(srcOut)}/view/activity/${activityClass}Activity.java" />
		 <open file="${escapeXmlAttribute(srcOut)}/view/activity/${activityClass}Activity.java" />
    <#elseif frameSupperActivity == "BaseSwipeList">
         <instantiate from="root/src/app_package/activity/BaseSwipeListActivity.java.ftl"
					    to="${escapeXmlAttribute(srcOut)}/view/activity/${activityClass}Activity.java" />
		 <open file="${escapeXmlAttribute(srcOut)}/view/activity/${activityClass}Activity.java" />
         <instantiate from="root/src/app_package/adapter/BaseAdapter.java.ftl"
                        to="${escapeXmlAttribute(srcOut)}/view/adapter/${activityClass}Adapter.java" />          
         <instantiate from="root/src/app_package/xml/item_adapter.xml.ftl"
                        to="${escapeXmlAttribute(resOut)}/layout/item_${activityClass}.xml" />
    <#elseif frameSupperActivity == "BaseRequest">
       <instantiate from="root/src/app_package/activity/BaseRequestActivity.java.ftl"
					  to="${escapeXmlAttribute(srcOut)}/view/activity/${activityClass}Activity.java" />
	   <open file="${escapeXmlAttribute(srcOut)}/view/activity/${activityClass}Activity.java" />
    </#if>

    <#if frameSupperActivity == "BaseSwipeList"||frameSupperActivity== "BaseSwipe"||frameSupperActivity == "BaseRequest">      
         <instantiate from="root/src/app_package/presenter/BasePresenter.java.ftl"
                        to="${escapeXmlAttribute(srcOut)}/presenter/${activityClass}Pt.java" />   
         <instantiate from="root/src/app_package/bean/BaseBean.java.ftl"
                        to="${escapeXmlAttribute(srcOut)}/bean/${activityClass}Bean.java" />					   
    </#if>


<#elseif selectViewType == "fragment">

	<#if generateLayout>   
	<instantiate from="root/src/app_package/xml/layout_simple.xml.ftl"
                   to="${escapeXmlAttribute(resOut)}/layout/${layoutFragmentName}.xml" />  
    <open file="${escapeXmlAttribute(resOut)}/layout/${layoutFragmentName}.xml" />
    </#if>

    <#if frameSupperFragment == "Base">   
		 <instantiate from="root/src/app_package/fragment/BaseFragment.java.ftl"
                        to="${escapeXmlAttribute(srcOut)}/view/fragment/${activityClass}Fragment.java" />
		<open file="${escapeXmlAttribute(srcOut)}/view/fragment/${activityClass}Fragment.java" />
    <#elseif frameSupperFragment == "BaseSwipe">
        <instantiate from="root/src/app_package/fragment/BaseSwipeFragment.java.ftl"
                       to="${escapeXmlAttribute(srcOut)}/view/fragment/${activityClass}Fragment.java" />
		<open file="${escapeXmlAttribute(srcOut)}/view/fragment/${activityClass}Fragment.java" />
    <#elseif frameSupperFragment == "BaseSwipeList">
        <instantiate from="root/src/app_package/fragment/BaseSwipeListFragment.java.ftl"
                       to="${escapeXmlAttribute(srcOut)}/view/fragment/${activityClass}Fragment.java" />
		<open file="${escapeXmlAttribute(srcOut)}/view/fragment/${activityClass}Fragment.java" />
        <instantiate from="root/src/app_package/adapter/BaseAdapter.java.ftl"
                       to="${escapeXmlAttribute(srcOut)}/view/adapter/${activityClass}Adapter.java" />      
        <instantiate from="root/src/app_package/xml/item_adapter.xml.ftl"
                       to="${escapeXmlAttribute(resOut)}/layout/item_${activityClass}.xml" />
    <#elseif frameSupperFragment == "BaseRequest">
        <instantiate from="root/src/app_package/fragment/BaseRequestFragment.java.ftl"
                       to="${escapeXmlAttribute(srcOut)}/view/fragment/${activityClass}Fragment.java" />
		<open file="${escapeXmlAttribute(srcOut)}/view/fragment/${activityClass}Fragment.java" />
    </#if>

    <#if frameSupperFragment == "BaseSwipeList"||frameSupperFragment== "BaseSwipe"||frameSupperFragment == "BaseRequest">  
         <instantiate from="root/src/app_package/presenter/BasePresenter.java.ftl"
                       to="${escapeXmlAttribute(srcOut)}/presenter/${activityClass}Pt.java" />   
         <instantiate from="root/src/app_package/bean/BaseBean.java.ftl"
                       to="${escapeXmlAttribute(srcOut)}/bean/${activityClass}Bean.java" />		
    </#if>

</#if>
</recipe>
