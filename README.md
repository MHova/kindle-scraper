# KindleScraper

KindleScraper is an application that monitors the price of the [Amazon Fire HD 10 tablet](https://www.amazon.com/Amazon_Fire_HD_10/dp/B0BHZT5S12) and sends an email notification if its price drops by more than a customizable absolute delta.

Please see the commit and pull request histories for additional commentary beyond this `README`.

How to start the KindleScraper application
---
Maven is required to build this application.

1. Run `mvn clean install` to build the application
1. Start the application with `java -jar target/kindleScraper-0.0.1-SNAPSHOT.jar server config.yml`

See a visualization of the price check history at `http://localhost:8080/chart.html`

Scraping will occur at regular intervals as soon as the application starts. Dropwizard and other framework logs are sent to console while application logs are sent to both console and `logfile.log`.

By default, the application will do the following:
- read price information from a static html document at `src/main/resources/kindle.htm` instead of doing a real web scrape. The price listed in the document is `$94.99`. The document can be modified in real-time while the application is running for testing purposes. The price can be found on line `7123`.
- send price drop notifications to the log instead of sending an email

To customize these and other behaviors, see the [Configuration](#Configuration) section below.

Configuration
---
Settings are located in `config.yml`.

##### Notifications
To enable logging notifications:
- set `notification.type` to `logging`

To enable email notifications:
- set `notification.type` to `email`
- set `notification.user` to your SMTP server username, which is probably your email address. This will also be used as the sender.
- set `notification.password` to your SMTP server password. For Gmail, this will be a generated app password. See this support [article](https://support.google.com/accounts/answer/185833?hl=en) then go [here](https://myaccount.google.com/apppasswords) to setup your app password.
- set `notification.recipient` to the email address to which the notifications should be sent.
- set `notification.smtpHost` to the SMTP host address. Defaults to `smtp.gmail.com`
- set `notification.smtpPort` to the SMTP port. Defaults to `587`

##### HTML Source
To parse HTML from a local file:
- set `document.type` to `file`
- set `document.fileLocation` to the location of the local HTML file. A sample HTML file is included at `src/main/resources/kindle.htm`.

To parse HTML from a real website:
- set `document.type` to `web`
- set `document.url` to the desired web URL. Defaults to `https://www.amazon.com/Amazon_Fire_HD_10/dp/B0BHZT5S12`

##### Scraping Interval
Customize how often scraping occurs with `jobs.scrapeJob` (defaults to `1s`). See the `@Every` section of the `dropwizard-jobs` [documentation](https://github.com/dropwizard-jobs/dropwizard-jobs?tab=readme-ov-file#available-job-types) for the list of all supported time units.

A reasonable interval for real web scraping for the purposes of testing and reviewing the application should be `10s`. A reasonable interval for actually monitoring the price of a Kindle is probably `12h`.

##### Minimum Price Decrease
The absolute price difference threshold that must be reached in order for a price drop to trigger a notification is configured with `minimumPriceDecrease`. Defaults to `0.01`.

Health Checks
---
Service health checks are located at [http://localhost:8081/healthcheck](http://localhost:8081/healthcheck). Specifically, there is a `scrapeJob` health check that will fail if the scraping job is no longer running. The likeliest reason why the scraping job would no longer be running is that it encountered an error during HTML parsing. This probably signals either a change in the web page's structure or the web site blocking the scraping job with a captcha. Human intervention is required in this case.

Architecture
---
I debated between building this as a serverless application deployed on AWS Lambda versus a locally-hosted persistent service. While the former is probably what I'd do if this were a real-world application for scalability reasons, I landed on the latter architecture as I wanted to optimize for ease of packaging, delivery, and review for the purposes of this take-home interview assignment.

Technology Choices
---
I chose [Dropwizard](https://www.dropwizard.io) as the skeleton for this application as it is the Java web service framework I am most familiar with. The web scraper runs as a [Quartz](https://www.quartz-scheduler.org/) job via the [dropwizard-jobs](https://github.com/dropwizard-jobs/dropwizard-jobs) plugin. Price check history is persisted in an in-memory [H2](http://h2database.com/html/main.html) database. H2, with its simplicity and ease of use, is specifically designed for this exact use case: an experimental not-meant-for-production application. Additionally, using a built-in in-memory database reduces the amount of required setup and avoids leaving footprints on the local environment, both to the benefit of the reviewer of this assignment.

Challenges
---
The five biggest unknowns with this project were:
##### HTML parsing
Can I decipher the giant 24k line HTML page and figure out the CSS selectors that are specific enough to pull out the price and exactly the price? It took fiddling with the browser element inspector tool and manually scrolling through the HTML a bunch to figure it out. The online HTML/CSS course I've been taking helped immensely with navigating the document and constructing the correct CSS selectors.

##### H2 in-memory DB integration
Will this just work out of the box? I've maybe used H2 once like 10+ years ago. Fortunately the answer was "yes." The H2 configurations I found online and the JDBI declarative API worked swimmingly right out of the box.

##### Email notifications
Can I figure out how to send emails programmatically through Gmail's SMTP server? The internet was once again very helpful with figuring out the correct incancations to create a `javax.mail.Session` and a `javax.mail.internet.MimeMessage`. Discovering the existence of Google's "app password" mechanism and how to navigate it were a bit tricky, but I managed to do that as well. I am now the proud owner of the `ecfx.project@gmail.com` email address.

##### Real web scraping
Will Amazon out me as a bot and hit me with a captcha once I start programmatically hitting their website? The general consensus online is that 5s is a reasonable interval for web scraping. I took a more conservative approach and only ever scraped at 10s intervals during development. Amazon did indeed intermittently hit me with captchas when I was sending random junk in the `user-agent` header. Once I found an acceptable `user-agent` string to send (thank you, once again, to the internet), Amazon never again hit me with a captcha. As a result, I did not even attempt to open the can of worms that is captcha-breaking.

My solution to handling page structure changes or captchas is to simply fail fast as any fix would require human intervention anyway. The scraper job stops running and the application's health check will start failing in this case.

##### Price check history visualization
Can I figure out how to get Chart.js working to display a simple line graph of the price check history? The answer is yes, though with some difficulty in getting from zero to one. See the comments in this [commit](https://github.com/MHova/kindle-scraper/commit/67ae3450b18b97705d95470a14019447a1ee66b0).
