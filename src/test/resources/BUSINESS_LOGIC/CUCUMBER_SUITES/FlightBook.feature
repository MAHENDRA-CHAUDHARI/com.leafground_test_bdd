Feature: Flight Booking feature functionality

Background: User is successfully Landng on DemoTour Site user is successfully logged in
Given user Launching browser "<browserName>"
Given user enter url and open Home page for Flight Book

#1
@Test
Scenario Outline:: Filled successfully with valid Flight information
When verify Flight Menu availability "<menu_name>"
Then user click on Fligt Menu
Then Verify Instruction message after Landing on Filght Menu "<Instruction_msg>"
Then verify selected default service class type
Given user selects "<trip_type>" trip type
And user selects departure city as "<from_city>"
And user selects destination city as "<to_city>"
And user selects departure date as "<departure_date>"
And user selects return date as "<return_date>"
And user selects number of passengers as "<passengers>"
And user selects service class as "<service_class>"
And user selects airline as "<airline>"
When user clicks on "Continue" button
Then user should see expected result "<expected_result>"
Then close browser window

Examples:
 |menu_name| trip_type  | from_city | to_city | departure_date | return_date | passengers | service_class | airline     | expected_result|Instruction_msg|
 |Flights| Round Trip | New York  | London  | 10     | 20  | 2          | Economy       | No Preference | flight results displayed|Use our Flight Finder to search for the lowest fare on participating airlines. Once you've booked your flight, don't forget to visit the Mercury Tours Hotel Finder to reserve lodging in your destination city.|
 #|Flights| One Way    | Paris     | Tokyo   | 01     |             | 1          | Business      | Unified      | flight results displayed|Use our Flight Finder to search for the lowest fare on participating airlines. Once you've booked your flight, don't forget to visit the Mercury Tours Hotel Finder to reserve lodging in your destination city.|
