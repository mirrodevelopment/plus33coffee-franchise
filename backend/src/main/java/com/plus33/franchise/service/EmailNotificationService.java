package com.plus33.franchise.service;

import com.plus33.franchise.model.FranchiseApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * =========================================================================
 * EmailNotificationService — Async Email Service
 * =========================================================================
 *
 * Sends HTML emails for:
 *  1. Confirmation to applicant after form submission (apply.html)
 *  2. Admin notification when new application arrives
 *  3. Status update email when application is approved/rejected
 *
 * Email: plus33coffee.franchises@gmail.com (from apply.html JS)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:plus33coffee.franchises@gmail.com}")
    private String adminEmail;

    @Value("${plus33.mail.from:noreply@plus33cafe.com}")
    private String fromEmail;

    /**
     * Sends a stylish HTML confirmation email to the applicant
     * after they submit through apply.html.
     */
    @Async
    public void sendApplicationConfirmationToApplicant(FranchiseApplication application) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "PLUS33 Café Français");
            helper.setTo(application.getEmail());
            helper.setSubject("Your Franchise Application Has Been Received — PLUS33 Café Français");
            helper.setText(buildApplicantConfirmationHtml(application), true);

            mailSender.send(message);
            log.info("Confirmation email sent to applicant: {}", application.getEmail());

        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Failed to send confirmation email to {}: {}", application.getEmail(), e.getMessage());
        }
    }

    /**
     * Sends admin notification with full application details.
     */
    @Async
    public void sendApplicationNotificationToAdmin(FranchiseApplication application) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "PLUS33 Franchise System");
            helper.setTo(adminEmail);
            helper.setSubject("New Franchise Application #" + application.getId() + " — " + application.getFullName());
            helper.setText(buildAdminNotificationHtml(application), true);

            mailSender.send(message);
            log.info("Admin notification sent for application ID: {}", application.getId());

        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Failed to send admin notification: {}", e.getMessage());
        }
    }

    /**
     * Sends status update email when application is approved or rejected.
     */
    @Async
    public void sendStatusUpdateToApplicant(FranchiseApplication application) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "PLUS33 Café Français");
            helper.setTo(application.getEmail());

            boolean approved = application.getStatus() == FranchiseApplication.ApplicationStatus.APPROVED;
            helper.setSubject(approved
                ? "Congratulations! Your Franchise Application Has Been Approved — PLUS33"
                : "Update on Your PLUS33 Franchise Application"
            );
            helper.setText(buildStatusUpdateHtml(application, approved), true);

            mailSender.send(message);
            log.info("Status update email sent to: {} ({})", application.getEmail(), application.getStatus());

        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Failed to send status update email: {}", e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // HTML Email Templates (Branded PLUS33 Style)
    // -----------------------------------------------------------------------

    private String buildApplicantConfirmationHtml(FranchiseApplication app) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <style>
                body { font-family: 'Georgia', serif; background: #071421; color: #F9F7F2; margin: 0; padding: 0; }
                .container { max-width: 600px; margin: 40px auto; background: #0B1D2C; border: 1px solid rgba(212,175,55,0.3); border-radius: 16px; overflow: hidden; }
                .header { background: linear-gradient(135deg, #0B1D2C, #071421); padding: 40px; text-align: center; border-bottom: 1px solid rgba(212,175,55,0.2); }
                .logo-text { font-size: 28px; font-weight: 700; color: #D4AF37; letter-spacing: 4px; }
                .tagline { font-size: 11px; color: rgba(249,247,242,0.5); letter-spacing: 3px; margin-top: 4px; text-transform: uppercase; }
                .body { padding: 40px; }
                .title { font-size: 22px; color: #D4AF37; margin-bottom: 16px; }
                p { color: rgba(249,247,242,0.85); line-height: 1.8; margin-bottom: 16px; }
                .detail-row { display: flex; margin-bottom: 10px; border-bottom: 1px solid rgba(212,175,55,0.1); padding-bottom: 10px; }
                .label { color: rgba(249,247,242,0.5); font-size: 12px; letter-spacing: 1px; text-transform: uppercase; width: 180px; flex-shrink: 0; }
                .value { color: #E7C86A; font-size: 14px; }
                .footer { background: #071421; padding: 24px 40px; text-align: center; border-top: 1px solid rgba(212,175,55,0.1); }
                .footer p { font-size: 12px; color: rgba(249,247,242,0.4); margin: 0; }
              </style>
            </head>
            <body>
              <div class="container">
                <div class="header">
                  <div class="logo-text">PLUS 33</div>
                  <div class="tagline">Café Français · Franchise Division</div>
                </div>
                <div class="body">
                  <h2 class="title">Application Received</h2>
                  <p>Dear %s,</p>
                  <p>Thank you for your interest in joining the PLUS33 Café Français franchise family. We have successfully received your application and our global expansion team will evaluate your request within <strong style="color:#D4AF37">24 to 48 hours</strong>.</p>
                  <p><strong>Application Summary:</strong></p>
                  <div class="detail-row"><span class="label">Applicant</span><span class="value">%s</span></div>
                  <div class="detail-row"><span class="label">Email</span><span class="value">%s</span></div>
                  <div class="detail-row"><span class="label">Location</span><span class="value">%s, %s, %s</span></div>
                  <div class="detail-row"><span class="label">Franchise Model</span><span class="value">%s</span></div>
                  <div class="detail-row"><span class="label">Investment Budget</span><span class="value">%s</span></div>
                  <p style="margin-top:24px;">If you have any questions, please contact us at <a href="mailto:plus33coffee.franchises@gmail.com" style="color:#D4AF37">plus33coffee.franchises@gmail.com</a></p>
                  <p>Great Coffee. Strong Brand. Profitable Partnership.</p>
                </div>
                <div class="footer">
                  <p>© 2025 PLUS33 Café Français · All Rights Reserved</p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(
                app.getFullName(),
                app.getFullName(),
                app.getEmail(),
                app.getCity(), app.getState(), app.getCountry(),
                app.getFranchiseModel(),
                app.getInvestmentBudget() != null ? app.getInvestmentBudget() : "Not specified"
            );
    }

    private String buildAdminNotificationHtml(FranchiseApplication app) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <style>
                body { font-family: Arial, sans-serif; background: #f4f4f4; padding: 20px; }
                .container { max-width: 700px; margin: auto; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                .header { background: #071421; color: #D4AF37; padding: 24px 32px; font-size: 20px; font-weight: bold; letter-spacing: 2px; }
                .badge { display: inline-block; background: #D4AF37; color: #071421; padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: bold; margin-left: 12px; }
                .body { padding: 32px; }
                table { width: 100%%; border-collapse: collapse; }
                th { text-align: left; background: #f8f8f8; padding: 10px 16px; font-size: 12px; color: #666; letter-spacing: 1px; text-transform: uppercase; }
                td { padding: 12px 16px; border-bottom: 1px solid #eee; font-size: 14px; color: #333; }
                .status { background: #FFF3CD; color: #856404; padding: 3px 10px; border-radius: 4px; font-size: 12px; font-weight: bold; }
              </style>
            </head>
            <body>
              <div class="container">
                <div class="header">PLUS33 — New Franchise Application <span class="badge">#%d</span></div>
                <div class="body">
                  <table>
                    <tr><th colspan="2">PERSONAL INFORMATION</th></tr>
                    <tr><td>Full Name</td><td>%s</td></tr>
                    <tr><td>Email</td><td>%s</td></tr>
                    <tr><td>Phone</td><td>%s %s</td></tr>
                    <tr><td>Location</td><td>%s, %s, %s %s</td></tr>
                    <tr><th colspan="2">PROFESSIONAL BACKGROUND</th></tr>
                    <tr><td>Occupation</td><td>%s</td></tr>
                    <tr><td>Employer</td><td>%s</td></tr>
                    <tr><td>Industry</td><td>%s</td></tr>
                    <tr><td>Experience</td><td>%s</td></tr>
                    <tr><th colspan="2">FRANCHISE PREFERENCES</th></tr>
                    <tr><td>Franchise Model</td><td>%s</td></tr>
                    <tr><td>Location Type</td><td>%s</td></tr>
                    <tr><td>Target Region</td><td>%s</td></tr>
                    <tr><td>Has Location</td><td>%s</td></tr>
                    <tr><th colspan="2">INVESTMENT</th></tr>
                    <tr><td>Budget</td><td>%s</td></tr>
                    <tr><td>Finance Source</td><td>%s</td></tr>
                    <tr><td>Status</td><td><span class="status">PENDING REVIEW</span></td></tr>
                  </table>
                </div>
              </div>
            </body>
            </html>
            """.formatted(
                app.getId(),
                app.getFullName(), app.getEmail(),
                orNA(app.getPhoneCode()), orNA(app.getPhone()),
                orNA(app.getCity()), orNA(app.getState()), orNA(app.getCountry()), orNA(app.getZip()),
                orNA(app.getOccupation()), orNA(app.getEmployer()), orNA(app.getIndustry()), orNA(app.getYearsExperience()),
                orNA(app.getFranchiseModel()), orNA(app.getLocationType()), orNA(app.getRegion()),
                app.getHasLocation() != null ? (app.getHasLocation() ? "Yes" : "No") : "Not specified",
                orNA(app.getInvestmentBudget()), orNA(app.getFinanceSource())
            );
    }

    private String buildStatusUpdateHtml(FranchiseApplication app, boolean approved) {
        String color = approved ? "#27AE60" : "#E74C3C";
        String statusWord = approved ? "APPROVED" : "NOT PROCEEDING";
        String message = approved
            ? "Congratulations! We are thrilled to inform you that your franchise application has been approved. Our team will reach out to you shortly to discuss the next steps."
            : "Thank you for your interest in PLUS33 Café Français. After careful review, we are unable to proceed with your application at this time. We encourage you to reapply in the future.";

        return """
            <!DOCTYPE html>
            <html lang="en">
            <head><meta charset="UTF-8"></head>
            <body style="font-family:Georgia,serif;background:#071421;color:#F9F7F2;margin:0;padding:40px 20px;">
              <div style="max-width:560px;margin:auto;background:#0B1D2C;border:1px solid rgba(212,175,55,0.3);border-radius:16px;overflow:hidden;">
                <div style="background:linear-gradient(135deg,#0B1D2C,#071421);padding:40px;text-align:center;">
                  <div style="font-size:28px;font-weight:700;color:#D4AF37;letter-spacing:4px;">PLUS 33</div>
                </div>
                <div style="padding:40px;">
                  <h2 style="color:%s;margin-bottom:16px;">Application %s</h2>
                  <p style="color:rgba(249,247,242,0.85);line-height:1.8;">Dear %s,</p>
                  <p style="color:rgba(249,247,242,0.85);line-height:1.8;">%s</p>
                  <p style="color:rgba(249,247,242,0.85);">For further information, please contact us at <a href="mailto:plus33coffee.franchises@gmail.com" style="color:#D4AF37">plus33coffee.franchises@gmail.com</a></p>
                </div>
              </div>
            </body>
            </html>
            """.formatted(color, statusWord, app.getFullName(), message);
    }

    private String orNA(String value) {
        return (value != null && !value.isBlank()) ? value : "N/A";
    }
}
