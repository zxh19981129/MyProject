package com.tiger.oa.dao;

import com.tiger.oa.entity.ClaimVoucher;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("claimVoucherDao")
public interface ClaimVoucherDao {
    void insert(ClaimVoucher claimVoucher);
    void delete(int id);
    void update(ClaimVoucher claimVoucher);
    ClaimVoucher select(int id);
    List<ClaimVoucher> selectByCreateSn(String createSn);
    List<ClaimVoucher> selectByNextDealSn(String nextDealSn);
}
