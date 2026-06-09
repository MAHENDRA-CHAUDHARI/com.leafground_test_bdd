Feature: Frame Handling - LeafGround
  Validate single, nested, and multiple iframe interactions

  Background:
    Given user launches the browser "<browserName>"
    Given user enter url and open Home page for Ground Leaf
    When user click on Browser Menu
    When user navigates to Frame Handling page
    Then the page should be loaded successfully

  # ----------------------------
  # STEP 1: Single Frame
  # ----------------------------
@Regression @Frame_Handling
  Scenario: Handle single frame click action
    When user switches to first frame
    Then user clicks on Click Me button inside inner frame
    And user should return to main page context
    Then closed browser window

  # ----------------------------
  # STEP 2: Nested Frame
  # ----------------------------
@Regression @Frame_Handling
  Scenario: Handle nested frame interaction
    When user switches to inner frame
    And user clicks on Click Me button inside nested frame
    And user should return to parent frame
    Then closed browser window

  # ----------------------------
  # STEP 3: Count number of frames
  # ----------------------------
@Regression @Frame_Handling
  Scenario: Validate total number of frames
    When user retrieves all iframe elements
    Then frame count should be greater than or equal to expected value
    Then closed browser window

  # ----------------------------
  # STEP 4: Frame navigation stability
  # ----------------------------
@Regression @Frame_Handling
  Scenario: Switch between frames and main page repeatedly
    When user switches to frame
    And user returns to default content
    And user switches again to another frame
    Then all frame switches should work without exception
    Then closed browser window

  # ----------------------------
  # EDGE CASES
  # ----------------------------
@Regression @Frame_Handling
  Scenario: Switch to invalid frame index
    When user tries to switch to non-existing frame
    Then NoSuchFrameException should be handled gracefully
    Then closed browser window
    
@Regression @Frame_Handling
  Scenario: Perform action without switching frame
    When user tries to click element inside frame without switching
    Then StaleElementReferenceException or failure should occur safely
    Then closed browser window