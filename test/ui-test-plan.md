# UI Test Plan

The test runner compiles the Java files in `src/main/java`, starts a fresh `Beemo` process for each case, sends the listed commands through standard input, and compares the complete standard output exactly. Cases run from top to bottom and testing stops at the first failure.

## Test case: Add and list a todo

**Aim:** Verify that a todo is stored with the correct type and incomplete status.

### Input

```text
todo borrow book
list
bye
```

### Expected output

```text
____________________________________________________________
 ____  _____ _____ __  __  ___  
| __ )| ____| ____|  \/  |/ _ \ 
|  _ \|  _| |  _| | |\/| | | | |
| |_) | |___| |___| |  | | |_| |
|____/|_____|_____|_|  |_|\___/ 
Hello! I'm Beemo.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Beemo signing off! See you next time! ૮ ˶ᵔ ᵕ ᵔ˶ ა
____________________________________________________________
```

## Test case: Invalid additions preserve task order

**Aim:** Verify that rejected task commands do not add entries or shift the numbering of valid tasks.

### Input

```text
list
todo read book
todo
deadline return book
deadline return book /by Sunday
event meeting /from Monday
event meeting /from Monday /to Tuesday
blah
list
bye
```

### Expected output

```text
____________________________________________________________
 ____  _____ _____ __  __  ___  
| __ )| ____| ____|  \/  |/ _ \ 
|  _ \|  _| |  _| | |\/| | | | |
| |_) | |___| |___| |  | | |_| |
|____/|_____|_____|_|  |_|\___/ 
Hello! I'm Beemo.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS... The description of a todo cannot be empty. ╥‸╥
____________________________________________________________
____________________________________________________________
OOPS... A deadline needs a '/by' date or time. ╥‸╥
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS... An event needs both '/from' and '/to' times. ╥‸╥
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] meeting (from: Monday to: Tuesday)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS... I don't know what that means ╥‸╥
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] meeting (from: Monday to: Tuesday)
____________________________________________________________
____________________________________________________________
Beemo signing off! See you next time! ૮ ˶ᵔ ᵕ ᵔ˶ ა
____________________________________________________________
```

## Test case: Invalid status changes preserve completion state

**Aim:** Verify that invalid mark and unmark commands do not alter valid tasks or prevent later status updates.

### Input

```text
todo read book
deadline return book /by Sunday
mark 1
mark 0
mark two
mark 3
unmark 2
unmark 1
unmark -1
mark 2
list
bye
```

### Expected output

```text
____________________________________________________________
 ____  _____ _____ __  __  ___  
| __ )| ____| ____|  \/  |/ _ \ 
|  _ \|  _| |  _| | |\/| | | | |
| |_) | |___| |___| |  | | |_| |
|____/|_____|_____|_|  |_|\___/ 
Hello! I'm Beemo.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [T][X] read book
____________________________________________________________
____________________________________________________________
OOPS... Task 0 is not in your list. ╥‸╥
____________________________________________________________
____________________________________________________________
OOPS... 'two' is not a valid task number. ╥‸╥
____________________________________________________________
____________________________________________________________
OOPS... Task 3 is not in your list. ╥‸╥
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] read book
____________________________________________________________
____________________________________________________________
OOPS... Task -1 is not in your list. ╥‸╥
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][ ] read book
2.[D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Beemo signing off! See you next time! ૮ ˶ᵔ ᵕ ᵔ˶ ა
____________________________________________________________
```

## Test case: Recover from invalid commands

**Aim:** Verify that invalid task commands report specific errors and do not terminate the program.

### Input

```text
todo
blah
deadline submit report
event meeting /from Monday
mark
mark two
mark 1
todo borrow book
mark 2
bye
```

### Expected output

```text
____________________________________________________________
 ____  _____ _____ __  __  ___  
| __ )| ____| ____|  \/  |/ _ \ 
|  _ \|  _| |  _| | |\/| | | | |
| |_) | |___| |___| |  | | |_| |
|____/|_____|_____|_|  |_|\___/ 
Hello! I'm Beemo.
What can I do for you?
____________________________________________________________
____________________________________________________________
OOPS... The description of a todo cannot be empty. ╥‸╥
____________________________________________________________
____________________________________________________________
OOPS... I don't know what that means ╥‸╥
____________________________________________________________
____________________________________________________________
OOPS... A deadline needs a '/by' date or time. ╥‸╥
____________________________________________________________
____________________________________________________________
OOPS... An event needs both '/from' and '/to' times. ╥‸╥
____________________________________________________________
____________________________________________________________
OOPS... Please provide a task number after 'mark'.
____________________________________________________________
____________________________________________________________
OOPS... 'two' is not a valid task number. ╥‸╥
____________________________________________________________
____________________________________________________________
OOPS... Task 1 is not in your list. ╥‸╥
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS... Task 2 is not in your list. ╥‸╥
____________________________________________________________
____________________________________________________________
Beemo signing off! See you next time! ૮ ˶ᵔ ᵕ ᵔ˶ ა
____________________________________________________________
```

## Test case: Preserve deadline and event text

**Aim:** Verify deadline and event details remain strings and task status can be marked and unmarked.

### Input

```text
deadline do homework /by no idea :-p
event project meeting /from Mon 2pm /to 4pm
mark 2
unmark 2
list
bye
```

### Expected output

```text
____________________________________________________________
 ____  _____ _____ __  __  ___  
| __ )| ____| ____|  \/  |/ _ \ 
|  _ \|  _| |  _| | |\/| | | | |
| |_) | |___| |___| |  | | |_| |
|____/|_____|_____|_|  |_|\___/ 
Hello! I'm Beemo.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] do homework (by: no idea :-p)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[D][ ] do homework (by: no idea :-p)
2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Beemo signing off! See you next time! ૮ ˶ᵔ ᵕ ᵔ˶ ა
____________________________________________________________
```
