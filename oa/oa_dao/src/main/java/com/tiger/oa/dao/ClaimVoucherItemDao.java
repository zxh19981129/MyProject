package com.tiger.oa.dao;

import com.tiger.oa.entity.ClaimVoucherItem;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("claimVoucherItemDao")
public interface ClaimVoucherItemDao {
    void insert(ClaimVoucherItem claimVoucherItem);
    void delete(int id);
    void update(ClaimVoucherItem claimVoucherItem);
    List<ClaimVoucherItem> selectByClaimVoucherId(int claimVoucherId);
}
