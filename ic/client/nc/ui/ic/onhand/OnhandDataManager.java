package nc.ui.ic.onhand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.pubitf.ic.atp.IAtpQuery;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.ui.ic.onhand.model.OnhandCompositeModel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelHeadRowStateChangeEventListener;
import nc.ui.pub.bill.IBillModelRowStateChangeEventListener;
import nc.ui.pub.bill.RowStateChangeEvent;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.editor.BillListView;
import nc.ui.uif2.editor.IBillListPanelView;
import nc.ui.uif2.model.IMultiRowSelectModel;
import nc.vo.ic.atp.pub.AtpQryCond;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandDlgConst;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.onhand.pub.OnhandQryCond;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

import org.apache.commons.lang.StringUtils;

/**
 * <b> 简要描述功能 </b>
 * <p>
 * </p>
 * 
 * @since 创建日期 Apr 14, 2014
 * @author wangweir
 */
public class OnhandDataManager implements AppEventListener {
	
	private IBillModelRowStateChangeEventListener multiModeListener = null;

	private OnhandCompositeModel onhandCompositeModel;
	
	private IBillListPanelView billListView;
	
	private String pk_cust;
	
	private Set<String> hiddenFields = new HashSet<String>();

	// 多组织处理
	private String[] pk_orgs;

	public void refresh() {
		if (this.isOnhandDimVOValid(this.getOnhandDimVO())) {
			return;
		}

		this.hiddenBatchField();
		// 执行查询
		this.doQuery(this.getOnhandDimVO());
	}

	public void queryOnhand(OnhandDimVO onhandDimVO) {
		try {
		if (isOnhandDimVOValid(onhandDimVO)) {
			return;
		}
		
		pk_cust=onhandDimVO.getCasscustid();

		// 按物料属性处理维度
		if (onhandDimVO.getCmaterialvid() != null) {
		  onhandDimVO = this.getRealOnhandDim(onhandDimVO);
		}

		// 备份OnhandDimVO
		this.setOnhandDimVO(onhandDimVO);

		this.hiddenBatchField();
		// 执行查询
		this.doQuery(onhandDimVO);
		} catch (Exception e) {
			getOnhandCompositeModel().getModel().initModel(null);
//			MessageDialog.showErrorDlg(getOnhandCompositeModel()
//					.getModel().getContext().getEntranceUI(), null, e.getMessage());
			Logger.error(e.getMessage(), e);
		}
	}

  private void hiddenBatchField() {
    if (onhandCompositeModel.getOnhandQueryAreaModel().isQueryBatchCodeType()) {
		  BillItem[] items = this.getBillListView().getBillListPanel().getBillListData().getHeadShowItems();
		  for (BillItem billItem : items) {
        String key = billItem.getKey();
		    if (!key.equals(OnhandDimVO.VBATCHCODE) && !key.startsWith(OnhandDimVO.PK_BATCHCODE)) {
		      hiddenFields.add(key);
		      this.getBillListView().getBillListPanel().hideHeadTableCol(key);
        }
      }
		} else {
		  if (!ValueCheckUtil.isNullORZeroLength(hiddenFields)) {
		    for (String field : hiddenFields) {
		      this.getBillListView().getBillListPanel().showHeadTableCol(field);
        }
		  }
		}
  }

