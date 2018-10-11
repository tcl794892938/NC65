package nc.ui.ic.onhand.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.vo.ic.onhand.entity.OnhandDlgConst;
import nc.vo.uif2.AppStatusRegisteryCallback;
import nc.vo.uif2.LoginContext;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 
 * @since 创建日期 Mar 27, 2014
 * @author wangweir
 */
public class OnhandQueryAreaModel implements IQueryAreaDataProvider, IQueryAreaSeleted, Serializable {
	
	/**
	 * 控件调用（是批次参照还是存量查拣）
	 */
	private boolean isBatch = false;

    private static final String SELECTED_ZERO_ONHAND_TYPE = "SelectedZeroOnhandType";

    private static final String SELECTED_QUERY_MATCH_TYPE = "SelectedQueryMatchType";

    private static final String SELECTED_QUERY_TYPE = "SelectedQueryType";

    /**
     * 查询结果是否含0结存
     */
    public static final String NONEZERO = "nonezero";

    public static final String ZERO = "zero";

    /* End */

    /**
     * 查询匹配
     */
    public static final String ALL = "all";

    public static final String BYBILL = "bybill";

    /* End */

    /**
     * 查询类别
     */
    public static final String BATCHCODE = "batchcode";

    public static final String ATP = "atp";

    public static final String ONHAND = "onhand";

    /* End */

    /**
     * 默认值
     */
    private String defaultQueryType;

    private String defaultMatchType;

    private String defualtZeroOnhandType;

    /* End */

    private IConstEnum[] queryTypeDatas;

    private IConstEnum[] queryMatchDatas;

    private IConstEnum[] zeroOnhandDatas;

    private IConstEnum selectedQueryType;

    private IConstEnum selectedQueryMatchType;

    private IConstEnum selectedZeroOnhandType;

