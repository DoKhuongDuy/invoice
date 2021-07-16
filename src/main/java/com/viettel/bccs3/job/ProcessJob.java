package com.viettel.bccs3.job;

import com.viettel.bccs3.common.Constant;
import com.viettel.bccs3.service.InvoiceJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class ProcessJob {
    private final InvoiceJobService jobService;

    //    @Scheduled(cron = Constant.EVERY_TEN_SECONDS)
    @Scheduled(cron = Constant.EVERY_2MINUTE)
    public void getCurrentTransInvoiceProcess() {
        try {
            jobService.getCurrentTransInvoice();
            log.info("---------------------------------------------------------------------------------------------");
        } catch (Exception e) {
            log.error("Get current transaction invoice error: " + e);
        }
    }

    //    @Scheduled(cron = Constant.EVERY_LAST_DAY)
    public void getLastDayTransInvoiceProcess() {
        try {
            jobService.getLastDayTransInvoice();
        } catch (Exception e) {
            log.error("Get last day transaction invoice error: " + e);
        }
    }
}