	protected void doQuery(final OnhandDimVO onhandDimVO) {//by cwf
		try {
			OnhandVO[] onhandVOs = this.doQueryInner(onhandDimVO);
			Boolean b=onhandCompositeModel.getOnhandQueryAreaModel().isBatch()
					&&onhandCompositeModel.getModel().getContext().getNodeCode().equals("40080804");
			if(onhandCompositeModel.getModel().getContext().getNodeCode().equals("40060402")||b){
				/*billListView.getBillListPanel().getHeadItem("nnum2").setShow(true);
				billListView.getBillListPanel().getHeadItem("nnum1").setShow(true);
				billListView.getBillListPanel().getHeadItem("nrsnum").setShow(true);
				billListView.getBillListPanel().getHeadItem("custnrsnum").setShow(true);*/
				
				billListView.getBillListPanel().showHeadTableCol("nnum2");
				billListView.getBillListPanel().showHeadTableCol("nnum1");
				billListView.getBillListPanel().showHeadTableCol("nrsnum");
				billListView.getBillListPanel().showHeadTableCol("custnrsnum");
				
				if(onhandCompositeModel.getModel().getContext().getNodeCode().equals("40060402")){
					
					billListView.getBillListPanel().showHeadTableCol("jybz");
					billListView.getBillListPanel().showHeadTableCol("01004");
					billListView.getBillListPanel().showHeadTableCol("01001");
					billListView.getBillListPanel().showHeadTableCol("01002");
					billListView.getBillListPanel().showHeadTableCol("01003");
					billListView.getBillListPanel().showHeadTableCol("01005");
					billListView.getBillListPanel().showHeadTableCol("02014");
					
					/*billListView.getBillListPanel().getHeadItem("jybz").setShow(true);
					billListView.getBillListPanel().getHeadItem("01004").setShow(true);
					billListView.getBillListPanel().getHeadItem("01001").setShow(true);
					billListView.getBillListPanel().getHeadItem("01002").setShow(true);
					billListView.getBillListPanel().getHeadItem("01003").setShow(true);
					billListView.getBillListPanel().getHeadItem("01005").setShow(true);
					billListView.getBillListPanel().getHeadItem("02014").setShow(true);*/
				}
				
				
			/*	billListView.getBillListPanel().resetHeadTableColumnOrder();
				
				multiModeListener = new BillListMultiModelListener();
				// 可通过复选项多选
				billListView.getBillListPanel().setMultiSelect(true);
				// 和单据模板同步事件：复选框选择事件
				billListView.getBillListPanel().getHeadBillModel().addRowStateChangeEventListener(multiModeListener);
				// 由表体选中引起表头选中同步BillManageModel
				billListView.getBillListPanel().getHeadBillModel()
						.addHeadRowStateChangeEventListener(new BillListHeadSynListener());*/
				
				/*billListView.getBillListPanel().getParentListPanel().getTable().setRowSelectionAllowed(true);
				billListView.getBillListPanel().getParentListPanel().getTable().setColumnSelectionAllowed(false);
				billListView.getBillListPanel().getParentListPanel().getTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);*/
				
				
			if(onhandCompositeModel.getModel().getContext().getNodeCode().equals("40060402")){//发货单相同维度合并
					
				Map<String, OnhandVO> map=new HashMap<String, OnhandVO>();
				for(OnhandVO vo:onhandVOs){
					String s=vo.getPk_org()+vo.getCwarehouseid()+vo.getCmaterialoid()+vo.getPk_batchcode();
					if(!map.keySet().contains(s)){
						map.put(s, vo);
					}else{
						OnhandVO hvo=map.get(s);
						hvo.setNonhandastnum(hvo.getNonhandastnum().add(vo.getNonhandastnum()));
						hvo.setNonhandnum(hvo.getNonhandnum().add(vo.getNonhandnum()));
						map.put(s, hvo);
					}
				}
				List<OnhandVO> list=new ArrayList<OnhandVO>();
				for(String s:map.keySet()){
					list.add(map.get(s));
				}
				onhandVOs=list.toArray(new OnhandVO[0]);
			}
				IUAPQueryBS iQ=NCLocator.getInstance().lookup(IUAPQueryBS.class);
				for(OnhandVO vo:onhandVOs){
					String sql1="select sum(b.nastnum) from so_delivery_b b where b.pk_org='"+vo.getPk_org()+"' and b.csendstordocid ='"+vo.getCwarehouseid()+"' "
							+ " and b.cmaterialid ='"+vo.getCmaterialoid()+"' and b.pk_batchcode='"+vo.getPk_batchcode()+"' and nvl(b.dr,0)=0"
							+ " and not exists(select 1 from ic_saleout_b c where c.csourcebillbid=b.cdeliverybid and  nvl(c.dr,0)=0)";
					Object obj=iQ.executeQuery(sql1, new ColumnProcessor());
					
					UFDouble ud1=obj==null?new UFDouble(0):new UFDouble(obj.toString());
					
					String sql2="select sum(nvl(a.ntalrsnum,0) - nvl(a.ntalrsoutnum,0) ) from ic_reserve a left join scm_batchcode b "
							+ " on a.vbatchcode=b.vbatchcode where a.pk_org='"+vo.getPk_org()+"' "
							+ "and a.cwarehouseid='"+vo.getCwarehouseid()+"' and a.cmaterialoid ='"+vo.getCmaterialoid()+"' "
							+ "and b.pk_batchcode='"+vo.getPk_batchcode()+"' and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and a.fresstate=1";
					Object obj2=iQ.executeQuery(sql2, new ColumnProcessor());
					UFDouble ud2=obj2==null?new UFDouble(0):new UFDouble(obj2.toString());
					
					vo.setNnum2(vo.getNonhandastnum());
					vo.setNnum1(ud1);
					vo.setNrsnum(ud2);
					vo.setNonhandastnum(vo.getNonhandastnum().sub(ud1).sub(ud2));
					vo.setNonhandnum(vo.getNonhandnum().sub(ud1).sub(ud2));
					
				}
				getOnhandCompositeModel().getModel().initModel(onhandVOs);
				
				if(onhandCompositeModel.getModel().getContext().getNodeCode().equals("40060402")){
				//追加检验信息
				String checkcode="('01004','01001','01002','01003','01005','02014')";
				String material="";
				String batchcode="";
				for(OnhandVO vo:onhandVOs){
					material+="'"+vo.getCmaterialoid()+"',";
					batchcode+="'"+vo.getPk_batchcode()+"',";
				}
				
				String sql="select c.vbatchcode,c.pk_material,a.vcheckitemcode,b.vchkvalue,c.vdef1 from qc_checkitem  "
						+ "a left join qc_checkbill_b b on a.pk_checkitem=b.pk_checkitem left join  qc_checkbill c "
						+ "on b.pk_checkbill=c.pk_checkbill   left join scm_batchcode d on c.vbatchcode=d.vbatchcode "
						+ " where    a.vcheckitemcode in "+checkcode+" and "
								+ "d.pk_batchcode in("+batchcode.substring(0, batchcode.length()-1)+") and "
										+ " c.pk_material in ("+material.substring(0, material.length()-1)+")";
				List<Map<String, Object>> maplist=(List<Map<String, Object>>) iQ.executeQuery(sql, new MapListProcessor());

				BillModel model=billListView.getBillListPanel().getHeadBillModel();
				for(int i=0;i<model.getRowCount();i++){
					for(Map<String, Object> ma:maplist){
						if(ma.get("vbatchcode").equals(model.getValueAt(i, "vbatchcode"))){
							model.setValueAt(ma.get("vchkvalue"), i, ma.get("vcheckitemcode").toString());
							model.setValueAt(ma.get("vdef1"), i, "jybz");
						}
					}
				}
				model.execLoadFormula();
				}
			}
			else {
				
				getOnhandCompositeModel().getModel().initModel(onhandVOs);
			}
			
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
			System.out.println(e.getMessage());
		}
		
	}

