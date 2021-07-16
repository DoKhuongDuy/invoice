package com.viettel.bccs3.service;

import com.viettel.bccs3.domain.dto.CreateInvoiceDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProducerService {

    @Autowired
    private KafkaTemplate<String, CreateInvoiceDTO> createInvoiceDTOKafkaTemplate;

    public void sendMessage(CreateInvoiceDTO createInvoiceDTO, String topic, int partition) {
        try {
            this.createInvoiceDTOKafkaTemplate.send(topic, partition, KafkaHeaders.MESSAGE_KEY, createInvoiceDTO);
            log.info("Sent message = [" + createInvoiceDTO.toString() + "] success!");
        } catch (Exception e) {
            log.error(e);
            log.info("Unable to send message = [" + createInvoiceDTO.toString() + "]");
        }
    }
}
