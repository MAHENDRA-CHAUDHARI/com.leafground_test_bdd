Feature: User Registration functionality
  
Background: User is successfully Landing on DemoTour Site user is successfully logged in
Given user Launch browser "<browserName>"
Given user enter url and open Home page

@SmokeTest
Scenario Outline: Register successfully with valid information
When user click on Register Menu
When user enter First Name "<first_name>"
When user enter Last Name "<last_name>"
When user enter Phone Number "<phone_number>"
When user enter Email ID "<email_id>"
When user enter address "<address>"
When user enter select city name "<city_name>"
When user enter state name "<state_name>"
When user enter Postal Code "<postal_code>"
When user select country "<country>"
When user enter username "<userName>"
When user enter password "<password>"
When user enter confirmation password "<confirm_password>"
When prompt popup is open then close it for further activity
Then user click on Submit button
Then verify complete registration salutation for "<first_name>" and "<last_name>"
Then user can see the registration confirmation message
Then user can validate the user details "<userName>"
Then successfully registration done then closed browser

Examples:
| first_name | last_name | phone_number | email_id           | address     | city_name | state_name | postal_code | country       | userName  | password  | confirm_password |
| John       | Doe       | +9100123456  | john@example.com   | 123 Main St | New York  | NY         | 10001       | UNITED STATES | johndoe   | Pass@123  | Pass@123        |
| Laurel     | Max       | +91220123456 | laurel@example.com | 123 MG Road | BOMBAY    | IN         | 90001       | INDIA         | laurelmax | Pass1@123  | Pass1@123        |

@RegresTest
Scenario Outline: Register successfully with Special Characters information
When user click on Register Menu
When user enter First Name "<first_name1>"
When user enter Last Name "<last_name1>"
When user enter Phone Number "<phone_number1>"
When user enter Email ID "<email_id1>"
When user enter address "<address1>"
When user select country "<country1>"
When user enter username "<userName1>"
When user enter password "<password1>"
When user enter confirmation password "<confirm_password1>"
When prompt popup is open then close it for further activity
Then user click on Submit button
Then successfully registration done then closed browser
Examples:
| first_name1 | last_name1 | phone_number1 | email_id1           | address1     | country1       | userName1  | password1  | confirm_password1 |
| Mike       | Tyson       | +9 0123456  | mike@example.com   | 420 Main St | PANAMA | miketyson   | users@123  | user@123        |
