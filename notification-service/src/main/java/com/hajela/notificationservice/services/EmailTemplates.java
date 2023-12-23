package com.hajela.notificationservice.services;

public class EmailTemplates {
    public static final String ACTIVATION_EMAIL_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Helping Hands Activation</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);">
                    <h2 style="color: #1a73e8;">Welcome to Helping Hands!</h2>
                    <p>Thank you for joining Helping Hands. To activate your account, please click the link below:</p>                                
                    <p>
                        <a href="%s" style="display: inline-block; padding: 10px 20px; background-color: #1a73e8; color: #fff; text-decoration: none; border-radius: 5px;">Activate Your Account</a>
                    </p>
                    <p>If you did not sign up for Helping Hands, please ignore this email.</p>
                    <p>Best Regards,<br>
                        The Helping Hands Team</p>
                </div>
            </body>
            </html>
            """;

    public static final String RESET_PASSWORD_EMAIL_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Helping Hands Password Reset</title>
            </head>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);">
                    <h2 style="color: #1a73e8;">Helping Hands Password Reset</h2>
                    <p>We received a request to reset your Helping Hands account password. If you did not make this request, you can ignore this email.</p>
                    <p>To reset your password, please click the link below:</p>
                    <p>
                        <a href="%s" style="display: inline-block; padding: 10px 20px; background-color: #1a73e8; color: #fff; text-decoration: none; border-radius: 5px;">Reset Password</a>
                    </p>
                    <p>This link will expire in 24 hours for security reasons. If you continue to have problems, please contact our support team.</p>
                    <p>Best Regards,<br>
                        The Helping Hands Team</p>
                </div>
            </body>
            </html>
    """;
}
