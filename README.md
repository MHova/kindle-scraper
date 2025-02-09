# KindleScraper

How to start the KindleScraper application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/kindleScraper-0.0.1-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Notification Configuration
---
Switch between logging and email notifications by modifying the following fields in `config.yml`:

Logging notifications:
- `notification.type`: set to `logging`

Email notifications:
- `notification.type`: set to `email`
- `notification.user`: your username for the SMTP server, which is probably your email address. This will also be used as the sender.
- `notification.password`: your password for the SMTP server. For Gmail, this will be a generated app password. See this support [article](https://support.google.com/accounts/answer/185833?hl=en) then go [here](https://myaccount.google.com/apppasswords) to setup your app password.
- `notification.recipient`: the email address to which the notifications should be sent
- `notification.smtpHost`: The SMTP host address. Defaults to `smtp.gmail.com`
- `notification.smtpPort`: The SMTP port. Defaults to `587`

Document Source Configuration
---
Switch between parsing a local html file and scraping from the real web by modifying the following fields in `config.yml`:

Local file parsing:
- `document.type`: set to `file`
- `document.fileLocation`: set to location of the local file to parse. A sample html file is included at `src/main/resources/kindle.htm`.

Real web scraping:
- `document.type`: set to `web`
- `document.url`: set to the web URL to scrape. Defaults to `https://www.amazon.com/Amazon_Fire_HD_10/dp/B0BHZT5S12`

Be sure to also set the scraping frequency at `jobs.scrapeJob`. I have been using `1s` for local file parsing and `10s` for real web scraping. See the `@Every` section of the `dropwizard-jobs` [documentation](https://github.com/dropwizard-jobs/dropwizard-jobs?tab=readme-ov-file#available-job-types) for  the list of all supported time units.

Minimum Price Decrease
---
Set the `minimumPriceDecrease` config field to change by how much the price needs to drop before we send a notification. Defaults to `0.01`.

Logging
---
`Dropwizard` logs are sent to console while application logs are sent to both console and `logfile.log`.
