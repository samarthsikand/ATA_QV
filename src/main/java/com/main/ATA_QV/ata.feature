Feature: Testing the redbus.in website services

    Scenario: Testing the Bell website
	Given I am on "redbus.in" website
	When I set "From" field near "To" to "Delhi"
	And I set "To" field near "From" to "Jaipur"
	And I click "Date of Journey" field near "From"
	Then I verify the results