package com.fis.fpt.service;

import com.fis.fpt.client.NotifyCentralClient;
import com.fis.fpt.request.NotifyCentralForm;
import com.fis.fpt.service.dto.ContentNotify;
import com.fis.fpt.service.dto.NotifyCentralDTO;
import com.fis.fpt.service.dto.ReceiverDTO;
import com.fis.fpt.web.rest.errors.BadRequestAlertException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotifyService {
    private final NotifyCentralClient notifyCentralClient;
    public void sendNotify(List<ReceiverDTO> notifyUsers,
                           List<ReceiverDTO> notifyEmails,
                           ContentNotify content,
                           String type) {

        try {
            NotifyCentralDTO notifyCentralDTO = new NotifyCentralDTO();
            notifyCentralDTO.setType(type);
            notifyCentralDTO.setNotifyUsers(notifyUsers);
            notifyCentralDTO.setNotifyEmails(notifyEmails);
            notifyCentralDTO.setContent(content);
            notifyCentralDTO.setSystem("E_FORM");

            String status = notifyCentralClient.sendNotification(notifyCentralDTO);

            if (!"ok".equalsIgnoreCase(status)) {
                log.warn("NotifyCentral returned non-ok status: {}", status);
            }

        } catch (Exception e) {
            log.warn("NotifyCentral call failed. Skip notification. Type={}", type, e);
        }
    }
}
