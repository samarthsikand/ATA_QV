Feature: Testing the redbus.in website services

    Scenario: Testing the Bell website
    Given I am on "www.utexas.edu/learn/forms/radio.html" website
	When I select "Mr" radiobutton near "Mrs"
	Then I verify the results