package com.oasisinfobyte.examination.util;

import com.oasisinfobyte.examination.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Question bank supplying curated Java fundamentals examination questions.
 */
public final class QuestionBank {

    private QuestionBank() {
        // Prevent instantiation
    }

    /**
     * Constructs the standard 10-question Java Fundamentals question list.
     *
     * @return List of 10 curated Java questions with 4 options each.
     */
    public static List<Question> getDefaultQuestions() {
        List<Question> questions = new ArrayList<>();

        // Question 1: OOP - Encapsulation
        questions.add(new Question(
                1,
                "Which fundamental OOP principle is specifically concerned with bundling data with methods and hiding internal state?",
                List.of(
                        "Inheritance",
                        "Encapsulation",
                        "Polymorphism",
                        "Abstraction"
                ),
                1, // B: Encapsulation
                "Encapsulation is the mechanism of wrapping code and data together into a single unit while restricting direct access to components via private access modifiers.",
                "OOP Principles"
        ));

        // Question 2: OOP - Inheritance
        questions.add(new Question(
                2,
                "In Java, which keyword is used by a class to inherit another class?",
                List.of(
                        "implements",
                        "inherits",
                        "extends",
                        "super"
                ),
                2, // C: extends
                "The 'extends' keyword is used in Java class declarations to establish an inheritance relationship with a parent class.",
                "Inheritance"
        ));

        // Question 3: Polymorphism - Method Overloading vs Overriding
        questions.add(new Question(
                3,
                "Method overriding in Java is an example of which type of polymorphism?",
                List.of(
                        "Compile-time polymorphism",
                        "Runtime (dynamic) polymorphism",
                        "Parametric polymorphism",
                        "Ad-hoc polymorphism"
                ),
                1, // B: Runtime polymorphism
                "Method overriding resolves method calls at runtime based on the actual object instance type, which demonstrates dynamic / runtime polymorphism.",
                "Polymorphism"
        ));

        // Question 4: Interfaces
        questions.add(new Question(
                4,
                "Since Java 8, which of the following can an interface contain with a concrete implementation body?",
                List.of(
                        "Only final static fields",
                        "Default and static methods",
                        "Protected constructor methods",
                        "Transient synchronized methods"
                ),
                1, // B: Default and static methods
                "Java 8 introduced default and static methods in interfaces, permitting interface methods to provide concrete default implementation bodies.",
                "Interfaces"
        ));

        // Question 5: Exception Handling
        questions.add(new Question(
                5,
                "Which exception type is an unchecked exception in Java?",
                List.of(
                        "java.io.IOException",
                        "java.sql.SQLException",
                        "java.lang.NullPointerException",
                        "java.lang.ClassNotFoundException"
                ),
                2, // C: NullPointerException
                "NullPointerException inherits from RuntimeException, making it an unchecked exception that does not require mandatory catch or throws declaration.",
                "Exception Handling"
        ));

        // Question 6: Java Memory Management
        questions.add(new Question(
                6,
                "Where are Java object instances dynamically allocated in memory at runtime?",
                List.of(
                        "Call Stack",
                        "Heap Memory",
                        "Method Area",
                        "Program Counter Register"
                ),
                1, // B: Heap Memory
                "All Java object instances and their instance variables are allocated inside Heap Memory, managed by the Garbage Collector.",
                "JVM & Memory"
        ));

        // Question 7: String Immutability
        questions.add(new Question(
                7,
                "Why are String objects immutable in Java?",
                List.of(
                        "Because their internal character array is private and final, and String has no mutator methods",
                        "Because JVM prohibits modifying any class located in java.lang",
                        "Because strings cannot be garbage collected",
                        "Because they inherit directly from java.lang.Number"
                ),
                0, // A: private and final value array
                "String objects cannot have their contents changed after creation because String's internal storage is private final and the class is final.",
                "Strings"
        ));

        // Question 8: Java Collections Framework
        questions.add(new Question(
                8,
                "Which Java Collections Framework interface prohibits duplicate elements?",
                List.of(
                        "java.util.List",
                        "java.util.Queue",
                        "java.util.Set",
                        "java.util.Vector"
                ),
                2, // C: Set
                "The Set interface models the mathematical set abstraction and explicitly contractually disallows duplicate elements.",
                "Collections"
        ));

        // Question 9: Multi-Threading / Synchronization
        questions.add(new Question(
                9,
                "Which keyword ensures that only one thread can execute a critical code block or method at a time on an object's monitor?",
                List.of(
                        "volatile",
                        "synchronized",
                        "transient",
                        "atomic"
                ),
                1, // B: synchronized
                "The 'synchronized' keyword acquires an intrinsic lock (monitor) on an object, guaranteeing mutual exclusion and visibility across threads.",
                "Concurrency"
        ));

        // Question 10: JVM Architecture
        questions.add(new Question(
                10,
                "What is the role of the Just-In-Time (JIT) compiler within the Java Virtual Machine?",
                List.of(
                        "To compile .java source code files into .class bytecode files",
                        "To compile frequently executed bytecode sequences into native machine code at runtime",
                        "To verify bytecodes for memory safety before class loading",
                        "To compress compiled class files into JAR archives"
                ),
                1, // B: compile bytecode into native machine code
                "The JIT compiler analyzes bytecode execution profiles at runtime and compiles hot code paths into optimized native machine code for superior execution speed.",
                "JVM & Performance"
        ));

        // ── Collections Framework (questions 11-15) ───────────────────────────

        // Question 11: Collections - HashMap null keys
        questions.add(new Question(
                11,
                "Which Java collection class allows null keys and null values?",
                List.of(
                        "java.util.Hashtable",
                        "java.util.HashMap",
                        "java.util.TreeMap",
                        "java.util.ConcurrentHashMap"
                ),
                1, // B: HashMap
                "HashMap allows one null key and multiple null values. Hashtable, TreeMap, and ConcurrentHashMap all reject null keys.",
                "Collections Framework"
        ));

        // Question 12: Collections - ArrayList vs LinkedList
        questions.add(new Question(
                12,
                "Which data-structure backs a java.util.ArrayList?",
                List.of(
                        "Doubly linked list",
                        "Circular buffer",
                        "Dynamic (resizable) array",
                        "Red-black tree"
                ),
                2, // C: Dynamic array
                "ArrayList is backed by a plain Object array that is replaced with a larger copy whenever capacity is exceeded.",
                "Collections Framework"
        ));

        // Question 13: Collections - Iterator fail-fast
        questions.add(new Question(
                13,
                "What exception does a fail-fast Iterator throw when the collection is structurally modified during iteration?",
                List.of(
                        "java.lang.IllegalStateException",
                        "java.util.ConcurrentModificationException",
                        "java.lang.UnsupportedOperationException",
                        "java.util.NoSuchElementException"
                ),
                1, // B: ConcurrentModificationException
                "Fail-fast iterators check an internal modCount; a structural modification outside the iterator causes ConcurrentModificationException on the next iterator call.",
                "Collections Framework"
        ));

        // Question 14: Collections - TreeMap ordering
        questions.add(new Question(
                14,
                "In what order does java.util.TreeMap store its keys by default?",
                List.of(
                        "Insertion order",
                        "Natural ascending (sorted) order",
                        "Access order (most-recently-used last)",
                        "Hash-code order"
                ),
                1, // B: Natural sorted order
                "TreeMap is a Red-Black tree implementation of NavigableMap; it stores entries sorted by keys' natural order unless a custom Comparator is supplied.",
                "Collections Framework"
        ));

        // Question 15: Collections - LinkedHashMap
        questions.add(new Question(
                15,
                "Which Java Map implementation preserves insertion order while also providing O(1) average-case get/put performance?",
                List.of(
                        "java.util.TreeMap",
                        "java.util.HashMap",
                        "java.util.LinkedHashMap",
                        "java.util.IdentityHashMap"
                ),
                2, // C: LinkedHashMap
                "LinkedHashMap extends HashMap with a doubly-linked list that maintains insertion (or optionally access) order at no extra asymptotic cost per operation.",
                "Collections Framework"
        ));

        // ── Exception Handling (questions 16-20) ──────────────────────────────

        // Question 16: Exception - finally block
        questions.add(new Question(
                16,
                "When does a finally block NOT execute after a try block?",
                List.of(
                        "When an exception is thrown inside the try block",
                        "When a return statement is encountered inside the try block",
                        "When System.exit() is called inside the try block",
                        "When a checked exception is caught by an enclosing catch block"
                ),
                2, // C: System.exit()
                "A finally block always runs after try/catch except when System.exit() is invoked (or the JVM crashes), which halts the JVM immediately.",
                "Exception Handling"
        ));

        // Question 17: Exception - checked vs unchecked
        questions.add(new Question(
                17,
                "Which of the following is a checked exception?",
                List.of(
                        "java.lang.ArrayIndexOutOfBoundsException",
                        "java.lang.StackOverflowError",
                        "java.io.IOException",
                        "java.lang.NullPointerException"
                ),
                2, // C: IOException
                "Checked exceptions extend Exception (but not RuntimeException) and must be declared with 'throws' or handled with try-catch; IOException is a classic example.",
                "Exception Handling"
        ));

        // Question 18: Exception - custom exception
        questions.add(new Question(
                18,
                "To create a custom checked exception in Java, your class should extend which class?",
                List.of(
                        "java.lang.RuntimeException",
                        "java.lang.Throwable",
                        "java.lang.Error",
                        "java.lang.Exception"
                ),
                3, // D: Exception
                "Extending java.lang.Exception (but not RuntimeException) makes the custom exception checked, requiring callers to handle or declare it.",
                "Exception Handling"
        ));

        // Question 19: Exception - try-with-resources
        questions.add(new Question(
                19,
                "Which interface must a resource implement to be used in a try-with-resources statement?",
                List.of(
                        "java.io.Closeable",
                        "java.lang.AutoCloseable",
                        "java.io.Flushable",
                        "java.lang.Iterable"
                ),
                1, // B: AutoCloseable
                "try-with-resources automatically closes any resource whose type implements java.lang.AutoCloseable (or its sub-interface Closeable).",
                "Exception Handling"
        ));

        // Question 20: Exception - chained exceptions
        questions.add(new Question(
                20,
                "Which Throwable method retrieves the original cause of a chained exception?",
                List.of(
                        "getMessage()",
                        "getStackTrace()",
                        "getCause()",
                        "getSuppressed()"
                ),
                2, // C: getCause()
                "Exception chaining stores the originating exception via initCause() or a constructor argument; getCause() returns that stored cause.",
                "Exception Handling"
        ));

        // ── Multithreading (questions 21-25) ──────────────────────────────────

        // Question 21: Multithreading - Thread vs Runnable
        questions.add(new Question(
                21,
                "What is the preferred way to define a task for a new thread when your class already extends another class?",
                List.of(
                        "Extend java.lang.Thread",
                        "Implement java.lang.Runnable",
                        "Implement java.util.concurrent.Callable",
                        "Extend java.util.TimerTask"
                ),
                1, // B: Implement Runnable
                "Since Java supports only single inheritance, implementing Runnable leaves the class free to extend another class and separates task logic from thread management.",
                "Multithreading"
        ));

        // Question 22: Multithreading - volatile
        questions.add(new Question(
                22,
                "What guarantee does the 'volatile' keyword provide for a variable in Java?",
                List.of(
                        "Only one thread can read the variable at a time",
                        "All writes to the variable are immediately flushed to main memory and all reads bypass thread-local caches",
                        "The variable cannot be reassigned after initialisation",
                        "The variable is excluded from garbage collection"
                ),
                1, // B: main memory visibility
                "'volatile' ensures memory visibility: writes are immediately written to main memory and reads always reflect the latest value, preventing stale cached copies.",
                "Multithreading"
        ));

        // Question 23: Multithreading - deadlock
        questions.add(new Question(
                23,
                "What is a deadlock in a multi-threaded Java application?",
                List.of(
                        "A thread waiting forever because notify() was never called",
                        "Two or more threads each holding a lock the other needs, causing all to block indefinitely",
                        "An exception thrown when two threads access the same non-synchronized method",
                        "A situation where a high-priority thread starves a low-priority thread"
                ),
                1, // B: circular lock dependency
                "Deadlock occurs when thread A holds lock X and waits for lock Y while thread B holds lock Y and waits for lock X, creating a circular dependency.",
                "Multithreading"
        ));

        // Question 24: Multithreading - ExecutorService
        questions.add(new Question(
                24,
                "Which java.util.concurrent method returns an ExecutorService backed by a fixed-size thread pool?",
                List.of(
                        "Executors.newCachedThreadPool()",
                        "Executors.newSingleThreadExecutor()",
                        "Executors.newFixedThreadPool(int nThreads)",
                        "Executors.newScheduledThreadPool(int corePoolSize)"
                ),
                2, // C: newFixedThreadPool
                "Executors.newFixedThreadPool(n) creates a pool with exactly n worker threads; excess tasks queue until a thread becomes available.",
                "Multithreading"
        ));

        // Question 25: Multithreading - wait/notify
        questions.add(new Question(
                25,
                "In Java, calling wait() on an object inside a synchronized block causes the current thread to do what?",
                List.of(
                        "Sleep for a fixed interval and then reacquire the lock",
                        "Release the lock and enter a waiting state until notified",
                        "Spin in a tight loop checking a condition flag",
                        "Terminate the current thread and restart it"
                ),
                1, // B: release lock and wait
                "wait() atomically releases the object's monitor lock and suspends the thread; it resumes only after another thread calls notify() or notifyAll() on the same object.",
                "Multithreading"
        ));

        // ── JDBC & Databases (questions 26-30) ────────────────────────────────

        // Question 26: JDBC - PreparedStatement vs Statement
        questions.add(new Question(
                26,
                "Why should PreparedStatement be preferred over Statement for SQL queries that accept user input?",
                List.of(
                        "PreparedStatement is faster to instantiate",
                        "PreparedStatement automatically logs all queries",
                        "PreparedStatement uses parameterised placeholders, preventing SQL injection",
                        "PreparedStatement supports only SELECT statements"
                ),
                2, // C: SQL injection prevention
                "Parameterised queries in PreparedStatement separate SQL code from data, so user-supplied input is treated as a value and cannot alter query structure.",
                "JDBC & Databases"
        ));

        // Question 27: JDBC - ResultSet navigation
        questions.add(new Question(
                27,
                "What does ResultSet.next() return when it moves past the last row of the result set?",
                List.of(
                        "null",
                        "0",
                        "false",
                        "It throws java.sql.SQLException"
                ),
                2, // C: false
                "ResultSet.next() returns true when it successfully advances to the next row and false when there are no more rows.",
                "JDBC & Databases"
        ));

        // Question 28: JDBC - transactions
        questions.add(new Question(
                28,
                "To execute multiple SQL statements as a single atomic unit of work in JDBC, which Connection method must be called first?",
                List.of(
                        "connection.setAutoCommit(false)",
                        "connection.beginTransaction()",
                        "connection.setIsolationLevel(SERIALIZABLE)",
                        "connection.lock()"
                ),
                0, // A: setAutoCommit(false)
                "By default JDBC auto-commits each statement. Calling setAutoCommit(false) starts a manual transaction; you then call commit() or rollback() explicitly.",
                "JDBC & Databases"
        ));

        // Question 29: JDBC - Connection creation
        questions.add(new Question(
                29,
                "Which class is the entry point for obtaining a database Connection in JDBC?",
                List.of(
                        "java.sql.Statement",
                        "java.sql.DriverManager",
                        "java.sql.DataSource",
                        "java.sql.ResultSet"
                ),
                1, // B: DriverManager
                "DriverManager.getConnection(url, user, password) is the standard JDBC entry point; production code often uses a DataSource pool instead.",
                "JDBC & Databases"
        ));

        // Question 30: JDBC - SQLException
        questions.add(new Question(
                30,
                "SQLException in Java is classified as which type of exception?",
                List.of(
                        "Unchecked (RuntimeException subclass)",
                        "Error subclass",
                        "Checked exception (Exception subclass)",
                        "AssertionError subclass"
                ),
                2, // C: checked exception
                "java.sql.SQLException extends java.lang.Exception, making it a checked exception that callers must catch or declare with throws.",
                "JDBC & Databases"
        ));

        // ── Data Structures (questions 31-35) ─────────────────────────────────

        // Question 31: Data Structures - Array vs ArrayList
        questions.add(new Question(
                31,
                "What is the time complexity of accessing an element by index in a Java array or ArrayList?",
                List.of(
                        "O(log n)",
                        "O(n)",
                        "O(1)",
                        "O(n log n)"
                ),
                2, // C: O(1)
                "Both arrays and ArrayList use contiguous memory/index mapping, so any element is reachable in constant time.",
                "Data Structures"
        ));

        // Question 32: Data Structures - stack behaviour
        questions.add(new Question(
                32,
                "Which data structure follows the Last-In-First-Out (LIFO) access principle?",
                List.of(
                        "Queue",
                        "Stack",
                        "Deque used as a queue",
                        "Priority Queue"
                ),
                1, // B: Stack
                "A stack processes elements in LIFO order; the last element pushed is the first one popped.",
                "Data Structures"
        ));

        // Question 33: Data Structures - linked list insertion
        questions.add(new Question(
                33,
                "What is the time complexity of inserting a node at the head of a singly linked list?",
                List.of(
                        "O(n)",
                        "O(log n)",
                        "O(n log n)",
                        "O(1)"
                ),
                3, // D: O(1)
                "Inserting at the head only requires updating the head pointer and the new node's next reference—constant time regardless of list length.",
                "Data Structures"
        ));

        // Question 34: Data Structures - binary search tree
        questions.add(new Question(
                34,
                "In a balanced Binary Search Tree (BST), what is the average time complexity of a search operation?",
                List.of(
                        "O(1)",
                        "O(log n)",
                        "O(n)",
                        "O(n²)"
                ),
                1, // B: O(log n)
                "Each comparison halves the remaining search space in a balanced BST, yielding O(log n) for search, insert, and delete.",
                "Data Structures"
        ));

        // Question 35: Data Structures - queue
        questions.add(new Question(
                35,
                "Which data structure is most appropriate for implementing a breadth-first search (BFS) of a graph?",
                List.of(
                        "Stack",
                        "Priority Queue",
                        "Queue",
                        "Circular buffer"
                ),
                2, // C: Queue
                "BFS visits nodes level by level; a queue's FIFO order ensures nodes are explored in the order they are discovered.",
                "Data Structures"
        ));

        // ── Logical Reasoning (questions 36-40) ───────────────────────────────

        // Question 36: Logical Reasoning - Fibonacci
        questions.add(new Question(
                36,
                "What is the next number in the Fibonacci sequence: 1, 1, 2, 3, 5, 8, 13, ?",
                List.of(
                        "18",
                        "19",
                        "21",
                        "24"
                ),
                2, // C: 21
                "Each Fibonacci term is the sum of the two preceding ones: 13 + 8 = 21.",
                "Logical Reasoning"
        ));

        // Question 37: Logical Reasoning - power of 2
        questions.add(new Question(
                37,
                "A variable is doubled at every step starting from 1. After 5 doublings, what is its value?",
                List.of(
                        "10",
                        "16",
                        "32",
                        "64"
                ),
                2, // C: 32
                "Starting at 1 and doubling 5 times: 1 → 2 → 4 → 8 → 16 → 32. That is 2^5 = 32.",
                "Logical Reasoning"
        ));

        // Question 38: Logical Reasoning - loop count
        questions.add(new Question(
                38,
                "How many times does the following loop body execute? for (int i = 0; i < 10; i += 3)",
                List.of(
                        "3",
                        "4",
                        "10",
                        "5"
                ),
                1, // B: 4
                "i takes values 0, 3, 6, 9 (all < 10), then i = 12 exits the loop — 4 iterations.",
                "Logical Reasoning"
        ));

        // Question 39: Logical Reasoning - bit shifting
        questions.add(new Question(
                39,
                "What is the result of the expression 8 >> 1 in Java (arithmetic right shift)?",
                List.of(
                        "16",
                        "2",
                        "4",
                        "1"
                ),
                2, // C: 4
                "Right-shifting by 1 is equivalent to integer division by 2: 8 >> 1 = 8 / 2 = 4.",
                "Logical Reasoning"
        ));

        // Question 40: Logical Reasoning - modulo pattern
        questions.add(new Question(
                40,
                "Which expression correctly determines whether an integer n is even?",
                List.of(
                        "n % 2 == 1",
                        "n / 2 == 0",
                        "n % 2 == 0",
                        "n & 1 == 1"
                ),
                2, // C: n % 2 == 0
                "The modulo operator returns the remainder; an even number divided by 2 leaves remainder 0, so n % 2 == 0 is the standard evenness check.",
                "Logical Reasoning"
        ));

        return Collections.unmodifiableList(questions);
    }
}
