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
