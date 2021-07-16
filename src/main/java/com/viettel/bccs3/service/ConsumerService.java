package com.viettel.bccs3.service;

import com.viettel.bccs3.config.Topic;
import com.viettel.bccs3.domain.dto.CreateInvoiceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConsumerService {

    private final InvoiceCreationService invoiceCreationService;
    private static final String TOPIC_CREATE_INVOICE = Topic.BCCS3_CREATE_INVOICE;

    @KafkaListener(topics = TOPIC_CREATE_INVOICE, containerFactory = "partitionsKafkaListenerContainerFactory")
    public void listenToPartition(@Payload CreateInvoiceDTO message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        log.info("Received Message: " + message.toString() + " from partition: " + partition + " at " + formatter.format(LocalDateTime.now()));
        log.info("Create invoice at: " + LocalDateTime.now());
        try {
            invoiceCreationService.createInvoice(message);
            log.info("---------------------------------------------------------------------------------------------");
        } catch (Exception e) {
            log.error("Create invoice error: " + e);
        }
    }

}
