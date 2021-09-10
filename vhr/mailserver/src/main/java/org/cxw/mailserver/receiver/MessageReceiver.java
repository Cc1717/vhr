package org.cxw.mailserver.receiver;

import org.cxw.vhr.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Component
public class MessageReceiver {
    private static final Logger logger = LoggerFactory.getLogger(MessageReceiver.class);
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    MailProperties mailProperties;
    @Autowired
    TemplateEngine templateEngine;

    @RabbitListener(queues = "vhr.employee.welcome")
    public void handle(Employee employee) {
        logger.info("消息接收:" + employee.toString());
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            mimeMessageHelper.setTo(employee.getEmail());
            mimeMessageHelper.setSubject("入职通知");
            mimeMessageHelper.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("employeeName", employee.getName());
            context.setVariable("positionName", employee.getPosition().getName());
            context.setVariable("jobLevelName", employee.getJobLevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            mimeMessageHelper.setText(mail, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("邮件发送失败:" + e.getMessage());
        }
    }
}
