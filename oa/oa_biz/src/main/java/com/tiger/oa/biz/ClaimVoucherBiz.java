package com.tiger.oa.biz;

import com.tiger.oa.entity.ClaimVoucher;
import com.tiger.oa.entity.ClaimVoucherItem;
import com.tiger.oa.entity.DealRecord;

import java.util.List;

public interface ClaimVoucherBiz {
    void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items);
    ClaimVoucher get(int id);
    List<ClaimVoucherItem> getItems(int claimVoucherId);
    List<DealRecord> getRecords(int claimVoucherId);

    List<ClaimVoucher> getForSelf(String sn);
    List<ClaimVoucher> getForDeal(String sn);

    void update(ClaimVoucher claimVoucher,List<ClaimVoucherItem> items);

    void submit(int id);

    void deal(DealRecord dealRecord);
}
