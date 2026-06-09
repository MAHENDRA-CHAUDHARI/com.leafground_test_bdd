Feature: Alert Handling - LeafGround
  Validate different types of JavaScript and Sweet Alerts

  Background:
    Given user launches the browser "<browserName>"
    When user navigates to the Alert Handling page
    Then the page should be loaded successfully

  # ----------------------------
  # STEP 1: Simple Alert
  # ----------------------------
@Regression @Alert_Handling
  Scenario: Handle simple JavaScript alert
    When user clicks on Simple Alert button
    Then alert should be displayed
    When user accepts the alert
    Then alert should be closed successfully
    And page should remain stable

  # ----------------------------
  # STEP 2: Confirm Alert (OK/Cancel)
  # ----------------------------
@Regression @Alert_Handling
  Scenario: Handle confirmation alert - accept
    When user clicks on Confirm Alert button
    Then confirmation alert should be displayed
    When user clicks OK on alert
    Then success message for OK should be displayed

@Regression @Alert_Handling
  Scenario: Handle confirmation alert - dismiss
    When user clicks on Confirm Alert button
    Then confirmation alert should be displayed
    When user clicks Cancel on alert
    Then cancel message should be displayed

  # ----------------------------
  # STEP 3: Prompt Alert
  # ----------------------------
@Regression @Alert_Handling
  Scenario: Handle prompt alert with input text
    When user clicks on Prompt Alert button
    Then prompt alert should be displayed
    When user enters "TestUser" in alert
    And user accepts the prompt alert
    Then entered text should be displayed on page

  # ----------------------------
  # STEP 4: Sweet Alert - Simple Dialog
  # ----------------------------
@Regression @Alert_Handling
  Scenario: Handle Sweet Alert simple dialog
    When user clicks on Sweet Alert simple button
    Then sweet alert dialog should be visible
    When user clicks dismiss button
    Then dialog should be closed

  # ----------------------------
  # STEP 5: Sweet Modal Dialog (Blocking UI)
  # ----------------------------
@Regression @Alert_Handling
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
@Regression @Alert_Handling
  Scenario: Handle sweet alert confirmation - yes
    When user clicks Sweet Alert Confirmation button
    Then confirmation dialog should appear
    When user clicks Yes button
    Then success confirmation message should be shown

@Regression @Alert_Handling
  Scenario: Handle sweet alert confirmation - no
    When user clicks Sweet Alert Confirmation button
    Then confirmation dialog should appear
    When user clicks No button
    Then cancellation message should be shown