	/**
	 * @param onhandDimVO
	 * @return
	 */
	private boolean isOnhandDimVOValid(OnhandDimVO onhandDimVO) {
		return onhandDimVO == null || onhandDimVO.getPk_org() == null
				|| onhandDimVO.getCmaterialoid() == null;
	}

	/**
	 * @param onhandDimVO
	 * @return
	 * @return
	 */
	protected OnhandVO[] doQueryInner(OnhandDimVO onhandDimVO) {
		OnhandVO[] onhandVOs = null;
		try {
			OnhandDimVO cloendOnhandDimVO = (OnhandDimVO) onhandDimVO.clone();
			if (this.getOnhandCompositeModel().getOnhandQueryAreaModel()
					.isQueryBatchCodeType()) {
				onhandVOs = queryOfBatchCode(cloendOnhandDimVO);
				return onhandVOs;
			}
			// 查询所有 并且 多组织不空的情况
			if (onhandCompositeModel.getOnhandQueryAreaModel().isQueryAll()
					&& !ValueCheckUtil.isNullORZeroLength(this.pk_orgs)) {
				onhandVOs = this.queryOnhand(pk_orgs, cloendOnhandDimVO,
						onhandCompositeModel);
			} else {
				OnhandQryCond cond = OnhandQryCondBuilder.buildOnhandQryCond(
						cloendOnhandDimVO, this.getOnhandCompositeModel(), this.pk_orgs);
				cond.setBquerySN(true);

				onhandVOs = NCLocator
						.getInstance()
						.lookup(IAtpQuery.class)
						.queryOnhandAndATP(cond,
								this.buildATPQueryParam(cloendOnhandDimVO));
			}
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return onhandVOs;
	}

	/**
	 * 查询所有时，多组织处理
	 * 
	 * @param pk_orgs
	 * @param dimvo
	 * @return
	 */
	private OnhandVO[] queryOnhand(String[] pk_orgs, OnhandDimVO dimvo,
			OnhandCompositeModel onhandCompositeModel) {
		if (ValueCheckUtil.isNullORZeroLength(pk_orgs)) {
			return new OnhandVO[0];
		}
		OnhandDimVO[] queryDims = this.getQueryDims(pk_orgs, dimvo);
		OnhandQryCond onhandCond = OnhandQryCondBuilder.buildOnhandQryCond(
				queryDims, onhandCompositeModel);
		AtpQryCond atpCond = this.getAtpQueryCondWithoutOrg(dimvo,
				onhandCompositeModel);
		try {
			return NCLocator.getInstance().lookup(IAtpQuery.class)
					.queryOnhandAndATP(pk_orgs, onhandCond, atpCond);
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return new OnhandVO[0];
	}

	private AtpQryCond getAtpQueryCondWithoutOrg(OnhandDimVO dimvo,
			OnhandCompositeModel onhandCompositeModel) {
		AtpQryCond cond = new AtpQryCond();
		SqlBuilder sqlsf = new SqlBuilder();
		sqlsf.append(OnhandDimVO.PK_GROUP, AppContext.getInstance()
				.getPkGroup());
		sqlsf.append(" and ");
		sqlsf.append(OnhandDimVO.CMATERIALOID, dimvo.getCmaterialoid());

		List<String> groupFields = onhandCompositeModel.getModel()
				.getGroupFiled();
		boolean isQueryAll = onhandCompositeModel.getOnhandQueryAreaModel()
				.isQueryAll();
		if (isQueryAll || groupFields.contains(OnhandDimVO.CWAREHOUSEID)) {
			cond.setBextwarehouse(true);
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getCwarehouseid())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.CWAREHOUSEID, dimvo.getCwarehouseid());
		}
		if (isQueryAll || groupFields.contains(OnhandDimVO.VBATCHCODE)) {
			cond.setBextbatch(true);
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getPk_batchcode())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.PK_BATCHCODE, dimvo.getPk_batchcode());
		}
		cond.setWhere(sqlsf.toString());
		return cond;
	}

	private OnhandDimVO[] getQueryDims(String[] pk_orgs, OnhandDimVO dimvo) {
		List<OnhandDimVO> queryDims = new ArrayList<OnhandDimVO>();
		for (String pk_org : pk_orgs) {
			OnhandDimVO cloneDim = (OnhandDimVO) dimvo.clone();
			cloneDim.setAttributeValue(OnhandDimVO.PK_ORG, pk_org);
			queryDims.add(cloneDim);
		}
		return queryDims.toArray(new OnhandDimVO[0]);
	}

	/**
	 * @param cloendOnhandDimVO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private OnhandVO[] queryOfBatchCode(OnhandDimVO cloendOnhandDimVO) {
		SqlBuilder whereSQL = new SqlBuilder();
		whereSQL.append(BatchcodeVO.PK_GROUP, InvocationInfoProxy.getInstance().getGroupId());

		if (cloendOnhandDimVO.getCmaterialvid() != null) {
			whereSQL.append(" and ");
			whereSQL.append(BatchcodeVO.CMATERIALVID,
					cloendOnhandDimVO.getCmaterialvid());
		}
		if (cloendOnhandDimVO.getVbatchcode() != null) {
			whereSQL.append(" and ");
			whereSQL.append(BatchcodeVO.VBATCHCODE,
					cloendOnhandDimVO.getVbatchcode());
		}
		if (cloendOnhandDimVO.getPk_batchcode() != null) {
			whereSQL.append(" and ");
			whereSQL.append(BatchcodeVO.PK_BATCHCODE,
					cloendOnhandDimVO.getPk_batchcode());
		}
		whereSQL.append(" and ");
		whereSQL.append(BatchcodeVO.BSEAL, UFBoolean.FALSE);
		whereSQL.append(" and ");
		whereSQL.append("dr", 0);

		String filtersql = this.getOnhandCompositeModel()
				.getOnhandFilterAreaModel()
				.buildFilterFieldSQL(cloendOnhandDimVO);
		whereSQL.append(filtersql);

		List<BatchcodeVO> batchcodeVOs = Collections.emptyList();
		try {
			batchcodeVOs = (List<BatchcodeVO>) NCLocator
					.getInstance()
					.lookup(IMDPersistenceQueryService.class)
					.queryBillOfVOByCondWithOrder(BatchcodeVO.class,
							whereSQL.toString(), true, false,
							new String[] { BatchcodeVO.VBATCHCODE });
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}

		if (batchcodeVOs == null) {
			return new OnhandVO[0];
		}

		OnhandVO[] results = new OnhandVO[batchcodeVOs.size()];
		for (int i = 0; i < batchcodeVOs.size(); i++) {
			results[i] = new OnhandVO();
//			results[i].setVO((ISuperVO) cloendOnhandDimVO.clone());
			results[i].setVbatchcode(batchcodeVOs.get(i).getVbatchcode());
			results[i].setPk_batchcode(batchcodeVOs.get(i).getPk_batchcode());
		}
		return results;
	}

	/**
	 * 处理为实际的结存维度，与结存表的维度一致
	 */
	private OnhandDimVO getRealOnhandDim(OnhandDimVO dimvo) {
		if (dimvo == null) {
			return dimvo;
		}

		try {
		  OnhandDimVO[] dimvos = NCLocator.getInstance().lookup(IOnhandQry.class).getRealOnhandDim(new OnhandDimVO []{dimvo});
      if (!ValueCheckUtil.isNullORZeroLength(dimvos)) {
        return dimvos[0];
      }
    }
    catch (BusinessException e) {
      ExceptionUtils.wrappException(e);
    }
		return dimvo;
	}

	/**
	 * 构建可用量查询条件
	 * 
	 * @param onhandVO
	 */
	private AtpQryCond buildATPQueryParam(OnhandDimVO dimvo) {
		List<String> groupFields = onhandCompositeModel.getModel()
				.getGroupFiled();
		
		AtpQryCond cond = new AtpQryCond();
		SqlBuilder sqlsf = new SqlBuilder();
		sqlsf.append(OnhandDimVO.PK_GROUP, AppContext.getInstance()
				.getPkGroup());
		if (StringUtils.isNotEmpty(dimvo.getPk_org())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.PK_ORG, dimvo.getPk_org());
		}

		sqlsf.append(" and ");
		sqlsf.append(OnhandDimVO.CMATERIALOID, dimvo.getCmaterialoid());
		if (groupFields.contains(OnhandDimVO.CWAREHOUSEID)) {
			cond.setBextwarehouse(true);
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getCwarehouseid())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.CWAREHOUSEID, dimvo.getCwarehouseid());
		}
		if (groupFields.contains(OnhandDimVO.PK_BATCHCODE)) {
			cond.setBextbatch(true);
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getPk_batchcode())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.PK_BATCHCODE, dimvo.getPk_batchcode());
		}
		
		if (groupFields.contains(OnhandDimVO.VBATCHCODE)) {
			cond.setBextbatch(true);
		}
		if (groupFields.contains(OnhandDimVO.CPROJECTID)) {
		  cond.setBextprojectid(true);
		}
    if (groupFields.contains(OnhandDimVO.CVENDORID)) {
      cond.setBextvendorid(true);
    }
    if (groupFields.contains(OnhandDimVO.CASSCUSTID)) {
      cond.setBextasscustid(true);
    }
    if (groupFields.contains(OnhandDimVO.CPRODUCTORID)) {
      cond.setBextproductorid(true);
    }
    if (groupFields.contains(OnhandDimVO.CFFILEID)) {
      cond.setBextffileid(true);
    }
    // 设置是否按照自由辅助属性展开
    this.setBextfree(groupFields, cond);
		
		if (!StringUtil.isSEmptyOrNull(dimvo.getVbatchcode())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VBATCHCODE, dimvo.getVbatchcode());
		}
		
		// 对于批次参照的查询可用量，需要把空批次过滤掉
		boolean isfilterNullBatch = onhandCompositeModel.getOnhandQueryAreaModel().isBatch() && onhandCompositeModel.getOnhandQueryAreaModel().isQueryAtpType();
		if (StringUtil.isSEmptyOrNull(dimvo.getPk_batchcode()) && StringUtil.isSEmptyOrNull(dimvo.getVbatchcode()) && isfilterNullBatch) {
			sqlsf.append(" and ");
			sqlsf.appendIDIsNotNull(OnhandDimVO.PK_BATCHCODE);
		}
		// 增加固定辅助属性
		// 项目
    if (!StringUtil.isSEmptyOrNull(dimvo.getCprojectid())) {
      sqlsf.append(" and ");
      sqlsf.append(OnhandDimVO.CPROJECTID, dimvo.getCprojectid());
//      cond.setBextprojectid(true);
    }
    // 供应商
    if (!StringUtil.isSEmptyOrNull(dimvo.getCvendorid())) {
      sqlsf.append(" and ");
      sqlsf.append(OnhandDimVO.CVENDORID, dimvo.getCvendorid());
//      cond.setBextvendorid(true);
    }
    // 客户
    if (!StringUtil.isSEmptyOrNull(dimvo.getCasscustid())) {
      sqlsf.append(" and ");
      sqlsf.append(OnhandDimVO.CASSCUSTID, dimvo.getCasscustid());
//      cond.setBextasscustid(true);
    }
    // 生产厂商
    if (!StringUtil.isSEmptyOrNull(dimvo.getCproductorid())) {
      sqlsf.append(" and ");
      sqlsf.append(OnhandDimVO.CPRODUCTORID, dimvo.getCproductorid());
//      cond.setBextproductorid(true);
    }
    // 特征码
    if (!StringUtil.isSEmptyOrNull(dimvo.getCffileid())) {
      sqlsf.append(" and ");
      sqlsf.append(OnhandDimVO.CFFILEID, dimvo.getCffileid());
//      cond.setBextffileid(true);
    }
		// 增加自由辅助属性条件
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree1())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE1, dimvo.getVfree1());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree2())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE2, dimvo.getVfree2());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree3())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE3, dimvo.getVfree3());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree4())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE4, dimvo.getVfree4());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree5())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE5, dimvo.getVfree5());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree6())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE6, dimvo.getVfree6());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree7())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE7, dimvo.getVfree7());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree8())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE8, dimvo.getVfree8());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree9())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE9, dimvo.getVfree9());
		}
		if (!StringUtil.isSEmptyOrNull(dimvo.getVfree10())) {
			sqlsf.append(" and ");
			sqlsf.append(OnhandDimVO.VFREE10, dimvo.getVfree10());
		}
		sqlsf.append(onhandCompositeModel.getOnhandFilterAreaModel().buildFilterFieldSQL(dimvo));
		cond.setWhere(sqlsf.toString());
		cond.setPk_org(dimvo.getPk_org());

		return cond;
	}

  private void setBextfree(List<String> groupFields, AtpQryCond cond) {
    for (int i = 1; i <= 10; i ++) {
      if (groupFields.contains("vfree" + i)) {
        cond.setBextfree(true);
        break;
      }
    }
  }

	/**
	 * @return the onhandCompositeModel
	 */
	public OnhandCompositeModel getOnhandCompositeModel() {
		return onhandCompositeModel;
	}

	/**
	 * @param onhandCompositeModel
	 *            the onhandCompositeModel to set
	 */
	public void setOnhandCompositeModel(
			OnhandCompositeModel onhandCompositeModel) {
		this.onhandCompositeModel = onhandCompositeModel;
		this.onhandCompositeModel.getModel().addAppEventListener(this);
	}

	/**
	 * @return the onhandDimVO
	 */
	public OnhandDimVO getOnhandDimVO() {
		// 此处是获取备份的OnhandDimVO，请不要对它进行修改，只允许使用此值。如果需要修改，请先clone
		return this.getOnhandCompositeModel().getModel().getOnhandDimVO();
	}

	/**
	 * @param onhandDimVO
	 *            the onhandDimVO to set
	 */
	public void setOnhandDimVO(OnhandDimVO onhandDimVO) {
		this.getOnhandCompositeModel().getModel().setOnhandDimVO(onhandDimVO);
	}

	@Override
	public void handleEvent(AppEvent event) {
		if (OnhandDlgConst.ONHAND_NEED_REFRESH.equals(event.getType())) {
			this.refresh();
		}
	}

	public String[] getPk_orgs() {
		return pk_orgs;
	}

	public void setPk_orgs(String[] pk_orgs) {
		this.pk_orgs = pk_orgs;
	}
	
	 /**
   * @return the billListView
   */
  public IBillListPanelView getBillListView() {
    return billListView;
  }

  /**
   * @param billListView
   *            the billListView to set
   */
  public void setBillListView(IBillListPanelView billListView) {
    this.billListView = billListView;
  }
  
  /**
   * 监听
   * @author Administrator
   *
   */
  
  private class BillListMultiModelListener implements
	IBillModelRowStateChangeEventListener {
	  
	  @Override
	  public void valueChanged(final RowStateChangeEvent event) {

		  OnhandListView lw=(OnhandListView)billListView;
		  
		  if(!lw.isMultiSelectionEnable()||lw.getMultiSelectionMode()!=BillListView.CHECKBOX_SELECTION){
			  return ;
		  }
	  
	  		synModelMultiSelect(event);
	  		synBodyMultiSelect(event);
	  
	  }

	  private void synModelMultiSelect(RowStateChangeEvent event) {

	  	int startIndex = event.getRow();
	  	int endIndex = event.getEndRow();
	  	if (onhandCompositeModel instanceof IMultiRowSelectModel) {
	  		int[] indexs = new int[endIndex - startIndex + 1];
	  		for (int i = startIndex; i <= endIndex; i++) {
	  			indexs[i - startIndex] = i;
	  		}
	  		if (event.isSelectState()) {
	  			((IMultiRowSelectModel) onhandCompositeModel).addSelectedOperaRow(indexs);
	  		} else {
	  			((IMultiRowSelectModel) onhandCompositeModel)
	  					.removeSelectedOperaRow(indexs);
	  		}
	  	}
	  }

	  private void synBodyMultiSelect(final RowStateChangeEvent event) {

	  	// SwingUtilities.invokeLater(new Runnable() {
	  	// @Override
	  	// public void run() {
	  	//multiSelectBodyRow(event);
	  	// }
	  	// });
	  }



  }
  
  private class BillListHeadSynListener implements
	IBillModelHeadRowStateChangeEventListener {
	  
	  @Override
	  public void valueChanged(RowStateChangeEvent event) {
	  	
		  OnhandListView lw=(OnhandListView)billListView;
		  
		  if(!lw.isMultiSelectionEnable()||lw.getMultiSelectionMode()!=BillListView.CHECKBOX_SELECTION){
			  return ;
		  }
		  
		  synModelMultiSelect(event);
		  
	  }

	  private void synModelMultiSelect(RowStateChangeEvent event) {

	  	int index = event.getRow();
	  	if (event.getOldRowStaus() == event.getRowStaus())
	  		return;
	  	if (onhandCompositeModel instanceof IMultiRowSelectModel) {
	  		if (event.isSelectState()) {
	  			((IMultiRowSelectModel) onhandCompositeModel)
	  					.addSelectedOperaRow(new int[] { index });
	  		} else {
	  			((IMultiRowSelectModel) onhandCompositeModel)
	  					.removeSelectedOperaRow(new int[] { index });
	  		}
	  	}
	  }


  }
}
