Feature: Alert Handling - LeafGround
  Validate different types of JavaScript and Sweet Alerts

  Background:
    Given user launches the browser
    When user navigates to the Alert Handling page
    Then the page should be loaded successfully

  # ----------------------------
  # STEP 1: Simple Alert
  # ----------------------------

  Scenario: Handle simple JavaScript alert
    When user clicks on Simple Alert button
    Then alert should be displayed
    When user accepts the alert
    Then alert should be closed successfully
    And page should remain stable

  # ----------------------------
  # STEP 2: Confirm Alert (OK/Cancel)
  # ----------------------------

  Scenario: Handle confirmation alert - accept
    When user clicks on Confirm Alert button
    Then confirmation alert should be displayed
    When user clicks OK on alert
    Then success message for OK should be displayed

  Scenario: Handle confirmation alert - dismiss
    When user clicks on Confirm Alert button
    Then confirmation alert should be displayed
    When user clicks Cancel on alert
    Then cancel message should be displayed

  # ----------------------------
  # STEP 3: Prompt Alert
  # ----------------------------

  Scenario: Handle prompt alert with input text
    When user clicks on Prompt Alert button
    Then prompt alert should be displayed
    When user enters "TestUser" in alert
    And user accepts the prompt alert
    Then entered text should be displayed on page

  # ----------------------------
  # STEP 4: Sweet Alert - Simple Dialog
  # ----------------------------

  Scenario: Handle Sweet Alert simple dialog
    When user clicks on Sweet Alert simple button
    Then sweet alert dialog should be visible
    When user clicks dismiss button
    Then dialog should be closed

  # ----------------------------
  # STEP 5: Sweet Modal Dialog (Blocking UI)
  # ----------------------------

  Scenario: Handle sweet modal dialog and close
    When user opens Sweet Modal Dialog
    Then modal dialog should be visible
    And background should be blocked
    When user clicks dismiss button on modal
    Then modal should be closed
    And user should regain page control

  # ----------------------------
  # STEP 6: Sweet Alert Confirmation
  # ----------------------------

  Scenario: Handle sweet alert confirmation - yes
    When user clicks Sweet Alert Confirmation button
    Then confirmation dialog should appear
    When user clicks Yes button
    Then success confirmation message should be shown

  Scenario: Handle sweet alert confirmation - no
    When user clicks Sweet Alert Confirmation button
    Then confirmation dialog should appear
    When user clicks No button
    Then cancellation message should be shown

  # ----------------------------
  # STEP 7: Sweet Alert - Minimize/Maximize
  # ----------------------------

  Scenario: Minimize and maximize sweet alert
    When user opens Sweet Alert with minimize/maximize option
    Then alert should be displayed
    When user minimizes alert
    Then alert should collapse
    When user maximizes alert
    Then alert should expand again

  # ----------------------------
  # STEP 8: Negative / Edge Cases
  # ----------------------------

  Scenario: Handle alert without accepting (timeout behavior)
    When user triggers simple alert
    Then alert should be present
    And system should wait for alert handling
    When user does not interact with alert for 3 seconds
    Then alert should still be active or handled safely

  Scenario: Switch context without handling alert
    When user triggers alert
    And user tries to interact with page elements
    Then interaction should be blocked until alert is handled

  Scenario: Invalid alert handling attempt
    When no alert is present
    And user tries to accept alert
    Then NoAlertPresentException should be handled gracefully