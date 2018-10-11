package nc.ui.ic.m46;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.ui.uif2.factory.AbstractJavaBeanDefinition;

public class ProductIn_org extends nc.ui.ic.general.model.config.GeneralCommon {
	private Map<String, Object> context = new HashMap();

	public nc.ui.uif2.userdefitem.QueryParam getQueryParams1() {
		if (context.get("queryParams1") != null)
			return (nc.ui.uif2.userdefitem.QueryParam) context
					.get("queryParams1");
		nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
		context.put("queryParams1", bean);
		bean.setMdfullname("ic.FinProdInHeadVO");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.userdefitem.QueryParam getQueryParams2() {
		if (context.get("queryParams2") != null)
			return (nc.ui.uif2.userdefitem.QueryParam) context
					.get("queryParams2");
		nc.ui.uif2.userdefitem.QueryParam bean = new nc.ui.uif2.userdefitem.QueryParam();
		context.put("queryParams2", bean);
		bean.setMdfullname("ic.FinProdInBodyVO");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.editor.UserdefQueryParam getUserQueryParams1() {
		if (context.get("userQueryParams1") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("userQueryParams1");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("userQueryParams1", bean);
		bean.setMdfullname("ic.FinProdInHeadVO");
		bean.setPos(0);
		bean.setPrefix("vdef");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.editor.UserdefQueryParam getUserQueryParams2() {
		if (context.get("userQueryParams2") != null)
			return (nc.ui.uif2.editor.UserdefQueryParam) context
					.get("userQueryParams2");
		nc.ui.uif2.editor.UserdefQueryParam bean = new nc.ui.uif2.editor.UserdefQueryParam();
		context.put("userQueryParams2", bean);
		bean.setMdfullname("ic.FinProdInBodyVO");
		bean.setPos(1);
		bean.setPrefix("vbdef");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.general.model.ICGenRevisePageService getPageQuery() {
		if (context.get("pageQuery") != null)
			return (nc.ui.ic.general.model.ICGenRevisePageService) context
					.get("pageQuery");
		nc.ui.ic.general.model.ICGenRevisePageService bean = new nc.ui.ic.general.model.ICGenRevisePageService();
		context.put("pageQuery", bean);
		bean.setVoClassName("nc.vo.ic.m46.entity.FinProdInVO");
		bean.setBillType("46");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m46.model.ProductInModelService getManageModelService() {
		if (context.get("manageModelService") != null)
			return (nc.ui.ic.m46.model.ProductInModelService) context
					.get("manageModelService");
		nc.ui.ic.m46.model.ProductInModelService bean = new nc.ui.ic.m46.model.ProductInModelService();
		context.put("manageModelService", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.general.model.ICGenBizModel getIcBizModel() {
		if (context.get("icBizModel") != null)
			return (nc.ui.ic.general.model.ICGenBizModel) context
					.get("icBizModel");
		nc.ui.ic.general.model.ICGenBizModel bean = new nc.ui.ic.general.model.ICGenBizModel();
		context.put("icBizModel", bean);
		bean.setService(getManageModelService());
		bean.setBusinessObjectAdapterFactory((nc.vo.bd.meta.IBDObjectAdapterFactory) findBeanInUIF2BeanFactory("boadatorfactory"));
		bean.setIcUIContext((nc.ui.ic.pub.env.ICUIContext) findBeanInUIF2BeanFactory("icUIContext"));
		bean.setBillType("46");
		bean.setPowerValidate(true);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.TangramContainer getContainer() {
		if (context.get("container") != null)
			return (nc.ui.uif2.TangramContainer) context.get("container");
		nc.ui.uif2.TangramContainer bean = new nc.ui.uif2.TangramContainer();
		context.put("container", bean);
		bean.setTangramLayoutRoot((nc.ui.uif2.tangramlayout.node.TangramLayoutNode) findBeanInUIF2BeanFactory("vsnodequery"));
		bean.setModel(getIcBizModel());
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m46.deal.ProductInDLGWrapper getQryDLGInitializer() {
		if (context.get("qryDLGInitializer") != null)
			return (nc.ui.ic.m46.deal.ProductInDLGWrapper) context
					.get("qryDLGInitializer");
		nc.ui.ic.m46.deal.ProductInDLGWrapper bean = new nc.ui.ic.m46.deal.ProductInDLGWrapper();
		context.put("qryDLGInitializer", bean);
		bean.setModel(getIcBizModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m46.deal.ProductInProcessorInfo getUIProcesorInfo() {
		if (context.get("UIProcesorInfo") != null)
			return (nc.ui.ic.m46.deal.ProductInProcessorInfo) context
					.get("UIProcesorInfo");
		nc.ui.ic.m46.deal.ProductInProcessorInfo bean = new nc.ui.ic.m46.deal.ProductInProcessorInfo();
		context.put("UIProcesorInfo", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.actions.ActionContributors getToftpanelActionContributors() {
		if (context.get("toftpanelActionContributors") != null)
			return (nc.ui.uif2.actions.ActionContributors) context
					.get("toftpanelActionContributors");
		nc.ui.uif2.actions.ActionContributors bean = new nc.ui.uif2.actions.ActionContributors();
		context.put("toftpanelActionContributors", bean);
		bean.setContributors(getManagedList0());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList0() {
		List list = new ArrayList();
		list.add(getActionsOfList());
		list.add(getActionsOfCard());
		return list;
	}

	public nc.ui.ic.m46.action.FetchAutoActionFor46 getFetchAutoAction() {
		if (context.get("fetchAutoAction") != null)
			return (nc.ui.ic.m46.action.FetchAutoActionFor46) context
					.get("fetchAutoAction");
		nc.ui.ic.m46.action.FetchAutoActionFor46 bean = new nc.ui.ic.m46.action.FetchAutoActionFor46();
		context.put("fetchAutoAction", bean);
		bean.setEditorModel((nc.ui.ic.general.model.ICGenBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setInterceptor((nc.ui.uif2.actions.ActionInterceptor) findBeanInUIF2BeanFactory("cacheLockInterceptor"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.MenuAction getLinkQryBrowseGroupAction() {
		if (context.get("linkQryBrowseGroupAction") != null)
			return (nc.funcnode.ui.action.MenuAction) context
					.get("linkQryBrowseGroupAction");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("linkQryBrowseGroupAction", bean);
		bean.setCode("linkQryAction");
		bean.setName(getI18nFB_d7587a());
		bean.setActions(getManagedList1());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_d7587a() {
		if (context.get("nc.ui.uif2.I18nFB#d7587a") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#d7587a");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#d7587a", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0742");
		bean.setDefaultValue("联查");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#d7587a", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList1() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("linkQryAction"));
		list.add(getSeparatorAction_1f1b204());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("setPieceAtion"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("displayOrhideAction"));
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1f1b204() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1f1b204") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1f1b204");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1f1b204", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.funcnode.ui.action.MenuAction getLinkQryEditGroupAction() {
		if (context.get("linkQryEditGroupAction") != null)
			return (nc.funcnode.ui.action.MenuAction) context
					.get("linkQryEditGroupAction");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("linkQryEditGroupAction", bean);
		bean.setCode("linkQryAction");
		bean.setName(getI18nFB_de4254());
		bean.setActions(getManagedList2());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_de4254() {
		if (context.get("nc.ui.uif2.I18nFB#de4254") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#de4254");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#de4254", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0742");
		bean.setDefaultValue("联查");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#de4254", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList2() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("setPieceAtion"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("displayOrhideAction"));
		return list;
	}

	public nc.funcnode.ui.action.MenuAction getAssistantFunctionBrowseAction_IN() {
		if (context.get("assistantFunctionBrowseAction_IN") != null)
			return (nc.funcnode.ui.action.MenuAction) context
					.get("assistantFunctionBrowseAction_IN");
		nc.funcnode.ui.action.MenuAction bean = new nc.funcnode.ui.action.MenuAction();
		context.put("assistantFunctionBrowseAction_IN", bean);
		bean.setCode("NastMngBrowseAction");
		bean.setName(getI18nFB_1d4aec8());
		bean.setActions(getManagedList3());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private java.lang.String getI18nFB_1d4aec8() {
		if (context.get("nc.ui.uif2.I18nFB#1d4aec8") != null)
			return (java.lang.String) context.get("nc.ui.uif2.I18nFB#1d4aec8");
		nc.ui.uif2.I18nFB bean = new nc.ui.uif2.I18nFB();
		context.put("&nc.ui.uif2.I18nFB#1d4aec8", bean);
		bean.setResDir("4008001_0");
		bean.setResId("04008001-0741");
		bean.setDefaultValue("辅助功能");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		try {
			Object product = bean.getObject();
			context.put("nc.ui.uif2.I18nFB#1d4aec8", product);
			return (java.lang.String) product;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List getManagedList3() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("attachMentMngAction"));
		return list;
	}

	public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfList() {
		if (context.get("actionsOfList") != null)
			return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer) context
					.get("actionsOfList");
		nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(
				(nc.ui.uif2.components.ITabbedPaneAwareComponent) findBeanInUIF2BeanFactory("list"));
		context.put("actionsOfList", bean);
		bean.setActions(getManagedList4());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList4() {
		List list = new ArrayList();
		list.add(getAddMenuGroup());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("editAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("deleteAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("copyAction"));
		list.add(getSeparatorAction_1f00b2());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("queryAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("refreshAction"));
		list.add(getSeparatorAction_166de99());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("maintainMenu"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("signActionMenu"));
		list.add(getAssistantFunctionBrowseAction_IN());
		list.add(getSeparatorAction_a02a32());
		list.add(getLinkQryBrowseGroupAction());
		list.add(getSeparatorAction_132746f());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("importExportAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("printMngAction"));
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1f00b2() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1f00b2") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1f00b2");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1f00b2", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_166de99() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#166de99") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#166de99");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#166de99", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_a02a32() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#a02a32") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#a02a32");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#a02a32", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_132746f() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#132746f") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#132746f");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#132746f", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.uif2.actions.StandAloneToftPanelActionContainer getActionsOfCard() {
		if (context.get("actionsOfCard") != null)
			return (nc.ui.uif2.actions.StandAloneToftPanelActionContainer) context
					.get("actionsOfCard");
		nc.ui.uif2.actions.StandAloneToftPanelActionContainer bean = new nc.ui.uif2.actions.StandAloneToftPanelActionContainer(
				(nc.ui.uif2.components.ITabbedPaneAwareComponent) findBeanInUIF2BeanFactory("card"));
		context.put("actionsOfCard", bean);
		bean.setModel(getIcBizModel());
		bean.setActions(getManagedList5());
		bean.setEditActions(getManagedList6());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList5() {
		List list = new ArrayList();
		list.add(getAddMenuGroup());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("editAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("deleteAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("copyAction"));
		list.add(getSeparatorAction_17a43d3());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("queryAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("refreshCardAction"));
		list.add(getSeparatorAction_1fad681());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("maintainMenu"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("signActionMenu"));
		list.add(getAssistantFunctionBrowseAction_IN());
		list.add(getSeparatorAction_118cce4());
		list.add(getLinkQryBrowseGroupAction());
		list.add(getSeparatorAction_1338e52());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("importExportAction"));
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("printMngAction"));
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_17a43d3() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#17a43d3") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#17a43d3");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#17a43d3", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1fad681() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1fad681") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1fad681");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1fad681", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_118cce4() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#118cce4") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#118cce4");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#118cce4", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1338e52() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1338e52") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1338e52");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1338e52", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList6() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("saveAction"));
		list.add(getSeparatorAction_1c4d324());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("cancelAction"));
		list.add(getSeparatorAction_101e892());
		list.add(getFetchAutoAction());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("maintainMenu"));
		list.add(getSeparatorAction_4beb9c());
		list.add(getLinkQryEditGroupAction());
		list.add(getSeparatorAction_596890());
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("importExportAction"));
		return list;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_1c4d324() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#1c4d324") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#1c4d324");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#1c4d324", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_101e892() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#101e892") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#101e892");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#101e892", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_4beb9c() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#4beb9c") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#4beb9c");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#4beb9c", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.funcnode.ui.action.SeparatorAction getSeparatorAction_596890() {
		if (context.get("nc.funcnode.ui.action.SeparatorAction#596890") != null)
			return (nc.funcnode.ui.action.SeparatorAction) context
					.get("nc.funcnode.ui.action.SeparatorAction#596890");
		nc.funcnode.ui.action.SeparatorAction bean = new nc.funcnode.ui.action.SeparatorAction();
		context.put("nc.funcnode.ui.action.SeparatorAction#596890", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.actions.AddMenuAction getAddMenuGroup() {
		if (context.get("addMenuGroup") != null)
			return (nc.ui.pubapp.uif2app.actions.AddMenuAction) context
					.get("addMenuGroup");
		nc.ui.pubapp.uif2app.actions.AddMenuAction bean = new nc.ui.pubapp.uif2app.actions.AddMenuAction();
		context.put("addMenuGroup", bean);
		bean.setBillType("46");
		bean.setActions(getManagedList7());
		bean.setModel(getIcBizModel());
		bean.setPfAddInfoLoader(getPfAddInfoLoader());
		bean.initUI();
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList7() {
		List list = new ArrayList();
		list.add((javax.swing.Action) findBeanInUIF2BeanFactory("addAction"));
		list.add(getRefAction());
		return list;
	}

	public nc.ui.ic.m46.billref.RefAction getRefAction() {
		if (context.get("refAction") != null)
			return (nc.ui.ic.m46.billref.RefAction) context.get("refAction");
		nc.ui.ic.m46.billref.RefAction bean = new nc.ui.ic.m46.billref.RefAction();
		context.put("refAction", bean);
		bean.setSourceBillType("C003");
		bean.setSourceBillName("参照质检报告");
		bean.setFlowBillType(false);
		bean.setModel(getIcBizModel());
		bean.setEditor((nc.ui.pubapp.uif2app.view.BillForm) findBeanInUIF2BeanFactory("card"));
		bean.setTransferViewProcessor(getTransferProcessorforZJSQ());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.abspanel.action.TransferViewProcessor getTransferProcessorforZJSQ() {
		if (context.get("transferProcessorforZJSQ") != null)
			return (nc.ui.abspanel.action.TransferViewProcessor) context
					.get("transferProcessorforZJSQ");
		nc.ui.abspanel.action.TransferViewProcessor bean = new nc.ui.abspanel.action.TransferViewProcessor();
		context.put("transferProcessorforZJSQ", bean);
		bean.setList((nc.ui.pubapp.uif2app.view.ShowUpableBillListView) findBeanInUIF2BeanFactory("list"));
		bean.setActionContainer(getContainer());
		bean.setCardActionContainer(getActionsOfCard());
		bean.setListActionContainer(getActionsOfList());
		bean.setTransferLogic(getTransferLogicforZJSQ());
		bean.setBillForm((nc.ui.pubapp.uif2app.view.ShowUpableBillForm) findBeanInUIF2BeanFactory("card"));
		bean.setCancelAction((nc.ui.uif2.NCAction) findBeanInUIF2BeanFactory("cancelAction"));
		bean.setSaveAction((nc.ui.uif2.NCAction) findBeanInUIF2BeanFactory("saveAction"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m46.billref.XMSBFromZJSQTRansferBillDataLogic getTransferLogicforZJSQ() {
		if (context.get("transferLogicforZJSQ") != null)
			return (nc.ui.ic.m46.billref.XMSBFromZJSQTRansferBillDataLogic) context
					.get("transferLogicforZJSQ");
		nc.ui.ic.m46.billref.XMSBFromZJSQTRansferBillDataLogic bean = new nc.ui.ic.m46.billref.XMSBFromZJSQTRansferBillDataLogic();
		context.put("transferLogicforZJSQ", bean);
		bean.setBillForm((nc.ui.pubapp.uif2app.view.PubShowUpableBillForm) findBeanInUIF2BeanFactory("card"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener getInitDataListener() {
		if (context.get("InitDataListener") != null)
			return (nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener) context
					.get("InitDataListener");
		nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener bean = new nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener();
		context.put("InitDataListener", bean);
		bean.setModel(getIcBizModel());
		bean.setVoClassName("nc.vo.ic.m46.entity.FinProdInVO");
		bean.setAutoShowUpComponent((nc.ui.uif2.components.IAutoShowUpComponent) findBeanInUIF2BeanFactory("card"));
		bean.setQueryAction((nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction) findBeanInUIF2BeanFactory("queryAction"));
		bean.setProcessorMap(getManagedMap0());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap0() {
		Map map = new HashMap();
		map.put("40", getNtbInitProcessor_1dfd166());
		return map;
	}

	private nc.ui.ic.general.view.NtbInitProcessor getNtbInitProcessor_1dfd166() {
		if (context.get("nc.ui.ic.general.view.NtbInitProcessor#1dfd166") != null)
			return (nc.ui.ic.general.view.NtbInitProcessor) context
					.get("nc.ui.ic.general.view.NtbInitProcessor#1dfd166");
		nc.ui.ic.general.view.NtbInitProcessor bean = new nc.ui.ic.general.view.NtbInitProcessor();
		context.put("nc.ui.ic.general.view.NtbInitProcessor#1dfd166", bean);
		bean.setModel(getIcBizModel());
		bean.setQueryArea((nc.ui.uif2.actions.QueryAreaShell) findBeanInUIF2BeanFactory("queryArea"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m46.handler.Pk_orgHandlerFor46 getPk_orgHandler() {
		if (context.get("pk_orgHandler") != null)
			return (nc.ui.ic.m46.handler.Pk_orgHandlerFor46) context
					.get("pk_orgHandler");
		nc.ui.ic.m46.handler.Pk_orgHandlerFor46 bean = new nc.ui.ic.m46.handler.Pk_orgHandlerFor46();
		context.put("pk_orgHandler", bean);
		bean.setEditorModel((nc.ui.ic.pub.model.ICBizEditorModel) findBeanInUIF2BeanFactory("icBizEditorModel"));
		bean.setContext((nc.ui.ic.pub.env.ICUIContext) findBeanInUIF2BeanFactory("icUIContext"));
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m46.handler.MainNumHandlerFor46 getMainNumHandler() {
		if (context.get("mainNumHandler") != null)
			return (nc.ui.ic.m46.handler.MainNumHandlerFor46) context
					.get("mainNumHandler");
		nc.ui.ic.m46.handler.MainNumHandlerFor46 bean = new nc.ui.ic.m46.handler.MainNumHandlerFor46();
		context.put("mainNumHandler", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m46.handler.NumHandlerFor46 getNumHandler() {
		if (context.get("numHandler") != null)
			return (nc.ui.ic.m46.handler.NumHandlerFor46) context
					.get("numHandler");
		nc.ui.ic.m46.handler.NumHandlerFor46 bean = new nc.ui.ic.m46.handler.NumHandlerFor46();
		context.put("numHandler", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m46.handler.VtrantypecodeHandlerFor46 getVtrantypecodeHandler() {
		if (context.get("vtrantypecodeHandler") != null)
			return (nc.ui.ic.m46.handler.VtrantypecodeHandlerFor46) context
					.get("vtrantypecodeHandler");
		nc.ui.ic.m46.handler.VtrantypecodeHandlerFor46 bean = new nc.ui.ic.m46.handler.VtrantypecodeHandlerFor46();
		context.put("vtrantypecodeHandler", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m46.handler.CmffileidHandlerFor46 getCmffileidHandler() {
		if (context.get("cmffileidHandler") != null)
			return (nc.ui.ic.m46.handler.CmffileidHandlerFor46) context
					.get("cmffileidHandler");
		nc.ui.ic.m46.handler.CmffileidHandlerFor46 bean = new nc.ui.ic.m46.handler.CmffileidHandlerFor46();
		context.put("cmffileidHandler", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.pub.handler.card.ICCardEditEventHandlerMap getChildCardEditHandlerMap() {
		if (context.get("childCardEditHandlerMap") != null)
			return (nc.ui.ic.pub.handler.card.ICCardEditEventHandlerMap) context
					.get("childCardEditHandlerMap");
		nc.ui.ic.pub.handler.card.ICCardEditEventHandlerMap bean = new nc.ui.ic.pub.handler.card.ICCardEditEventHandlerMap();
		context.put("childCardEditHandlerMap", bean);
		bean.setHandlerMap(getManagedMap1());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private Map getManagedMap1() {
		Map map = new HashMap();
		map.put("cmffileid", getCmffileidHandler());
		map.put("cprocalbodyvid", getCprocalbodyvidHandler_12ca467());
		map.put("cprocalbodyoid", getCprocalbodyoidHandler_1dd827d());
		map.put("cprowarehouseid", getCprowarehouseidHandler_1a2078e());
		map.put("cworkcenterid", getCworkcenteridHandler_9e36f1());
		map.put("cwarehouseid", getCwarehouseidHandlerFor46_12911c3());
		map.put("cdptvid", getCdptvidHandler_17755ed());
		map.put("ccostcenterid", getCcostcenteridHandler_15eded4());
		map.put("cproductid", getCproductidHandler_1aa75d4());
		map.put("fproductclass", getFproductclassHandler_1dc189());
		return map;
	}

	private nc.ui.ic.m46.handler.CprocalbodyvidHandler getCprocalbodyvidHandler_12ca467() {
		if (context.get("nc.ui.ic.m46.handler.CprocalbodyvidHandler#12ca467") != null)
			return (nc.ui.ic.m46.handler.CprocalbodyvidHandler) context
					.get("nc.ui.ic.m46.handler.CprocalbodyvidHandler#12ca467");
		nc.ui.ic.m46.handler.CprocalbodyvidHandler bean = new nc.ui.ic.m46.handler.CprocalbodyvidHandler();
		context.put("nc.ui.ic.m46.handler.CprocalbodyvidHandler#12ca467", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m46.handler.CprocalbodyoidHandler getCprocalbodyoidHandler_1dd827d() {
		if (context.get("nc.ui.ic.m46.handler.CprocalbodyoidHandler#1dd827d") != null)
			return (nc.ui.ic.m46.handler.CprocalbodyoidHandler) context
					.get("nc.ui.ic.m46.handler.CprocalbodyoidHandler#1dd827d");
		nc.ui.ic.m46.handler.CprocalbodyoidHandler bean = new nc.ui.ic.m46.handler.CprocalbodyoidHandler();
		context.put("nc.ui.ic.m46.handler.CprocalbodyoidHandler#1dd827d", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m46.handler.CprowarehouseidHandler getCprowarehouseidHandler_1a2078e() {
		if (context.get("nc.ui.ic.m46.handler.CprowarehouseidHandler#1a2078e") != null)
			return (nc.ui.ic.m46.handler.CprowarehouseidHandler) context
					.get("nc.ui.ic.m46.handler.CprowarehouseidHandler#1a2078e");
		nc.ui.ic.m46.handler.CprowarehouseidHandler bean = new nc.ui.ic.m46.handler.CprowarehouseidHandler();
		context.put("nc.ui.ic.m46.handler.CprowarehouseidHandler#1a2078e", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m46.handler.CworkcenteridHandler getCworkcenteridHandler_9e36f1() {
		if (context.get("nc.ui.ic.m46.handler.CworkcenteridHandler#9e36f1") != null)
			return (nc.ui.ic.m46.handler.CworkcenteridHandler) context
					.get("nc.ui.ic.m46.handler.CworkcenteridHandler#9e36f1");
		nc.ui.ic.m46.handler.CworkcenteridHandler bean = new nc.ui.ic.m46.handler.CworkcenteridHandler();
		context.put("nc.ui.ic.m46.handler.CworkcenteridHandler#9e36f1", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m46.handler.CwarehouseidHandlerFor46 getCwarehouseidHandlerFor46_12911c3() {
		if (context
				.get("nc.ui.ic.m46.handler.CwarehouseidHandlerFor46#12911c3") != null)
			return (nc.ui.ic.m46.handler.CwarehouseidHandlerFor46) context
					.get("nc.ui.ic.m46.handler.CwarehouseidHandlerFor46#12911c3");
		nc.ui.ic.m46.handler.CwarehouseidHandlerFor46 bean = new nc.ui.ic.m46.handler.CwarehouseidHandlerFor46();
		context.put("nc.ui.ic.m46.handler.CwarehouseidHandlerFor46#12911c3",
				bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m46.handler.CdptvidHandler getCdptvidHandler_17755ed() {
		if (context.get("nc.ui.ic.m46.handler.CdptvidHandler#17755ed") != null)
			return (nc.ui.ic.m46.handler.CdptvidHandler) context
					.get("nc.ui.ic.m46.handler.CdptvidHandler#17755ed");
		nc.ui.ic.m46.handler.CdptvidHandler bean = new nc.ui.ic.m46.handler.CdptvidHandler();
		context.put("nc.ui.ic.m46.handler.CdptvidHandler#17755ed", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m46.handler.CcostcenteridHandler getCcostcenteridHandler_15eded4() {
		if (context.get("nc.ui.ic.m46.handler.CcostcenteridHandler#15eded4") != null)
			return (nc.ui.ic.m46.handler.CcostcenteridHandler) context
					.get("nc.ui.ic.m46.handler.CcostcenteridHandler#15eded4");
		nc.ui.ic.m46.handler.CcostcenteridHandler bean = new nc.ui.ic.m46.handler.CcostcenteridHandler();
		context.put("nc.ui.ic.m46.handler.CcostcenteridHandler#15eded4", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m46.handler.CproductidHandler getCproductidHandler_1aa75d4() {
		if (context.get("nc.ui.ic.m46.handler.CproductidHandler#1aa75d4") != null)
			return (nc.ui.ic.m46.handler.CproductidHandler) context
					.get("nc.ui.ic.m46.handler.CproductidHandler#1aa75d4");
		nc.ui.ic.m46.handler.CproductidHandler bean = new nc.ui.ic.m46.handler.CproductidHandler();
		context.put("nc.ui.ic.m46.handler.CproductidHandler#1aa75d4", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private nc.ui.ic.m46.handler.FproductclassHandler getFproductclassHandler_1dc189() {
		if (context.get("nc.ui.ic.m46.handler.FproductclassHandler#1dc189") != null)
			return (nc.ui.ic.m46.handler.FproductclassHandler) context
					.get("nc.ui.ic.m46.handler.FproductclassHandler#1dc189");
		nc.ui.ic.m46.handler.FproductclassHandler bean = new nc.ui.ic.m46.handler.FproductclassHandler();
		context.put("nc.ui.ic.m46.handler.FproductclassHandler#1dc189", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m46.handler.CbizidHandlerFor46 getCbizidHandler() {
		if (context.get("cbizidHandler") != null)
			return (nc.ui.ic.m46.handler.CbizidHandlerFor46) context
					.get("cbizidHandler");
		nc.ui.ic.m46.handler.CbizidHandlerFor46 bean = new nc.ui.ic.m46.handler.CbizidHandlerFor46();
		context.put("cbizidHandler", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.ic.m46.handler.CMaterialHandlerFor46 getCmaterialvidHandler() {
		if (context.get("cmaterialvidHandler") != null)
			return (nc.ui.ic.m46.handler.CMaterialHandlerFor46) context
					.get("cmaterialvidHandler");
		nc.ui.ic.m46.handler.CMaterialHandlerFor46 bean = new nc.ui.ic.m46.handler.CMaterialHandlerFor46();
		context.put("cmaterialvidHandler", bean);
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.actions.PfAddInfoLoader getPfAddInfoLoader() {
		if (context.get("pfAddInfoLoader") != null)
			return (nc.ui.pubapp.uif2app.actions.PfAddInfoLoader) context
					.get("pfAddInfoLoader");
		nc.ui.pubapp.uif2app.actions.PfAddInfoLoader bean = new nc.ui.pubapp.uif2app.actions.PfAddInfoLoader();
		context.put("pfAddInfoLoader", bean);
		bean.setBillType("46");
		bean.setModel(getIcBizModel());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	public nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare getUserdefAndMarAsstCardPreparator() {
		if (context.get("userdefAndMarAsstCardPreparator") != null)
			return (nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare) context
					.get("userdefAndMarAsstCardPreparator");
		nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillDataPrepare();
		context.put("userdefAndMarAsstCardPreparator", bean);
		bean.setBillDataPrepares(getManagedList8());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList8() {
		List list = new ArrayList();
		list.add((nc.ui.pub.bill.IBillData) findBeanInUIF2BeanFactory("userdefitemPreparator"));
		list.add(getMarProdAsstPreparator());
		list.add((nc.ui.pub.bill.IBillData) findBeanInUIF2BeanFactory("marAsstPreparator"));
		return list;
	}

	public nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare getUserdefAndMarAsstListPreparator() {
		if (context.get("userdefAndMarAsstListPreparator") != null)
			return (nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare) context
					.get("userdefAndMarAsstListPreparator");
		nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare bean = new nc.ui.pubapp.uif2app.view.CompositeBillListDataPrepare();
		context.put("userdefAndMarAsstListPreparator", bean);
		bean.setBillListDataPrepares(getManagedList9());
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

	private List getManagedList9() {
		List list = new ArrayList();
		list.add((nc.ui.pub.bill.IBillListData) findBeanInUIF2BeanFactory("userdefitemlistPreparator"));
		list.add(getMarProdAsstPreparator());
		list.add((nc.ui.pub.bill.IBillListData) findBeanInUIF2BeanFactory("marAsstPreparator"));
		return list;
	}

	public nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator getMarProdAsstPreparator() {
		if (context.get("marProdAsstPreparator") != null)
			return (nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator) context
					.get("marProdAsstPreparator");
		nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator bean = new nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator();
		context.put("marProdAsstPreparator", bean);
		bean.setModel(getIcBizModel());
		bean.setContainer((nc.ui.uif2.userdefitem.UserDefItemContainer) findBeanInUIF2BeanFactory("userdefitemContainer"));
		bean.setPrefix("vprodfree");
		bean.setMaterialField("cproductid");
		bean.setProjectField("cprodprojectid");
		bean.setSupplierField("cprodvendorid");
		bean.setProductorField("cprodproductorid");
		bean.setCustomerField("cprodasscustid");
		setBeanFacotryIfBeanFacatoryAware(bean);
		invokeInitializingBean(bean);
		return bean;
	}

}
