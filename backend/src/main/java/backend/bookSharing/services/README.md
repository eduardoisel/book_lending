# Organization

The code is divided between book and user related services. Each as its own interface and implementation, along with
its microservices and data classes.

## Return types

The interfaces use 2 classes on its return type; Optional and io.vavr Either. 

Either is of type left or right, with left being chosen as the one to return the reason for failure of
the action, and right returning whatever is necessary to return on success. Returning left does rollback on method.

If success of an action does not need to return anything, Optional class is used, only having something to return on
failure.