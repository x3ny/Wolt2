# Wolt2 TODO

## Current milestone: reliable restaurant order workflow

- [ ] Review and test the working customer, driver, and payment comboboxes
- [ ] Create an order successfully from the restaurant screen
- [ ] Validate total price, including negative, zero, and invalid values
- [ ] Validate delivery address input
- [ ] Verify that a new order starts with `CREATED` status
- [ ] Add valid order status transitions
  - [ ] `CREATED` → `ACCEPTED`
  - [ ] `ACCEPTED` → `PREPARING`
  - [ ] `PREPARING` → `DELIVERED`
  - [ ] Allow cancellation only before delivery
- [ ] Prevent invalid status jumps, such as `CREATED` → `DELIVERED`
- [ ] Add a confirmation before deleting an order
- [ ] Test the main success and failure cases
- [ ] Review the changes and commit them to Git

## Suggested next exercise

- [ ] Implement `canChangeStatus(currentStatus, nextStatus)`
- [ ] Use it inside `updateSelectedOrderStatus()`
- [ ] Show a clear warning when a transition is not allowed

## Later improvements

- [ ] Move database operations out of the JavaFX controller
- [ ] Add automated tests for validation and status transitions
- [ ] Improve error handling so technical exceptions are not shown directly to users