	// 查询类别Begin
	private IConstEnum onhandType = new DefaultConstEnum(ONHAND,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
					"04008001-0867")/* @res "查询现存量" */);

	private IConstEnum atpType = new DefaultConstEnum(ATP,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
					"04008001-0868")/* @res "查询可用量" */);

	private IConstEnum batchcodeType = new DefaultConstEnum(BATCHCODE,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
					"04008001-0869")/* @res "查询批次档案" */);

	// 查询类别End

	// 查询匹配Begin
	private IConstEnum queryByBill = new DefaultConstEnum(BYBILL,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
					"04008001-0870")/* @res "按单据查询" */);

	private IConstEnum queryAll = new DefaultConstEnum(ALL,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
					"04008001-0871")/* @res "查询所有" */);

	// 查询匹配End

	// 查询结果是否含0结存Begin
	private IConstEnum containZero = new DefaultConstEnum(ZERO,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
					"04008001-0872")/* @res "含0结存" */);

	private IConstEnum nonZero = new DefaultConstEnum(NONEZERO,
			nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4008001_0",
					"04008001-0873")/* @res "不含0结存" */);

    // 查询结果是否含0结存End

    private boolean recordSetting = true;

    private LoginContext context;

    private transient OnhandEventDelegate eventDelegator = new OnhandEventDelegate();

    public OnhandQueryAreaModel() {

    }

    /**
     * 
     */
    public void initModel() {
        initDefaultSelections();
        registeCallbak();
    }

    protected void initDefaultSelections() {
        initDefaultSelectionByValue(defaultQueryType, defaultMatchType, defualtZeroOnhandType);
    }

    /**
     * @param defualtZeroOnhandType
     * @param defaultMatchType
     * @param defaultQueryType
     */
    private void initDefaultSelectionByValue(String defaultQueryType,
                                             String defaultMatchType,
                                             String defualtZeroOnhandType) {
        IConstEnum defaultConstEnum = initOneDefaultConstValue(this.getQueryTypeDatas(), defaultQueryType);
        this.setSelectedQueryType(defaultConstEnum == null ? onhandType : defaultConstEnum);

        defaultConstEnum = initOneDefaultConstValue(this.getQueryMatchDatas(), defaultMatchType);
        this.setSelectedQueryMatchType(defaultConstEnum == null ? queryByBill : defaultConstEnum);

        defaultConstEnum = initOneDefaultConstValue(this.getZeroOnhandDatas(), defualtZeroOnhandType);
        this.setSelectedZeroOnhandType(defaultConstEnum == null ? nonZero : defaultConstEnum);
    }

    /**
     * @param constEnumValues
     * @param defaultEnumValue
     * @return
     */
    private IConstEnum initOneDefaultConstValue(IConstEnum[] constEnumValues,
                                                String defaultEnumValue) {
        if (defaultEnumValue == null) {
            return null;
        }

        for (IConstEnum constEnum : constEnumValues) {
            if (defaultEnumValue.equals(constEnum.getValue())) {
                return constEnum;
            }
        }
        return null;
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public void registeCallbak() {
        if (!this.isRecordSetting()) {
            return;
        }

        if (getContext() == null || getContext().getStatusRegistery() == null) {
            return;
        }

        getContext().getStatusRegistery().addCallback(new AppStatusRegisteryCallback() {

            @Override
            public Object getID() {
                return getRegisteId();
            }

            @Override
            public Object getStatusObject() {
                Map<String, String> defaultStoreObj = new HashMap<String, String>();
                defaultStoreObj.put(SELECTED_QUERY_TYPE, (String) getSelectedQueryType().getValue());
                defaultStoreObj.put(SELECTED_QUERY_MATCH_TYPE, (String) getSelectedQueryMatchType().getValue());
                defaultStoreObj.put(SELECTED_ZERO_ONHAND_TYPE, (String) getSelectedZeroOnhandType().getValue());
                return defaultStoreObj;
            }
        });

        Object statusObj = getContext().getStatusRegistery().getAppStatusObject(getRegisteId());
        if (statusObj == null) {
            return;
        }

        initDefaultSelectionsByLastAppStatus((Map<String, String>) statusObj);
    }

    protected void initDefaultSelectionsByLastAppStatus(Map<String, String> statusObj) {
        this.initDefaultSelectionByValue(
            statusObj.get(SELECTED_QUERY_TYPE),
            statusObj.get(SELECTED_QUERY_MATCH_TYPE),
            statusObj.get(SELECTED_ZERO_ONHAND_TYPE));
    }

    /**
     * @return
     */
    public String getRegisteId() {
        return OnhandQueryAreaModel.class.getName();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.ic.onhand.IQueryAreaDataProvider#getQueryTypeDatas()
     */
    @Override
    public IConstEnum[] getQueryTypeDatas() {
        if (this.queryTypeDatas == null) {
        	if (this.isBatch) {
        		if(getContext().getNodeCode().equals("40060402")||getContext().getNodeCode().equals("40080804")){
        			this.queryTypeDatas = new IConstEnum[]{ onhandType };
        		}else{
        			this.queryTypeDatas = new IConstEnum[]{atpType, onhandType, batchcodeType };
        		}
        	} else {
        		if(getContext().getNodeCode().equals("40060402")){
        			this.queryTypeDatas = new IConstEnum[]{ onhandType };
        		}else{
        			this.queryTypeDatas = new IConstEnum[]{atpType, onhandType };
        		}
        	}
        }
        return queryTypeDatas;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.ic.onhand.IQueryAreaDataProvider#getQueryMatchDatas()
     */
    @Override
    public IConstEnum[] getQueryMatchDatas() {
        if (this.queryMatchDatas == null) {
            this.queryMatchDatas = new IConstEnum[]{queryByBill, queryAll };
        }
        return queryMatchDatas;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.ic.onhand.IQueryAreaDataProvider#getZeroOnhandDatas()
     */
    @Override
    public IConstEnum[] getZeroOnhandDatas() {
        if (this.zeroOnhandDatas == null) {
            this.zeroOnhandDatas = new IConstEnum[]{nonZero, containZero };
        }
        return zeroOnhandDatas;
    }

    /**
     * @param queryTypeDatas
     *        the queryTypeDatas to set
     */
    public void setQueryTypeDatas(IConstEnum[] queryTypeDatas) {
        this.queryTypeDatas = queryTypeDatas;
    }

    /**
     * @param queryMatchDatas
     *        the queryMatchDatas to set
     */
    public void setQueryMatchDatas(IConstEnum[] queryMatchDatas) {
        this.queryMatchDatas = queryMatchDatas;
    }

    /**
     * @param zeroOnhandDatas
     *        the zeroOnhandDatas to set
     */
    public void setZeroOnhandDatas(IConstEnum[] zeroOnhandDatas) {
        this.zeroOnhandDatas = zeroOnhandDatas;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.ic.onhand.model.IQueryAreaSeleted#getSelectedQueryType()
     */
    @Override
    public IConstEnum getSelectedQueryType() {
        return selectedQueryType;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.ic.onhand.model.IQueryAreaSeleted#setSelectedQueryType(nc.ui.pub.beans.constenum.IConstEnum)
     */
    @Override
    public void setSelectedQueryType(IConstEnum selectedQueryType) {
        this.selectedQueryType = selectedQueryType;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.ic.onhand.model.IQueryAreaSeleted#getSelectedQueryMatchType()
     */
    @Override
    public IConstEnum getSelectedQueryMatchType() {
        return selectedQueryMatchType;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.ic.onhand.model.IQueryAreaSeleted#setSelectedQueryMatchType(nc.ui.pub.beans.constenum.IConstEnum)
     */
    @Override
    public void setSelectedQueryMatchType(IConstEnum selectedQueryMatchType) {
        this.selectedQueryMatchType = selectedQueryMatchType;
    }

    /**
     * 
     */
    public void doRefresh() {
        this.fireEvent(new AppEvent(OnhandDlgConst.ONHAND_NEED_REFRESH));
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.ic.onhand.model.IQueryAreaSeleted#getSelectedZeroOnhandType()
     */
    @Override
    public IConstEnum getSelectedZeroOnhandType() {
        return selectedZeroOnhandType;
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.ic.onhand.model.IQueryAreaSeleted#setSelectedZeroOnhandType(nc.ui.pub.beans.constenum.IConstEnum)
     */
    @Override
    public void setSelectedZeroOnhandType(IConstEnum selectedZeroOnhandType) {
        this.selectedZeroOnhandType = selectedZeroOnhandType;
    }

    /**
     * @return the recordSetting
     */
    public boolean isRecordSetting() {
        return recordSetting;
    }

    /**
     * @param recordSetting
     *        the recordSetting to set
     */
    public void setRecordSetting(boolean recordSetting) {
        this.recordSetting = recordSetting;
    }

    /**
     * @return the context
     */
    public LoginContext getContext() {
        return context;
    }

    /**
     * @param context
     *        the context to set
     */
    public void setContext(LoginContext context) {
        this.context = context;
    }

    /**
     * return true if 查询所有
     * 
     * @return true if 查询所有
     */
    public boolean isQueryAll() {
        if (this.selectedQueryMatchType != null) {
            return this.queryAll.equals(this.getSelectedQueryMatchType());
        }
        return false;
    }

    /**
     * return true if 查询现存量
     * 
     * @return true if 查询现存量
     */
    public boolean isQueryOnhandType() {
        if (this.selectedQueryType != null) {
            return this.onhandType.equals(this.getSelectedQueryType());
        }
        return false;
    }

    /**
     * return true if query atp
     * 
     * @return true if query atp
     */
    public boolean isQueryAtpType() {
        if (this.selectedQueryType != null) {
            return this.atpType.equals(this.getSelectedQueryType());
        }
        return false;
    }

    /**
     * return true if query batchcode
     * 
     * @return true if query batchcode
     */
    public boolean isQueryBatchCodeType() {
        if (this.selectedQueryType != null) {
            return this.batchcodeType.equals(this.getSelectedQueryType());
        }
        return false;
    }
    
    /**
     * return true if query batchcode
     * 
     * @return true if query batchcode
     */
    public boolean isQueryZeroOnhand() {
        if (this.selectedZeroOnhandType != null) {
            return this.containZero.equals(this.getSelectedZeroOnhandType());
        }
        return false;
    }

    public void addAppEventListener(AppEventListener l) {
        this.eventDelegator.addAppEventListener(l);
    }

    public void removeAppEventListener(AppEventListener l) {
        this.eventDelegator.removeAppEventListener(l);
    }

    public void addAppEventListener(Class< ? extends AppEvent> eventType,
                                    IAppEventHandler< ? extends AppEvent> l) {
        this.eventDelegator.addAppEventListener(eventType, l);
    }

    public void fireEvent(AppEvent event) {
        this.eventDelegator.fireEvent(event);
    }

    public AppUiState getAppUiState() {
        return this.eventDelegator.getAppUiState();
    }

    public void removeAppEventListener(Class< ? extends AppEvent> eventType,
                                       IAppEventHandler< ? extends AppEvent> l) {
        this.eventDelegator.removeAppEventListener(eventType, l);
    }

    public void setAppUiState(AppUiState appUiState) {
        this.eventDelegator.setAppUiState(appUiState);
    }

    /**
     * @param defaultQueryType
     *        the defaultQueryType to set
     */
    public void setDefaultQueryType(String defaultQueryType) {
        this.defaultQueryType = defaultQueryType;
    }

    /**
     * @param defaultMatchType
     *        the defaultMatchType to set
     */
    public void setDefaultMatchType(String defaultMatchType) {
        this.defaultMatchType = defaultMatchType;
    }

    /**
     * @param defualtZeroOnhandType
     *        the defualtZeroOnhandType to set
     */
    public void setDefualtZeroOnhandType(String defualtZeroOnhandType) {
        this.defualtZeroOnhandType = defualtZeroOnhandType;
    }

	public boolean isBatch() {
		return isBatch;
	}

	public void setIsBatch(boolean isBatch) {
		this.isBatch = isBatch;
	}
}
