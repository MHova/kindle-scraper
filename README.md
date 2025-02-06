# KindleScraper

How to start the KindleScraper application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/kindleScraper-0.0.1-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Email Configuration
---

If using email notifications, the following fields will need to be filled out in `config.yml`:
- `email.user`: your username for the SMTP server, which is probably your email address. This will also be used as the sender.
- `email.password`: your password for the SMTP server. For Gmail, this will be a generated app password. See this support [article](https://support.google.com/accounts/answer/185833?hl=en) then go [here](https://myaccount.google.com/apppasswords) to setup your app password.
- `email.recipient`: the email address to which the notifications should be sent
- `email.smtpHost`: The SMTP host address. Defaults to `smtp.gmail.com`
- `email.smtpPort`: The SMTP port. Defaults to `587`