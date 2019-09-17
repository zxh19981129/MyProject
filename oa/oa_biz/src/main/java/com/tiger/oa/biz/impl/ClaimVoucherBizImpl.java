package com.tiger.oa.biz.impl;

import com.tiger.oa.biz.ClaimVoucherBiz;
import com.tiger.oa.dao.ClaimVoucherDao;
import com.tiger.oa.dao.ClaimVoucherItemDao;
import com.tiger.oa.dao.DealRecordDao;
import com.tiger.oa.dao.EmployeeDao;
import com.tiger.oa.entity.ClaimVoucher;
import com.tiger.oa.entity.ClaimVoucherItem;
import com.tiger.oa.entity.DealRecord;
import com.tiger.oa.entity.Employee;
import com.tiger.oa.global.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service("claimVoucherBiz")
public class ClaimVoucherBizImpl implements ClaimVoucherBiz {
    @Qualifier("claimVoucherDao")
    @Autowired
    private ClaimVoucherDao claimVoucherDao;
    @Qualifier("claimVoucherItemDao")
    @Autowired
    private ClaimVoucherItemDao claimVoucherItemDao;
    @Qualifier("dealRecordDao")
    @Autowired
    private DealRecordDao dealRecordDao;
    @Qualifier("employeeDao")
    @Autowired
    private EmployeeDao employeeDao;
    public void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setCreateTime(new Date());
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Content.CLAIMVOUCHER_CREATED);
        claimVoucherDao.insert(claimVoucher);
        for (ClaimVoucherItem item: items){
            item.setClaimVoucherId(claimVoucher.getId());
            claimVoucherItemDao.insert(item);
        }
    }

    public ClaimVoucher get(int id) {
        return claimVoucherDao.select(id);
    }

    public List<ClaimVoucherItem> getItems(int claimVoucherId) {
        return claimVoucherItemDao.selectByClaimVoucherId(claimVoucherId);
    }

    public List<DealRecord> getRecords(int claimVoucherId) {
        return dealRecordDao.selectByClaimVoucherId(claimVoucherId);
    }

    public List<ClaimVoucher> getForSelf(String sn) {
        return claimVoucherDao.selectByCreateSn(sn);
    }

    public List<ClaimVoucher> getForDeal(String sn) {
        return claimVoucherDao.selectByNextDealSn(sn);
    }

    public void update(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Content.CLAIMVOUCHER_CREATED);
        claimVoucherDao.update(claimVoucher);

        List<ClaimVoucherItem> olds = claimVoucherItemDao.selectByClaimVoucherId(claimVoucher.getId());
        for (ClaimVoucherItem old:olds){
            boolean isHave = false;
            for (ClaimVoucherItem item:items){
                if (item.getId()==old.getId()) {
                    isHave = true;
                    break;
                }
        }
            if(!isHave){
                claimVoucherItemDao.delete(old.getId());
            }
        }
        for (ClaimVoucherItem item:items){
            item.setClaimVoucherId(claimVoucher.getId());
            if(item.getId()!=null && item.getId()>0){
                claimVoucherItemDao.update(item);
            }else{
                claimVoucherItemDao.insert(item);
            }
        }
    }

    public void submit(int id) {
        ClaimVoucher claimVoucher = claimVoucherDao.select(id);
        Employee employee = employeeDao.select(claimVoucher.getCreateSn());

        claimVoucher.setStatus(Content.CLAIMVOUCHER_SUBMIT);
        claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(employee.getDepartmentSn(),Content.POST_FM).get(0).getSn());
        claimVoucherDao.update(claimVoucher);

        DealRecord dealRecord = new DealRecord();
        dealRecord.setClaimVoucherId(id);
        dealRecord.setComment("无");
        dealRecord.setDealResult(Content.CLAIMVOUCHER_SUBMIT);
        dealRecord.setDealSn(employee.getSn());
        dealRecord.setDealTime(new Date());
        dealRecord.setDealWay(Content.DEAL_SUBMIT);
        dealRecordDao.insert(dealRecord);
    }

    public void deal(DealRecord dealRecord) {
        ClaimVoucher claimVoucher = claimVoucherDao.select(dealRecord.getClaimVoucherId());
        Employee employee = (Employee)employeeDao.select(dealRecord.getDealSn());
        dealRecord.setDealTime(new Date());

        if (dealRecord.getDealWay().equals(Content.DEAL_PASS)){
            if (claimVoucher.getTotalAmount()<Content.LIMIT_CHECK || employee.getPost().equals(Content.POST_GM)){
                claimVoucher.setStatus(Content.CLAIMVOUCHER_SUBMIT);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null,Content.POST_CASHIER).get(0).getSn());

                dealRecord.setDealResult(Content.CLAIMVOUCHER_SUBMIT);

            }else{
                claimVoucher.setStatus(Content.CLAIMVOUCHER_RECHECK);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null,Content.POST_GM).get(0).getSn());


                dealRecord.setDealResult(Content.CLAIMVOUCHER_RECHECK);
            }
        }else if (dealRecord.getDealWay().equals(Content.DEAL_BACK)){
            claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
            claimVoucher.setStatus(Content.CLAIMVOUCHER_BACK);


            dealRecord.setDealResult(Content.CLAIMVOUCHER_BACK);
        }else if (dealRecord.getDealWay().equals(Content.DEAL_REJECT)){
            claimVoucher.setStatus(Content.CLAIMVOUCHER_TERMINATED);
            claimVoucher.setNextDealSn(null);

            dealRecord.setDealResult(Content.CLAIMVOUCHER_BACK);

        }else if (dealRecord.getDealWay().equals(Content.DEAL_PAID)){
            claimVoucher.setNextDealSn(null);
            claimVoucher.setStatus(Content.CLAIMVOUCHER_PAID);

            dealRecord.setDealResult(Content.CLAIMVOUCHER_PAID);
        }
        claimVoucherDao.update(claimVoucher);
        dealRecordDao.insert(dealRecord);
    }

}
