Feature: Testing the redbus.in website services

    Scenario: Testing the Bell website
    Given I am on "www.utexas.edu/learn/forms/radio.html" website
	When I select "Mr" radiobutton near "Mrs"
	And I select "abc" from "xyz" radiobuttongroup near "klmn"
	And I select "xyz" checkbox near "abc"
	And I select "value" from "abc" dropdown near "xyz"
	Then I verify the results