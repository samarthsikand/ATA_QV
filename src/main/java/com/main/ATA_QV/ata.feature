Feature: Testing the redbus.in website services

    Scenario: Testing the Bell website
	Given I am on "redbus.in" website
	When I set "input" field near "From" to "Delhi"
	And I set "input" field near "To" to "Jaipur"
	And I click "Date of Journey" field near "From"
	And I select date "Apr  2016,27" from calendar
	Then I verify the results