Feature: Window Handling - LeafGround
  Validate window switching, multiple windows, and close operations

  Background:
    Given user launches the browser "<browserName>"
    When user navigates to the Window Handling page
    Then the page should be loaded successfully

  Scenario: Verify page title and main element visibility
    Then page title should contain "Window"
    And main window button should be visible

  Scenario: Open new window and verify window count
    When user clicks on Open Home Page in New Window button
    Then new window should be opened
    And total window count should be 2

  Scenario: Capture window handles
    When user stores all window handles
    Then parent and child window handles should be captured

  Scenario: Switch to child window and validate
    When user switches to child window
    Then child window should be active
    And URL should contain "leafground"
    And child page title should be verified

  Scenario: Switch back to parent window
    When user switches back to parent window
    Then parent window should be active
    And main page should be displayed correctly

  Scenario: Open multiple windows and verify handles
    When user opens multiple windows (if available)
    Then more than one child window should be opened
    And all window handles should be stored

  Scenario: Iterate through all windows and validate
    When user switches through all open windows
    Then each window title or URL should be validated

  Scenario: Close child window and validate parent
    When user closes child window
    Then only parent window should remain open
    And parent window should still be active

  Scenario: Verify window count after closing child window
    Then window count should reflect only 1 open window

  Scenario: Attempt switch to closed window
    When user tries to switch to a closed window handle
    Then NoSuchWindowException should be handled

  Scenario: Refresh child window and validate stability
    When user opens child window and refreshes page
    Then page should reload successfully
    And session should remain active

  Scenario: Close all windows and end session
    When user closes all browser windows
    Then browser session should end cleanly