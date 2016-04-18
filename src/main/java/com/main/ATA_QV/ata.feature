Feature: Testing the redbus.in website services

    Scenario: Testing the Bell website
	Given I am on "redbus.in" website
	When I set "From" field near "To" to "Delhi"
	And I set "To" field near "From" to "Jaipur"
	And I click "Date of Journey" field near "From"
	And I select date "Apr  2016,9" from calendar
	And I select "9" from "Apr  2016" table
	And I click on "Search Buses" button
	And I select "Milk" radiobutton near "question"
	And I select "Milk" from "Dairy" radiobuttongroup near "something"
	Then I verify the results