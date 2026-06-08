Feature: Drag and Drop - LeafGround
  Validate drag and drop interactions within the page

  Background:
    Given user launches the browser
    When user navigates to Drag and Drop page
    Then the page should be loaded successfully

  # ----------------------------
  # STEP 1: Basic Drag and Drop
  # ----------------------------

  Scenario: Perform simple drag and drop
    When user drags element from source location
    And user drops element into target location
    Then element should be dropped successfully
    And target should reflect dropped element

  # ----------------------------
  # STEP 2: Reorder Drag and Drop (if available)
  # ----------------------------

  Scenario: Drag element within list for reordering
    When user drags item from position 1
    And user drops it at position 3
    Then item should be reordered successfully
    And new order should be reflected correctly

  # ----------------------------
  # STEP 3: Drag and drop verification
  # ----------------------------

  Scenario: Validate drag operation events
    When user starts dragging element
    Then drag should initiate successfully
    When user drops element into target
    Then drop event should be triggered successfully

  # ----------------------------
  # STEP 4: Multiple drag and drop actions
  # ----------------------------

  Scenario: Perform multiple drag and drop operations
    When user performs drag and drop action 3 times
    Then all drag operations should complete successfully
    And final UI state should be correct

  # ----------------------------
  # EDGE CASES
  # ----------------------------

  Scenario: Drop outside valid target
    When user drags element
    And user drops element outside valid drop zone
    Then element should return to original position

  Scenario: Rapid drag and drop actions
    When user performs drag and drop rapidly multiple times
    Then application should not crash
    And UI should remain stable

  Scenario: Drag without dropping
    When user starts dragging element but does not drop it
    Then element should return to original position after release