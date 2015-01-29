package com.dupont.budget.service.jms;

import com.dupont.budget.dto.EmailDTO;


public interface DupontMailSender {
    
    public void sendMail(EmailDTO emailDTO) ;
}
