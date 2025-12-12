package com.shamim.ecommerce.service;

import com.shamim.ecommerce.model.Seller;
import com.shamim.ecommerce.model.SellerReport;

public interface SellerReportService {

    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
